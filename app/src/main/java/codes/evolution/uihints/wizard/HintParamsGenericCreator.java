package codes.evolution.uihints.wizard;

import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import codes.evolution.uihintslib.ui.HintParams;
import codes.evolution.uihintslib.ui.ShadowViewType;

public class HintParamsGenericCreator<ParamsType extends HintParams> {

    private final Class<ParamsType> mClass;

    public HintParamsGenericCreator(Class<ParamsType> cls) {
        mClass = cls;
    }

    public ParamsType get(View target,
                          @Nullable View.OnClickListener hintViewClickListener) throws ReflectiveOperationException {
        return mClass
                .getConstructor(View.class, View.OnClickListener.class)
                .newInstance(target, hintViewClickListener);
    }

    public ParamsType get(View target,
                          ShadowViewType type,
                          @Nullable View.OnClickListener hintViewClickListener) throws ReflectiveOperationException {
        return mClass
                .getConstructor(View.class, ShadowViewType.class, View.OnClickListener.class)
                .newInstance(target, type, hintViewClickListener);
    }

    public ParamsType get(@Nullable ViewGroup.LayoutParams params,
                          @Nullable View.OnClickListener hintViewClickListener) throws ReflectiveOperationException {
        return mClass
                .getConstructor(ViewGroup.LayoutParams.class, View.OnClickListener.class)
                .newInstance(params, hintViewClickListener);
    }

    public ParamsType get(@Nullable ViewGroup.LayoutParams params,
                          @Nullable View.OnClickListener hintViewClickListener,
                          int hintViewType) throws ReflectiveOperationException {
        return mClass
                .getConstructor(ViewGroup.LayoutParams.class, View.OnClickListener.class, int.class)
                .newInstance(params, hintViewClickListener, hintViewType);
    }
}
