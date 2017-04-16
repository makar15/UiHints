package codes.evolution.uihints.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import codes.evolution.uihints.Hints;
import codes.evolution.uihints.R;
import codes.evolution.uihints.di.AppInjector;
import codes.evolution.uihintslib.Flow;
import codes.evolution.uihintslib.HintsFlowController;

public class ExampleActivity extends AppCompatActivity {

    @Inject HintsFlowController mHintsFlow;
    @Inject UiFlowNavigator mUiFlowNavigator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);
        AppInjector.buildComponent(this);
        AppInjector.get().inject(this);

        Flow flow = new Flow(Hints.getAllHints());
        mHintsFlow.onCreate(flow);

        mUiFlowNavigator.openFirstFragment();
    }

    @Override
    public void onDestroy() {
        mHintsFlow.onDestroy();
        super.onDestroy();
    }
}
