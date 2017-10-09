package codes.evolution.uihintslib;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import codes.evolution.base.Assert;
import codes.evolution.uihintslib.ui.HintParams;
import codes.evolution.uihintslib.ui.HintsViewFactory;
import codes.evolution.uihintslib.ui.UiHintsViewController;
import codes.evolution.uihintslib.ui.utils.DisplayInfo;
import codes.evolution.uihintslib.ui.utils.FullscreenDetector;

public class HintsFlowController<ParamsType extends HintParams> {

    private static final String TAG = "HintsFlowController";

    private final HintsStorage<ParamsType> mHintsStorage;
    private final HintsViewFactory mHintsViewFactory;
    private final UiHintsViewController mUiHintsViewController;
    private final Handler mUiHandler;

    private @Nullable Flow mFlow;
    private @Nullable Hint mCurrentHint;

    private final HintsStorage.HintsStorageListener mStorageListener =
            new HintsStorage.HintsStorageListener() {
                @Override
                public void onStorageEvent(HintsStorage.StorageEvent event,
                                           @Nullable @Hint.Name String hintName) {
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
                        if (mCurrentHint != null && mCurrentHint.equals(hint)) {
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

    public HintsFlowController(Context context,
                               HintsStorage<ParamsType> hintsStorage,
                               HintsViewFactory hintsViewFactory) {
        mHintsStorage = hintsStorage;
        mHintsViewFactory = hintsViewFactory;

        FullscreenDetector detector = new FullscreenDetector(context, new DisplayInfo(context));
        mUiHintsViewController = new UiHintsViewController(context, detector);
        mUiHandler = new Handler(Looper.getMainLooper());
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
        mFlow = null;
    }

    @SuppressWarnings("all")
    public void startNextIfNeeded() {
        String nextHintName = mCurrentHint.getNextHintName();
        if (nextHintName != null) {
            Hint hint = mFlow.getHint(nextHintName);
            showHint(hint);
            return;
        }
        hideHintWithAnimation();
    }

    public void exitWizard() {
        Assert.assertNotNull(mFlow);
        if (mFlow != null) {
            mFlow.clear();
            mFlow = null;
        }

        mHintsStorage.removeAllHints();
        mUiHandler.removeCallbacksAndMessages(null);
        mHintsStorage.removeListener(mStorageListener);
    }

    public Hint getCurrentShownHint() {
        return mCurrentHint;
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
        if (mCurrentHint != null && hint.equals(mCurrentHint)) {
            Log.d(TAG, "Temp" + hint + "is showing");
            return;
        }

        ParamsType params = mHintsStorage.getHintParams(hint.getName());
        if (params == null || isHintShown(hint.getName())) {
            Log.d(TAG, "Object params is null or " + hint + " was shown");
            hideHintWithAnimation();
            return;
        }

        mCurrentHint = hint;
        View hintView = mHintsViewFactory.createHintView(hint.getName(), params);

        mUiHintsViewController.show();
        mUiHintsViewController.changeTarget(hintView, params);
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

        mCurrentHint = null;
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

    private boolean isHintShown(@Hint.Name String hintName) {
        return mFlow != null && !mFlow.contains(hintName);
    }

    private void restoreFlow(Flow flow) {
        mFlow = new Flow(flow.getHints());
    }
}
