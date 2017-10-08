package codes.evolution.uihintslib.ui;

import android.animation.Animator;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import junit.framework.Assert;

import java.util.HashMap;
import java.util.Map;

import codes.evolution.uihintslib.AnimateSettings;
import codes.evolution.uihintslib.R;
import codes.evolution.uihintslib.ui.utils.AnimatorUtils;
import codes.evolution.uihintslib.ui.utils.UIUtils;

public class DecoratedHintsLayout extends FrameLayout {

    private static final String TAG = "DecoratedHintsLayout";

    private final ShadowView mShadowView;
    private final Map<HintParams, View> mHints = new HashMap<>();

    protected DecoratedHintsLayout(Context context) {
        this(context, null);
    }

    protected DecoratedHintsLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    protected DecoratedHintsLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mShadowView = new ShadowView(context);
        mShadowView.setLayoutParams(new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        setupAnimation();
        addView(mShadowView);
    }

    protected void relayoutLastHint() {
        Assert.assertTrue("Don't supports many hints", mHints.size() <= 1);
        removeAllHintViews(false);
        for (Map.Entry<HintParams, View> entry : mHints.entrySet()) {
            HintParams hintParams = entry.getKey();
            View hint = entry.getValue();
            showHint(hint, hintParams);
        }
    }

    protected void removeHints(@Nullable Runnable postAction) {
        removeAllHintViews(true);
        mHints.clear();
        mShadowView.hide(postAction);
    }

    protected void changeTarget(@Nullable final View hint, final HintParams params) {
        if (params == null) {
            Assert.fail("Object params is null");
            return;
        }

        removeAllHintViews(true);
        mHints.clear();
        showHint(hint, params);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getMeasuredHeight() == 0 || getMeasuredWidth() == 0) {
            return;
        }

