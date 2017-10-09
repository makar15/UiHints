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
import codes.evolution.uihints.R;
import codes.evolution.uihints.di.AppInjector;
import codes.evolution.uihints.wizard.HintViewTypeParams;
import codes.evolution.uihints.wizard.Hints;
import codes.evolution.uihints.wizard.WizardManager;

public class SecondFragment extends Fragment {

    @Inject WizardManager<HintViewTypeParams> mWizardManager;

    @Bind(R.id.btn_first_hint)
    Button mFirstHint;
    @Bind(R.id.btn_show_additional_hint)
    Button mShowAddHint;
    @Bind(R.id.tv_last_hint)
    TextView mLastHint;

    private final View.OnClickListener mOnShowAddHintClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mWizardManager.onReadyForShow(Hints.TEST_TEXT, mLastHint);
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
        mWizardManager.onReadyForShow(Hints.FINISH_FLOW);
        mWizardManager.onReadyForShow(Hints.SHOW_ADDITIONAL, mShowAddHint);
        mWizardManager.onReadyForShow(Hints.FIRST_HINT, mFirstHint);
    }

    @Override
    public void onStop() {
        mWizardManager.onRemoveFromReadyForShow(Hints.FIRST_HINT);
        mWizardManager.onRemoveFromReadyForShow(Hints.SHOW_ADDITIONAL);
        mWizardManager.onRemoveFromReadyForShow(Hints.TEST_TEXT);
        mWizardManager.onRemoveFromReadyForShow(Hints.FINISH_FLOW);
        super.onStop();
    }
}
