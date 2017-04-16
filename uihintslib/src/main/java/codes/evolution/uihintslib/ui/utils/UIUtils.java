package codes.evolution.uihintslib.ui.utils;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;

import junit.framework.Assert;

public class UIUtils {

    private static final int STATUS_BAR_DEFAULT_HEIGHT_DP = 35;
    private static final float DENSITY_FACTOR = 160f;

    public enum MeasureUnits {
        DP {
            public float toPx(float dp, Context context) {
                Resources resources = context.getResources();
                DisplayMetrics metrics = resources.getDisplayMetrics();
                return dp * (metrics.densityDpi / DENSITY_FACTOR);
            }
        },
        PX {
            public float toDp(float px, Context context) {
                Resources resources = context.getResources();
                DisplayMetrics metrics = resources.getDisplayMetrics();
                return px / (metrics.densityDpi / DENSITY_FACTOR);
            }
        };

        public float toPx(float dp, Context context) {
            throw new AbstractMethodError();
        }

        public float toDp(float dp, Context context) {
            throw new AbstractMethodError();
        }
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static float getStatusBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return context.getResources().getDimensionPixelSize(resourceId);
        }
        return MeasureUnits.DP.toPx(STATUS_BAR_DEFAULT_HEIGHT_DP, context);
    }

    @Nullable
    public static String getStringResourceByName(Context context, String name) {
        try {
            Resources res = context.getResources();
            int resId = res.getIdentifier(name, "string", context.getPackageName());
            return res.getString(resId);
        } catch (Resources.NotFoundException e) {
            Assert.fail("Not found resource String by name : " + name);
            return null;
        }
    }

    public static LayoutTransition getAppearingLayoutTransition() {
        LayoutTransition transition = new LayoutTransition();
        transition.enableTransitionType(LayoutTransition.APPEARING);
        transition.disableTransitionType(LayoutTransition.CHANGE_APPEARING);
        transition.disableTransitionType(LayoutTransition.CHANGE_DISAPPEARING);
        transition.disableTransitionType(LayoutTransition.DISAPPEARING);
        transition.disableTransitionType(LayoutTransition.CHANGING);
        return transition;
    }
}
