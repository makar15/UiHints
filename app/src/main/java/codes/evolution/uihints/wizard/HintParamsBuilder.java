package codes.evolution.uihints.wizard;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import codes.evolution.uihints.R;
import codes.evolution.uihintslib.Hint;
import codes.evolution.uihintslib.HintsFlowController;
import codes.evolution.uihintslib.ui.HintParams;
import codes.evolution.uihintslib.ui.ShadowViewType;

public class HintParamsBuilder<ParamsType extends HintParams> {

    private static final String TAG = "HintParamsBuilder";

    private final Context mContext;
    private final HintsFlowController<ParamsType> mHintsFlowController;
    private final HintParamsGenericCreator<ParamsType> mHintParamsGenericCreator;
    private final View.OnClickListener mHintViewClickListener = new HintViewClickListener();

    public HintParamsBuilder(Context context,
                             HintsFlowController<ParamsType> hintsFlowController,
                             HintParamsGenericCreator<ParamsType> hintParamsGenericCreator) {
        mContext = context;
        mHintsFlowController = hintsFlowController;
        mHintParamsGenericCreator = hintParamsGenericCreator;
    }

    @Nullable
    public ParamsType get(@Hint.Name String hintName, @Nullable View target) {
        ParamsType params = null;

        try {
            switch (hintName) {
                case Hints.START_FLOW:
                    params = mHintParamsGenericCreator.get(
                            HintsViewFactoryImpl.getCenterParams(mContext),
                            mHintViewClickListener,
                            HintViewType.SELECTION);
                    break;
                case Hints.FINISH_FLOW:
                    params = mHintParamsGenericCreator.get(
                            HintsViewFactoryImpl.getCenterParams(mContext),
                            mHintViewClickListener);
                    break;
                case Hints.START_SECOND_WINDOW:
                case Hints.SHOW_ADDITIONAL:
                case Hints.FIRST_HINT:
                    params = mHintParamsGenericCreator.get(
                            target,
                            ShadowViewType.RECTANGLE,
                            mHintViewClickListener);
                    break;
                case Hints.TEST_TEXT:
                    params = mHintParamsGenericCreator.get(
                            target,
                            mHintViewClickListener);
                    break;
            }
        } catch (ReflectiveOperationException e) {
            Log.e(TAG, "Object ParamsType type failed to create. The type is not valid and doesn't " +
                    "have a constructor with the requested parameters", e);
        }
        return params;
    }

    private class HintViewClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.yes:
                case R.id.got_it:
                    mHintsFlowController.showNextHint();
                    break;
                case R.id.no:
                    mHintsFlowController.destroy();
                    break;
                default:
                    Log.e(TAG, "View with" + view.getId() + " dosn't exist");
            }
        }
    }
}
