package codes.evolution.uihintslib.ui;

import android.support.annotation.IntDef;

@IntDef({SideLocation.LEFT, SideLocation.TOP, SideLocation.RIGHT, SideLocation.BOTTOM})
@interface SideLocation {
    int LEFT = 180;
    int TOP = 270;
    int RIGHT = 0;
    int BOTTOM = 90;
}
