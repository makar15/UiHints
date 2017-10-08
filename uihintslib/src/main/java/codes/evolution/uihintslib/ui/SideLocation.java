package codes.evolution.uihintslib.ui;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({SideLocation.LEFT, SideLocation.TOP, SideLocation.RIGHT, SideLocation.BOTTOM})
@Retention(RetentionPolicy.SOURCE)
public @interface SideLocation {
    int LEFT = 180;
    int TOP = 270;
    int RIGHT = 0;
    int BOTTOM = 90;
}
