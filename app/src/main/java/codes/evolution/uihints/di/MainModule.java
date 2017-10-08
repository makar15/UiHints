package codes.evolution.uihints.di;

import android.support.v4.app.FragmentActivity;

import javax.inject.Singleton;

import codes.evolution.uihints.ui.UiFlowNavigator;
import codes.evolution.uihints.wizard.HintParamsBuilder;
import codes.evolution.uihints.wizard.HintViewTypeParams;
import codes.evolution.uihints.wizard.HintsViewFactoryImpl;
import codes.evolution.uihints.wizard.WizardManager;
import codes.evolution.uihintslib.HintsFlowController;
import codes.evolution.uihintslib.HintsStorage;
import codes.evolution.uihintslib.HintsStorageImpl;
import codes.evolution.uihintslib.ui.HintsViewFactory;
import dagger.Module;
import dagger.Provides;

@Module
class MainModule {

    private final FragmentActivity mActivity;

    MainModule(FragmentActivity activity) {
        mActivity = activity;
    }

    @Provides
    @Singleton
    UiFlowNavigator provideUiFlowNavigator() {
        return new UiFlowNavigator(mActivity.getSupportFragmentManager());
    }

    @Provides
    @Singleton
    HintsViewFactory<HintViewTypeParams> provideHintsViewFactory() {
        return new HintsViewFactoryImpl(mActivity);
    }

    @Provides
    @Singleton
    HintParamsBuilder provideHintParamsBuilder(HintsFlowController<HintViewTypeParams> hintsFlowController) {
        return new HintParamsBuilder(mActivity, hintsFlowController);
    }

    @Provides
    @Singleton
    WizardManager provideWizardManager(HintsStorage<HintViewTypeParams> hintsStorage,
                                       HintParamsBuilder hintParamsBuilder) {
        return new WizardManager(hintsStorage, hintParamsBuilder);
    }

    @Provides
    @Singleton
    HintsStorage<HintViewTypeParams> provideHintsStorage() {
        return new HintsStorageImpl<>();
    }

    @Provides
    @Singleton
    HintsFlowController<HintViewTypeParams> provideHintsFlowController(HintsStorage<HintViewTypeParams> hintsStorage,
                                                                       HintsViewFactory<HintViewTypeParams> hintsViewFactory) {
        return new HintsFlowController<>(mActivity, hintsStorage, hintsViewFactory);
    }
}
