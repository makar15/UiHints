package codes.evolution.uihintslib.ui;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import codes.evolution.uihintslib.AnimateSettings;
import codes.evolution.uihintslib.ui.utils.AnimatorUtils;
import codes.evolution.uihintslib.ui.utils.FullscreenDetector;
import codes.evolution.uihintslib.ui.utils.UIUtils;

public class ShadowView extends View {

    private static final int COUNT_STEPS = 24;
    private static final int ALPHA = 175;

    private final float mShadowRadius;
    private final Paint mMainPaint;
    private final Paint mGradientPaint;
    private final ValueAnimator mShadowAnimator;

    private final ValueAnimator.AnimatorUpdateListener mAnimationListener =
            new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int currentStep = (Integer) animation.getAnimatedValue();
                    if (currentStep != mNumStepsToDraw) {
                        mNumStepsToDraw = currentStep;
                        invalidate();
                    }
                }
            };

    private DrawType mDrawType = DrawType.HIDE;
    @Nullable private ShadowViewType mType;
    @Nullable private RectF mCoordinates;
    private int mNumStepsToDraw = COUNT_STEPS;

    private enum DrawType {

        SHOW, HIDE;

        public boolean isShow() {
            return this.equals(SHOW);
        }
    }

    protected ShadowView(Context context) {
        this(context, null);
    }

    protected ShadowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        mMainPaint = new Paint();
        mMainPaint.setColor(Color.argb(ALPHA, 0, 0, 0));

        mGradientPaint = new Paint();
        mGradientPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        mShadowRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16,
                context.getResources().getDisplayMetrics());

        mShadowAnimator = ObjectAnimator.ofInt(0, COUNT_STEPS);
        mShadowAnimator.addUpdateListener(mAnimationListener);
    }

    protected RectF changeTarget(View target, ShadowViewType type) {
        if (target == null) {
            return null;
        }
        mDrawType = DrawType.SHOW;
        int[] coordinates = new int[2];
        target.getLocationOnScreen(coordinates);

        mType = type;
        float statusBarHeight = getStatusBarHeight();

        mCoordinates = new RectF(
                coordinates[0] - mShadowRadius,
                coordinates[1] - mShadowRadius - statusBarHeight,
                coordinates[0] + target.getWidth() + mShadowRadius,
                coordinates[1] + target.getHeight() + mShadowRadius - statusBarHeight);
        invalidate();
        return mCoordinates;
    }

    protected void reset() {
        mCoordinates = null;
    }

    protected void show() {
        if (mShadowAnimator.isRunning()) {
            mShadowAnimator.cancel();
        }

        if (mDrawType.isShow()) {
            mShadowAnimator.setDuration(0);
        } else {
            mShadowAnimator.setDuration(AnimateSettings.getHintsShowAnimationDuration());
        }
        mDrawType = DrawType.SHOW;
        mShadowAnimator.start();
    }

    protected void hide(@Nullable final Runnable callback) {
        if (mShadowAnimator.isRunning()) {
            mShadowAnimator.cancel();
        }

        if (!mDrawType.isShow()) {
            mShadowAnimator.setDuration(0);
        } else {
            mShadowAnimator.setDuration(AnimateSettings.getHintsHideAnimationDuration());
        }
        mDrawType = DrawType.HIDE;
        mShadowAnimator.addListener(new AnimatorUtils.SimpleAnimatorListener() {
            @Override
            public void onAnimationCancel(Animator animator) {
                super.onAnimationCancel(animator);
                mShadowAnimator.removeListener(this);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (callback != null) {
                    callback.run();
                }
                mShadowAnimator.removeListener(this);
            }
        });
        mShadowAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPaint(mMainPaint);
        if (mCoordinates != null && mDrawType != null) {
            drawShape(canvas, mDrawType, mCoordinates, mType);
        }
    }

    private void drawShape(Canvas canvas, DrawType drawType, RectF coordinates, ShadowViewType type) {
        int alphaStep = ALPHA / COUNT_STEPS;
        float offsetStep = mShadowRadius / COUNT_STEPS;

        int alpha = ALPHA;
        float offset = 0;
        int countSteps;
        if (drawType.isShow()) {
            countSteps = mNumStepsToDraw;
        } else {
            countSteps = COUNT_STEPS - mNumStepsToDraw;
        }

        for (int step = 0; step < countSteps; step++) {
            alpha = alpha - alphaStep;
            offset = offset + offsetStep;
            mGradientPaint.setColor(Color.argb(alpha, 0, 0, 0));
            RectF rect = new RectF(
                    coordinates.left + offset,
                    coordinates.top + offset,
                    coordinates.right - offset,
                    coordinates.bottom - offset);

            if (type.isCircle()) {
                float yRadius = (coordinates.bottom - coordinates.top) / 2;
                float xRadius = (coordinates.right - coordinates.left) / 2;
                canvas.drawRoundRect(rect, xRadius, yRadius, mGradientPaint);
            } else {
                canvas.drawRect(rect, mGradientPaint);
            }
        }
    }

    private float getStatusBarHeight() {
        return FullscreenDetector.isFullscreen() ? 0 : UIUtils.getStatusBarHeight(getContext());
    }
}
