package codes.evolution.uihintslib;

public class AnimateSettings {
    public static int UI_HINTS_OVERLAY_DURATION_MSC = 0;

    private static int DEFAULT_SHOW_ANIMATION_DURATION = 300;
    private static int DEFAULT_HIDE_ANIMATION_DURATION = 300;

    public void setHintsShowAnimationDuration(int showAnimationDuration) {
        DEFAULT_SHOW_ANIMATION_DURATION = showAnimationDuration;
    }

    public void setHintsHideAnimationDuration(int hideAnimationDuration) {
        DEFAULT_HIDE_ANIMATION_DURATION = hideAnimationDuration;
    }

    public static int getHintsShowAnimationDuration() {
        return DEFAULT_SHOW_ANIMATION_DURATION;
    }

    public static int getHintsHideAnimationDuration() {
        return DEFAULT_HIDE_ANIMATION_DURATION;
    }
}
