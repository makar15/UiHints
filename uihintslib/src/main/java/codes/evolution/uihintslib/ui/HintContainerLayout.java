package codes.evolution.uihintslib.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;

import codes.evolution.uihintslib.R;
import codes.evolution.uihintslib.ui.utils.BitmapUtils;

public class HintContainerLayout extends FrameLayout {

    private static final String TAG = "HintContainerLayout";
    private static final int STATE_EMPTY_TARGET = -1;

    private final FrameLayout.LayoutParams mParams =
            new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    private final Bitmap mTriangleBitmap;
    private final ImageView mTriangleView;
    private final float mTriangleWidth;
    private final float mTriangleHeight;

    private float mLeftPointTriangle;
    private float mTopPointTriangle;
    private float mAngle = STATE_EMPTY_TARGET;

    public HintContainerLayout(Context context) {
        this(context, null);
    }

    public HintContainerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HintContainerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // TODO set method for : TriangleBitmap and color RectangleDrawable
        mTriangleBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.wizard_triangle);
        mTriangleWidth = mTriangleBitmap.getWidth();
        mTriangleHeight = mTriangleBitmap.getHeight();
        mTriangleView = new ImageView(getContext());
        mTriangleView.setLayoutParams(mParams);
        addView(mTriangleView);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mAngle == STATE_EMPTY_TARGET) {
            Log.w(TAG, "You haven't target");
            return;
        }

        mTriangleView.setImageBitmap(BitmapUtils.getRotateBitmap(mTriangleBitmap, mAngle));
        mParams.setMargins((int) mLeftPointTriangle, (int) mTopPointTriangle, 0, 0);
    }

    protected void setTriangleLocation(RectF coordinatesTarget, @SideLocation int locationHint) {
        if (coordinatesTarget.isEmpty()) {
            return;
        }
        FrameLayout.LayoutParams params = (LayoutParams) getLayoutParams();
        float width = getMeasuredWidth();
        float height = getMeasuredHeight();
        float x;
        float y;

        switch (locationHint) {
            case SideLocation.LEFT:
                y = getCenterTarget(coordinatesTarget.top, coordinatesTarget.bottom);
                mTopPointTriangle = y - params.topMargin - (mTriangleHeight / 2);
                mLeftPointTriangle = width - mTriangleWidth;
                break;
            case SideLocation.TOP:
                mTopPointTriangle = height - mTriangleWidth;
                x = getCenterTarget(coordinatesTarget.left, coordinatesTarget.right);
                mLeftPointTriangle = x - params.leftMargin - (mTriangleHeight / 2);
                break;
            case SideLocation.RIGHT:
                y = getCenterTarget(coordinatesTarget.top, coordinatesTarget.bottom);
                mTopPointTriangle = y - params.topMargin - (mTriangleHeight / 2);
                mLeftPointTriangle = 0;
                break;
            case SideLocation.BOTTOM:
                mTopPointTriangle = 0;
                x = getCenterTarget(coordinatesTarget.left, coordinatesTarget.right);
                mLeftPointTriangle = x - params.leftMargin - (mTriangleHeight / 2);
                break;
        }
        mAngle = locationHint;
        Log.d(TAG, "triangle rotate angle: " + mAngle + "degrees");
    }

    private float getCenterTarget(float firstPoint, float secondPoint) {
        return secondPoint - ((secondPoint - firstPoint) / 2);
    }
}