        Assert.assertTrue("Don't supports many hints", mHints.size() <= 1);
        for (Map.Entry<HintParams, View> entry : mHints.entrySet()) {
            HintParams hintParams = entry.getKey();
            View hint = entry.getValue();
            measureHint(hint, hintParams);
        }
    }

    private void showHint(@Nullable final View hint, final HintParams params){
        mShadowView.hide(new Runnable() {
            @Override
            public void run() {
                switch (params.getType()) {
                    case HintParams.Params.TARGET:
                        RectF rect = mShadowView.changeTarget(params.getTarget(), params.getShadowType());
                        params.setTargetCoordinates(rect);
                        addHintView(hint, getDefaultParams());
                        break;
                    case HintParams.Params.LAYOUT_PARAMS:
                        mShadowView.reset();
                        addHintView(hint, params.getLayoutParams());
                        break;
                }

                mHints.put(params, hint);
            }
        });
    }

    private void addHintView(@Nullable View hint, ViewGroup.LayoutParams params) {
        if (hint != null) {
            hint.setLayoutParams(params);
            addView(hint);
        }
    }

    private LayoutParams getDefaultParams() {
        Resources res = getResources();
        boolean isTablet = res.getBoolean(R.bool.is_tablet);
        int width = isTablet ? res.getDimensionPixelSize(R.dimen.hint_tablet_container_width)
                : res.getDimensionPixelSize(R.dimen.hint_container_width);
        return new FrameLayout.LayoutParams(width, LayoutParams.WRAP_CONTENT);
    }

    private void measureHint(View hint, HintParams hintParams) {
        if (hintParams.isType(HintParams.Params.LAYOUT_PARAMS) || hint == null) {
            return;
        }

        RectF coordinates = hintParams.getTargetCoordinates();
        if (coordinates == null) {
            Assert.fail("Object coordinates is null");
            return;
        }

        @SideLocation int sideByWidth;
        @SideLocation int sideByHeight;
        @SideLocation int resultLocation;
        float freeSpaceByWidth;
        float freeSpaceByHeight;

        float leftFreeSpace = coordinates.left;
        float rightFreeSpace = getMeasuredWidth() - coordinates.right;
        float topFreeSpace = coordinates.top;
        float bottomFreeSpace = getMeasuredHeight() - coordinates.bottom;

        if (leftFreeSpace > rightFreeSpace) {
            freeSpaceByWidth = leftFreeSpace;
            sideByWidth = SideLocation.LEFT;
        } else {
            freeSpaceByWidth = rightFreeSpace;
            sideByWidth = SideLocation.RIGHT;
        }
        if (topFreeSpace > bottomFreeSpace) {
            freeSpaceByHeight = topFreeSpace;
            sideByHeight = SideLocation.TOP;
        } else {
            freeSpaceByHeight = bottomFreeSpace;
            sideByHeight = SideLocation.BOTTOM;
        }
        if (freeSpaceByWidth > freeSpaceByHeight) {
            resultLocation = sideByWidth;
        } else {
            resultLocation = sideByHeight;
        }

        FrameLayout.LayoutParams params = (LayoutParams) hint.getLayoutParams();
        float hintWidth = hint.getMeasuredWidth();
        float hintHeight = hint.getMeasuredHeight();

        switch (resultLocation) {
            case SideLocation.LEFT:
                params.setMargins((int) (coordinates.left - hintWidth), (int) getMargin(coordinates.top,
                        coordinates.bottom, hintHeight, getMeasuredHeight()), 0, 0);
                break;
            case SideLocation.TOP:
                params.setMargins((int) getMargin(coordinates.left, coordinates.right, hintWidth,
                        getMeasuredWidth()), (int) (coordinates.top - hintHeight), 0, 0);
                break;
            case SideLocation.RIGHT:
                params.setMargins((int) coordinates.right, (int) getMargin(coordinates.top,
                        coordinates.bottom, hintHeight, getMeasuredHeight()), 0, 0);
                break;
            case SideLocation.BOTTOM:
                params.setMargins((int) getMargin(coordinates.left, coordinates.right, hintWidth,
                        getMeasuredWidth()), (int) coordinates.bottom, 0, 0);
                break;
        }

        if (hint instanceof HintShapeLocator) {
            ((HintShapeLocator) hint).setShapeLocation(coordinates, resultLocation);
        } else {
            Log.d(TAG, "Your hint view not implement interface HintShapeLocator");
        }
    }

    private void setupAnimation() {
        LayoutTransition transition = UIUtils.getAppearingLayoutTransition();
        transition.setDuration(LayoutTransition.APPEARING, AnimateSettings.getHintsShowAnimationDuration());
        transition.addTransitionListener(new LayoutTransition.TransitionListener() {
            @Override
            public void startTransition(LayoutTransition transition, ViewGroup container, View view,
                                        int transitionType) {
                mShadowView.show();
            }

            @Override
            public void endTransition(LayoutTransition transition, ViewGroup container, View view,
                                      int transitionType) {
            }
        });
        setLayoutTransition(transition);
    }

    private float getMargin(float belowPoint, float higherPoint, float hintLength, int screenLength) {
        float freeSpace = screenLength - belowPoint;
        if (freeSpace < hintLength) {
            belowPoint = higherPoint - hintLength;
            return Math.max(0, belowPoint);
        }

        float targetHeight = higherPoint - belowPoint;
        if (hintLength > targetHeight) {
            return belowPoint;
        }
        return higherPoint - (targetHeight / 2) - (hintLength / 2);
    }

    private void removeAllHintViews(boolean withAnimation) {
        for (Map.Entry<HintParams, View> entry : mHints.entrySet()) {
            if (withAnimation) {
                removeHintWithAnimation(entry.getValue());
            } else {
                removeView(entry.getValue());
            }
        }
    }

    private void removeHintWithAnimation(final View hint) {
        ValueAnimator animator = ObjectAnimator.ofFloat(hint, View.ALPHA, 1f, 0f);
        animator.setDuration(AnimateSettings.getHintsHideAnimationDuration());
        animator.addListener(new AnimatorUtils.SimpleAnimatorListener() {

            @Override
            public void onAnimationEnd(Animator animator) {
                removeView(hint);
            }
        });
        animator.start();
    }
}
