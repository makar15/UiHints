package codes.evolution.uihintslib.ui.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class BitmapUtils {

    public static Bitmap getRotateBitmap(Bitmap bitmap, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }
}
