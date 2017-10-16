package codes.evolution.uihints.wizard;

import android.support.annotation.Nullable;
import android.view.View;

import codes.evolution.uihintslib.Hint;
import codes.evolution.uihintslib.HintsStorage;
import codes.evolution.uihintslib.ui.HintParams;

public class WizardManager<ParamsType extends HintParams> {

    private final HintsStorage<ParamsType> mHintsStorage;
    private final HintParamsBuilder<ParamsType> mHintParamsBuilder;

    public WizardManager(HintsStorage<ParamsType> hintsStorage,
                         HintParamsBuilder<ParamsType> hintParamsBuilder) {
        mHintsStorage = hintsStorage;
        mHintParamsBuilder = hintParamsBuilder;
    }

    public void onReadyForShow(@Hint.Name String hintName) {
        onReadyForShow(hintName, null);
    }

    public void onReadyForShow(@Hint.Name String hintName, @Nullable View target) {
        mHintsStorage.addHint(hintName, mHintParamsBuilder.get(hintName, target));
    }

    public void onRemoveFromReadyForShow(@Hint.Name String hintName) {
        mHintsStorage.removeHint(hintName);
    }
}
