package codes.evolution.uihintslib.ui;

import android.graphics.RectF;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import junit.framework.Assert;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;

public class HintParams {

    private static final ShadowViewType SHADOW_DEFAULT = ShadowViewType.CIRCLE;

    @IntDef({Params.TARGET, Params.LAYOUT_PARAMS})
    @Retention(RetentionPolicy.SOURCE)
    @interface Params {
        int TARGET = 1;
        int LAYOUT_PARAMS = 2;
    }

    private final @Params int mType;

    @Nullable private WeakReference<View> mTarget;
    @Nullable private ViewGroup.LayoutParams mLayoutParams;
    @Nullable private RectF mTargetCoordinates;
    @Nullable private View.OnClickListener mHintViewClickListener;
    private ShadowViewType mShadowType;

    public HintParams(View target,
                      @Nullable View.OnClickListener hintViewClickListener) {
        this(target, SHADOW_DEFAULT, hintViewClickListener);
    }

    public HintParams(View target,
                      ShadowViewType type,
                      @Nullable View.OnClickListener hintViewClickListener) {
        mTarget = new WeakReference<>(target);
        mShadowType = type;
        mHintViewClickListener = hintViewClickListener;

        mType = Params.TARGET;
    }

    public HintParams(@Nullable ViewGroup.LayoutParams params,
                      @Nullable View.OnClickListener hintViewClickListener) {
        mLayoutParams = params;
        mHintViewClickListener = hintViewClickListener;

        mType = Params.LAYOUT_PARAMS;
    }

    public ShadowViewType getShadowType() {
        return mShadowType;
    }

    @Nullable
    public View getTarget() {
        if (mTarget == null) {
            return null;
        }
        View target = mTarget.get();
        if (target == null) {
            Assert.fail("WeakReference target leaked");
            return null;
        }
        return target;
    }

    @Nullable
    public ViewGroup.LayoutParams getLayoutParams() {
        return mLayoutParams;
    }

    @Nullable
    public View.OnClickListener getHintViewListener() {
        return mHintViewClickListener;
    }

    void setTargetCoordinates(RectF targetCoordinates) {
        mTargetCoordinates = targetCoordinates;
    }

    @Nullable
    RectF getTargetCoordinates() {
        return mTargetCoordinates;
    }

    int getType() {
        return mType;
    }

    boolean isType(@Params int type) {
        return mType == type;
    }
}
