package codes.evolution.uihintslib;

import android.support.annotation.IntDef;

@IntDef({HintViewType.STANDARD, HintViewType.SELECTION})
public @interface HintViewType {
    int STANDARD = 1;
    int SELECTION = 2;
}
