package codes.evolution.uihints.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import codes.evolution.uihints.Hints;
import codes.evolution.uihints.R;
import codes.evolution.uihints.di.AppInjector;
import codes.evolution.uihintslib.HintsItemFactory;
import codes.evolution.uihintslib.HintsStorage;
import codes.evolution.uihintslib.ui.HintParams;
import codes.evolution.uihintslib.ui.ShadowViewType;

public class SecondFragment extends Fragment {

    @Inject HintsStorage mHintsStorage;

    @Bind(R.id.btn_first_hint)
    Button mFirstHint;
    @Bind(R.id.btn_show_additional_hint)
    Button mShowAddHint;
    @Bind(R.id.tv_last_hint)
    TextView mLastHint;

    private final View.OnClickListener mOnShowAddHintClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mHintsStorage.addHint(Hints.TEST_TEXT, mLastHint);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_second, null);
        ButterKnife.bind(this, view);
        AppInjector.get().inject(this);

        mShowAddHint.setOnClickListener(mOnShowAddHintClickListener);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mHintsStorage.addHint(Hints.FINISH_FLOW,
                new HintParams(HintsItemFactory.getCenterParams(getContext())));
        mHintsStorage.addHint(Hints.SHOW_ADDITIONAL, mShowAddHint, ShadowViewType.RECTANGLE);
        mHintsStorage.addHint(Hints.FIRST_HINT, mFirstHint, ShadowViewType.RECTANGLE);
    }

    @Override
    public void onStop() {
        mHintsStorage.removeHint(Hints.FIRST_HINT);
        mHintsStorage.removeHint(Hints.SHOW_ADDITIONAL);
        mHintsStorage.removeHint(Hints.TEST_TEXT);
        mHintsStorage.removeHint(Hints.FINISH_FLOW);
        super.onStop();
    }
}
