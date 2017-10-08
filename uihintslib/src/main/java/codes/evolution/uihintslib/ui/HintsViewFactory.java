package codes.evolution.uihintslib.ui;

import android.view.View;

import codes.evolution.uihintslib.Hint;

public interface HintsViewFactory<ParamsType extends HintParams> {
    View createHintView(@Hint.Name String hintName, ParamsType params);
}
