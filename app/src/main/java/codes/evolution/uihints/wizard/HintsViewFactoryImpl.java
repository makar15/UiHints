package codes.evolution.uihints.wizard;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import codes.evolution.uihints.R;
import codes.evolution.uihints.wizard.ui.HintContainerLayout;
import codes.evolution.uihintslib.Hint;
import codes.evolution.uihintslib.ui.HintsViewFactory;
import codes.evolution.uihintslib.ui.utils.UIUtils;

public class HintsViewFactoryImpl implements HintsViewFactory<HintViewTypeParams> {

    private static final String TITLE_PREF = "hint_title_";
    private static final String MESSAGE_PREF = "hint_message_";

    private final Context mContext;
    private final LayoutInflater mInflater;

    public HintsViewFactoryImpl(Context context) {
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View createHintView(@Hint.Name String hintName,
                               HintViewTypeParams params) {
        HintContainerLayout hintView = (HintContainerLayout) mInflater.inflate(R.layout.hint_view, null, false);
        ViewGroup hintContainer = (ViewGroup) hintView.findViewById(R.id.hint_fields_layout);
        TextView title = (TextView) hintContainer.findViewById(R.id.title);
        setHintTextFromResources(title, TITLE_PREF + hintName);
        TextView message = (TextView) hintContainer.findViewById(R.id.message);
        setHintTextFromResources(message, MESSAGE_PREF + hintName);

        switch (params.getHintViewType()) {
            case HintViewType.SELECTION:
                initSelectionHintView(hintContainer, params.getHintViewListener());
                break;
            case HintViewType.STANDARD:
            default: initStandardHintView(hintContainer, params.getHintViewListener());
        }
        return hintView;
    }

    private void initStandardHintView(ViewGroup hintContainer,
                                      View.OnClickListener listener) {
        View viewStandard = addViewToContainer(R.layout.standard_hint_layout, hintContainer);
        TextView btnGotIt = (TextView) viewStandard.findViewById(R.id.got_it);
        btnGotIt.setOnClickListener(listener);
    }

    private void initSelectionHintView(ViewGroup hintContainer,
                                       View.OnClickListener listener) {
        View viewSelection = addViewToContainer(R.layout.selection_hint_layout, hintContainer);
        TextView btnYes = (TextView) viewSelection.findViewById(R.id.yes);
        TextView btnNo = (TextView) viewSelection.findViewById(R.id.no);
        btnYes.setOnClickListener(listener);
        btnNo.setOnClickListener(listener);
    }

    private View addViewToContainer(int resource, ViewGroup hintContainer) {
        View view = mInflater.inflate(resource, null);
        hintContainer.addView(view);
        return view;
    }

    private void setHintTextFromResources(TextView textView, String hint) {
        textView.setText(UIUtils.getStringResourceByName(mContext, hint));
    }

    public static FrameLayout.LayoutParams getCenterParams(Context context) {
        int screenWidth = UIUtils.getScreenWidth();
        int defaultWidth = context.getResources().getDimensionPixelSize(R.dimen.hint_container_width);
        boolean isTablet = context.getResources().getBoolean(codes.evolution.uihintslib.R.bool.is_tablet);

        int width = isTablet ? (defaultWidth * 2) : screenWidth;
        return new FrameLayout.LayoutParams(width, FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
    }
}
