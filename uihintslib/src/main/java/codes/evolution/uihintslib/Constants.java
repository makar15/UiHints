package codes.evolution.uihintslib;

public class Constants {
    public static int UI_HINTS_OVERLAY_DURATION_MSC = 0;

    public static int HINTS_SHOW_ANIMATION_DURATION = 300;
    public static int HINTS_HIDE_ANIMATION_DURATION = 300;

    public void setHintsShowAnimationDuration(int newDuration) {
        HINTS_SHOW_ANIMATION_DURATION = newDuration;
    }

    public void setHintsHideAnimationDuration(int newDuration) {
        HINTS_HIDE_ANIMATION_DURATION = newDuration;
    }
}
