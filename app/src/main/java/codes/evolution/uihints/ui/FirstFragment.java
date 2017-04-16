package codes.evolution.uihints.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import codes.evolution.uihints.Hints;
import codes.evolution.uihints.R;
import codes.evolution.uihints.di.AppInjector;
import codes.evolution.uihintslib.HintViewType;
import codes.evolution.uihintslib.HintsItemFactory;
import codes.evolution.uihintslib.HintsStorage;
import codes.evolution.uihintslib.ui.HintParams;
import codes.evolution.uihintslib.ui.ShadowViewType;

public class FirstFragment extends Fragment {

    @Inject HintsStorage mHintsStorage;
    @Inject UiFlowNavigator mUiFlowNavigator;

    @Bind(R.id.btn_open)
    Button mOpen;

    private final View.OnClickListener mOnOpenClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mUiFlowNavigator.openSecondFragment();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, null);
        ButterKnife.bind(this, view);
        AppInjector.get().inject(this);

        mOpen.setOnClickListener(mOnOpenClickListener);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mHintsStorage.addHint(Hints.START_FLOW,
                new HintParams(HintsItemFactory.getCenterParams(getContext()), HintViewType.SELECTION));
        mHintsStorage.addHint(Hints.START_SECOND_WINDOW, mOpen, ShadowViewType.RECTANGLE);
    }

    @Override
    public void onStop() {
        mHintsStorage.removeHint(Hints.START_SECOND_WINDOW);
        mHintsStorage.removeHint(Hints.START_FLOW);
        super.onStop();
    }
}
