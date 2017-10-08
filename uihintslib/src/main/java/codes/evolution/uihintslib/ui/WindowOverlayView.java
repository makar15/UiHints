package codes.evolution.uihintslib.ui;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.PixelFormat;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import codes.evolution.base.Assert;
import codes.evolution.uihintslib.ui.utils.AnimatorUtils;

public abstract class WindowOverlayView {

    private static final String ANIMATION_NAME = "alpha";

    private final Context mContext;
    private final int mAnimationDuration;
    private final WindowManager mWindowManager;

    @Nullable private View mShownView;
    @Nullable private ObjectAnimator mAnimator;

    WindowOverlayView(Context context, int animationDuration) {
        mContext = context;
        mAnimationDuration = animationDuration;
        mWindowManager = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE));
    }

    protected abstract View getView();

    protected Context getContext() {
        return mContext;
    }

    public boolean isShown() {
        return mShownView != null && mShownView.isShown();
    }

    public View showWithAnimation() {
        if (mShownView == null) {
            mShownView = getView();
        }
        if (mShownView.isShown()) {
            return mShownView;
        }
        cancelAnimation();
        mShownView.setAlpha(0f);
        mWindowManager.addView(mShownView, getLayoutParams());
        mAnimator = ObjectAnimator.ofFloat(mShownView, ANIMATION_NAME, 0f, 1f);
        mAnimator.setDuration(mAnimationDuration);
        mAnimator.start();

        return mShownView;
    }

    public View show() {
        if (mShownView == null) {
            mShownView = getView();
        }
        if (mShownView.isShown()) {
            return mShownView;
        }
        cancelAnimation();
        mShownView.setAlpha(1f);
        mWindowManager.addView(mShownView, getLayoutParams());
        onShow();
        return mShownView;
    }

    public void hide() {
        cancelAnimation();

        Assert.assertNotNull(mShownView);
        if (mShownView == null) {
            return;
        }
        final View view = mShownView;
        mShownView = null;

        mAnimator = ObjectAnimator.ofFloat(view, ANIMATION_NAME, 1f, 0f);
        mAnimator.setDuration(mAnimationDuration);
        mAnimator.addListener(new AnimatorUtils.SimpleAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                if (view.isShown()) {
                    mWindowManager.removeViewImmediate(view);
                }
            }
        });
        onHide();
        mAnimator.start();
    }

    private void cancelAnimation() {
        if (mAnimator != null) {
            mAnimator.removeAllListeners();
            mAnimator.cancel();
        }
    }

    private WindowManager.LayoutParams getLayoutParams() {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                //WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.TYPE_APPLICATION_PANEL,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP | Gravity.START;
        return params;
    }

    protected void onShow() {}
    protected void onHide() {}
}
