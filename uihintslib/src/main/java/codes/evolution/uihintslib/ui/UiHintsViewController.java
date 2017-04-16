package codes.evolution.uihintslib.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import codes.evolutiom.base.Assert;
import codes.evolution.uihintslib.Constants;
import codes.evolution.uihintslib.ui.utils.FullscreenDetector;

public class UiHintsViewController extends WindowOverlayView {

    private final FullscreenDetector mFullscreenDetector;
    private final FullscreenDetector.FullscreenStateListener mListener =
            new FullscreenDetector.FullscreenStateListener() {
                @Override
                public void onChanged() {
                    if (mDecoratedHintsLayout != null) {
                        mDecoratedHintsLayout.relayoutLastHint();
                    }
                }
            };

    @Nullable private DecoratedHintsLayout mDecoratedHintsLayout;

    public UiHintsViewController(Context context, FullscreenDetector fullscreenDetector) {
        super(context, Constants.UI_HINTS_OVERLAY_DURATION_MSC);
        mFullscreenDetector = fullscreenDetector;
    }

    public void removeTargets(@Nullable Runnable postAction) {
        Assert.assertNotNull("DecoratedHintsLayout is null Object", mDecoratedHintsLayout);
        if (mDecoratedHintsLayout != null) {
            mDecoratedHintsLayout.removeHints(postAction);
        }
    }

    public void changeTarget(@Nullable HintContainerLayout hint, HintParams params) {
        Assert.assertNotNull("DecoratedHintsLayout is null Object", mDecoratedHintsLayout);
        if (mDecoratedHintsLayout != null) {
            mDecoratedHintsLayout.changeTarget(hint, params);
        }
    }

    @Override
    protected View getView() {
        mDecoratedHintsLayout = new DecoratedHintsLayout(getContext());
        mDecoratedHintsLayout.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        return mDecoratedHintsLayout;
    }

    @Override
    protected void onShow() {
        mFullscreenDetector.addListener(mListener);
    }

    @Override
    protected void onHide() {
        mFullscreenDetector.removeListener(mListener);
    }
}
