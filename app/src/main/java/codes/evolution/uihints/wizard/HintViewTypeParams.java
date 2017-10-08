package codes.evolution.uihints.wizard;

import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import codes.evolution.uihintslib.ui.HintParams;
import codes.evolution.uihintslib.ui.ShadowViewType;

public class HintViewTypeParams extends HintParams {

    private static final int HINT_DEFAULT = HintViewType.STANDARD;

    private int mViewType = HINT_DEFAULT;

    public HintViewTypeParams(View target,
                              @Nullable View.OnClickListener hintViewClickListener) {
        super(target, hintViewClickListener);
    }

    public HintViewTypeParams(View target, ShadowViewType type,
                              @Nullable View.OnClickListener hintViewClickListener) {
        super(target, type, hintViewClickListener);
    }

    public HintViewTypeParams(@Nullable ViewGroup.LayoutParams params,
                              @Nullable View.OnClickListener hintViewClickListener) {
        super(params, hintViewClickListener);
    }

    public HintViewTypeParams(@Nullable ViewGroup.LayoutParams params,
                              @Nullable View.OnClickListener hintViewClickListener,
                              int hintViewType) {
        super(params, hintViewClickListener);
        mViewType = hintViewType;
    }

    public int getHintViewType() {
        return mViewType;
    }
}
