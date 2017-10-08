package codes.evolution.uihints.wizard;

import android.support.annotation.Nullable;
import android.view.View;

import codes.evolution.uihintslib.Hint;
import codes.evolution.uihintslib.HintsStorage;

public class WizardManager {

    private final HintsStorage<HintViewTypeParams> mHintsStorage;
    private final HintParamsBuilder mHintParamsBuilder;

    public WizardManager(HintsStorage<HintViewTypeParams> hintsStorage,
                         HintParamsBuilder hintParamsBuilder) {
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
