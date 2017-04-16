package codes.evolution.uihintslib;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import codes.evolution.uihintslib.ui.HintContainerLayout;
import codes.evolution.uihintslib.ui.HintParams;
import codes.evolution.uihintslib.ui.utils.UIUtils;

public class HintsItemFactory {

    private static final String TITLE_PREF = "hint_title_";
    private static final String MESSAGE_PREF = "hint_message_";

    private final Context mContext;
    private final LayoutInflater mInflater;

    public HintsItemFactory(Context context) {
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public HintContainerLayout createHintView(View.OnClickListener listener, @Hint.HintName String hint,
                                              HintParams params) {
        HintContainerLayout viewHint = (HintContainerLayout) mInflater.inflate(R.layout.hint_view, null, false);
        ViewGroup hintContainer = (ViewGroup) viewHint.findViewById(R.id.hint_fields_layout);
        TextView title = (TextView) hintContainer.findViewById(R.id.title);
        setHintTextFromResources(title, TITLE_PREF + hint);
        TextView message = (TextView) hintContainer.findViewById(R.id.message);
        setHintTextFromResources(message, MESSAGE_PREF + hint);

        switch (params.getHintViewType()) {
            case HintViewType.SELECTION:
                initSelectionHint(hintContainer, listener);
                break;
            case HintViewType.STANDARD:
            default:
                initStandardHint(hintContainer, listener);
        }
        return viewHint;
    }

    private void initStandardHint(ViewGroup hintContainer, View.OnClickListener listener) {
        View viewStandard = mInflater.inflate(R.layout.standard_hint_layout, null);
        hintContainer.addView(viewStandard);
        TextView btnGotIt = (TextView) viewStandard.findViewById(R.id.got_it);
        btnGotIt.setOnClickListener(listener);
    }

    private void initSelectionHint(ViewGroup hintContainer, View.OnClickListener listener) {
        View viewSelection = mInflater.inflate(R.layout.selection_hint_layout, null);
        hintContainer.addView(viewSelection);
        TextView btnYes = (TextView) viewSelection.findViewById(R.id.yes);
        TextView btnNo = (TextView) viewSelection.findViewById(R.id.no);
        btnYes.setOnClickListener(listener);
        btnNo.setOnClickListener(listener);
    }

    private void setHintTextFromResources(TextView textView, String hint) {
        textView.setText(UIUtils.getStringResourceByName(mContext, hint));
    }

    public static FrameLayout.LayoutParams getCenterParams(Context context) {
        int screenWidth = UIUtils.getScreenWidth();
        int defaultWidth = context.getResources().getDimensionPixelSize(R.dimen.hint_container_width);
        boolean isTablet = context.getResources().getBoolean(R.bool.is_tablet);

        int width = isTablet ? (defaultWidth * 2) : screenWidth;
        return new FrameLayout.LayoutParams(width, FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
    }
}
