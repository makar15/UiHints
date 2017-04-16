package codes.evolution.uihintslib.ui.utils;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

public class FullscreenDetector {

    private static boolean sIsFullscreen;

    private final WindowManager mWindowManager;
    private final WindowManager.LayoutParams mParams;
    private final View mHelperView;
    private final ViewTreeObserver.OnGlobalLayoutListener mLayoutListener;
    private final List<FullscreenStateListener> mListeners = new ArrayList<>();

    public interface FullscreenStateListener {
        void onChanged();
    }

    public FullscreenDetector(Context context, final DisplayInfo displayInfo) {
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mParams = new WindowManager.LayoutParams();
        mParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
        mParams.gravity = Gravity.RIGHT | Gravity.TOP;
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mParams.width = 1;
        mParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        mParams.format = PixelFormat.TRANSPARENT;

        mHelperView = new View(context);
        mLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                boolean isFullscreen = displayInfo.getHeight() <= mHelperView.getHeight();
                if (isFullscreen != sIsFullscreen) {
                    sIsFullscreen = isFullscreen;
                    notifyListeners();
                }
            }
        };
    }

    public void addListener(FullscreenStateListener listener) {
        mListeners.add(listener);
    }

    public void removeListener(FullscreenStateListener listener) {
        mListeners.remove(listener);
    }

    private void notifyListeners() {
        for (FullscreenStateListener listener : mListeners) {
            listener.onChanged();
        }
    }

    public void onCreate() {
        mWindowManager.addView(mHelperView, mParams);
        ViewTreeObserver treeObserver = mHelperView.getViewTreeObserver();
        treeObserver.addOnGlobalLayoutListener(mLayoutListener);
    }

    public void onDestroy() {
        mWindowManager.removeView(mHelperView);
        ViewTreeObserver treeObserver = mHelperView.getViewTreeObserver();
        treeObserver.removeOnGlobalLayoutListener(mLayoutListener);
    }

    public static boolean isFullscreen() {
        return sIsFullscreen;
    }
}
