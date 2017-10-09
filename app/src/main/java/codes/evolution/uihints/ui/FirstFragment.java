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
import codes.evolution.uihints.R;
import codes.evolution.uihints.di.AppInjector;
import codes.evolution.uihints.wizard.HintViewTypeParams;
import codes.evolution.uihints.wizard.Hints;
import codes.evolution.uihints.wizard.WizardManager;

public class FirstFragment extends Fragment {

    @Inject UiFlowNavigator mUiFlowNavigator;
    @Inject WizardManager<HintViewTypeParams> mWizardManager;

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
        mWizardManager.onReadyForShow(Hints.START_FLOW);
        mWizardManager.onReadyForShow(Hints.START_SECOND_WINDOW, mOpen);
    }

    @Override
    public void onStop() {
        mWizardManager.onRemoveFromReadyForShow(Hints.START_SECOND_WINDOW);
        mWizardManager.onRemoveFromReadyForShow(Hints.START_FLOW);
        super.onStop();
    }
}
