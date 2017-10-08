package codes.evolution.uihints.wizard;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({HintViewType.STANDARD, HintViewType.SELECTION})
@Retention(RetentionPolicy.SOURCE)
public @interface HintViewType {
    int STANDARD = 1;
    int SELECTION = 2;
}
