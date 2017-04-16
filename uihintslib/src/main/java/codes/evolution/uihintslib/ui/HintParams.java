package codes.evolution.uihintslib.ui;

import android.graphics.RectF;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import junit.framework.Assert;

import java.lang.ref.WeakReference;

import codes.evolution.uihintslib.HintViewType;

public class HintParams {

    private static final ShadowViewType SHADOW_DEFAULT = ShadowViewType.CIRCLE;
    private static final @HintViewType int HINT_DEFAULT = HintViewType.STANDARD;

    @IntDef({Params.TARGET, Params.LAYOUT_PARAMS})
    @interface Params {
        int TARGET = 1;
        int LAYOUT_PARAMS = 2;
    }

    private final @Params int mType;
    private final @HintViewType int mViewType;

    @Nullable private WeakReference<View> mTarget;
    @Nullable private ViewGroup.LayoutParams mLayoutParams;
    @Nullable private RectF mTargetCoordinates;
    private ShadowViewType mShadowType;

    public HintParams(View target) {
        this(target, SHADOW_DEFAULT, HINT_DEFAULT);
    }

    public HintParams(View target, @HintViewType int viewType) {
        this(target, SHADOW_DEFAULT, viewType);
    }

    public HintParams(View target, ShadowViewType type) {
        this(target, type, HINT_DEFAULT);
    }

    public HintParams(View target, ShadowViewType type, @HintViewType int viewType) {
        mTarget = new WeakReference<>(target);
        mShadowType = type;
        mViewType = viewType;
        mType = Params.TARGET;
    }

    public HintParams(ViewGroup.LayoutParams params) {
        this(params, HINT_DEFAULT);
    }

    public HintParams(@Nullable ViewGroup.LayoutParams params, @HintViewType int viewType) {
        mLayoutParams = params;
        mViewType = viewType;
        mType = Params.LAYOUT_PARAMS;
    }

    public int getHintViewType() {
        return mViewType;
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
