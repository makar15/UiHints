package codes.evolution.uihintslib;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import codes.evolutiom.base.Assert;
import codes.evolution.uihintslib.ui.HintContainerLayout;
import codes.evolution.uihintslib.ui.HintParams;
import codes.evolution.uihintslib.ui.UiHintsViewController;
import codes.evolution.uihintslib.ui.utils.DisplayInfo;
import codes.evolution.uihintslib.ui.utils.FullscreenDetector;

public class HintsFlowController {

    private static final String TAG = "HintsFlowController";

    private final HintsStorage mHintsStorage;
    private final UiHintsViewController mUiHintsViewController;
    private final HintsItemFactory mHintsItemFactory;
    private final View.OnClickListener mHintsButtonListener = new HintsButtonClickListener();
    private final Handler mUiHandler;

    private @Nullable Flow mFlow;
    private @Nullable Hint mCurHint;

    private final HintsStorage.HintsStorageListener mStorageListener =
            new HintsStorage.HintsStorageListener() {
                @Override
                public void onStorageEvent(HintsStorage.StorageEvent event,
                                           @Nullable @Hint.HintName String hintName) {
                    if (event == HintsStorage.StorageEvent.REMOVED_ALL) {
                        mUiHandler.removeCallbacksAndMessages(null);
                        hideHintWithAnimation();
                        return;
                    }

                    Assert.assertNotNull(mFlow);
                    if (mFlow == null) {
                        Log.e(TAG, "Flow is null Object");
                        return;
                    }
                    Hint hint = mFlow.getHint(hintName);
                    if (event == HintsStorage.StorageEvent.REMOVED) {
                        if (mCurHint != null && mCurHint.equals(hint)) {
                            mUiHandler.removeCallbacksAndMessages(hint);
                            hideHint();
                        }
                        return;
                    }

                    // else StorageEvent ADDED
                    if (isHintShown(hintName)) {
                        Log.d(TAG, hint + " was shown");
                        return;
                    }

                    String prevHintName = hint.getPrevHintName();
                    if (isHintShown(prevHintName)) {
                        //showHint(hint);
                        showHintWithDelay(hint, 300);
                    }
                }
            };

    public HintsFlowController(Context context, HintsStorage hintsStorage) {
        mHintsStorage = hintsStorage;

        mUiHandler = new Handler(Looper.getMainLooper());
        FullscreenDetector detector = new FullscreenDetector(context, new DisplayInfo(context));
        mUiHintsViewController = new UiHintsViewController(context, detector);
        mHintsItemFactory = new HintsItemFactory(context);
    }

    public void onCreate(Flow flow) {
        Assert.assertNotNull(flow);
        if (flow == null) {
            Log.e(TAG, "Flow is null Object");
            return;
        }
        mHintsStorage.addListener(mStorageListener);
        restoreFlow(flow);
    }

    public void onDestroy() {
        mHintsStorage.removeListener(mStorageListener);
    }

    private void showHintWithDelay(final Hint hint, int delay) {
        mUiHandler.removeCallbacksAndMessages(null);
        mUiHandler.postAtTime(new Runnable() {
            @Override
            public void run() {
                showHint(hint);
            }
        }, hint, SystemClock.uptimeMillis() + delay);
    }

    private void showHint(Hint hint) {
        if (mCurHint != null && hint.equals(mCurHint)) {
            Log.d(TAG, "Temp" + hint + "is showing");
            return;
        }

        HintParams params = mHintsStorage.getHintParams(hint.getName());
        if (params == null || isHintShown(hint.getName())) {
            Log.d(TAG, "Object params is null or " + hint + " was shown");
            hideHintWithAnimation();
            return;
        }

        mCurHint = hint;
        HintContainerLayout viewHint = mHintsItemFactory.createHintView(mHintsButtonListener,
                hint.getName(), params);
        mUiHintsViewController.show();
        mUiHintsViewController.changeTarget(viewHint, params);
    }

    private void hideHint() {
        hideHint(false);
    }

    private void hideHintWithAnimation() {
        hideHint(true);
    }

    private void hideHint(boolean animation) {
        if (!mUiHintsViewController.isShown()) {
            return;
        }

        mCurHint = null;
        if (!animation) {
            mUiHintsViewController.hide();
            return;
        }
        // This Runnable starts by event onAnimationEnd
        mUiHintsViewController.removeTargets(new Runnable() {
            @Override
            public void run() {
                if (mUiHintsViewController.isShown()) {
                    mUiHintsViewController.hide();
                }
            }
        });
    }

    @SuppressWarnings("all")
    private void startNextIfNeeded() {
        String nextHintName = mCurHint.getNextHintName();
        if (nextHintName != null) {
            Hint hint = mFlow.getHint(nextHintName);
            showHint(hint);
            return;
        }
        hideHintWithAnimation();
    }

    private boolean isHintShown(@Hint.HintName String hintName) {
        return mFlow != null && !mFlow.contains(hintName);
    }

    private void restoreFlow(Flow flow) {
        mFlow = new Flow(flow.getHints());
    }

    private void exitWizard() {
        Assert.assertNotNull(mFlow);
        if (mFlow != null) {
            mFlow.clear();
            mFlow = null;
        }

        mHintsStorage.removeAllHints();
        mUiHandler.removeCallbacksAndMessages(null);
        mHintsStorage.removeListener(mStorageListener);
    }

    /**
     * Handle click on buttons to HintView
     */
    private class HintsButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (mCurHint == null) {
                throw new AssertionError("Current hint should be shown");
            }

            Assert.assertNotNull(mFlow);
            if (mFlow == null) {
                Log.e(TAG, "Flow is null Object");
                return;
            }
            mFlow.remove(mCurHint);
            int i = view.getId();
            if (i == R.id.yes) {
                startNextIfNeeded();
            } else if (i == R.id.no) {
                exitWizard();
            } else if (i == R.id.got_it) {
                startNextIfNeeded();
            }
        }
    }
}
