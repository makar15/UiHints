package codes.evolution.uihints.di;

import android.support.v4.app.FragmentActivity;

import javax.inject.Singleton;

import codes.evolution.uihints.ui.UiFlowNavigator;
import codes.evolution.uihints.wizard.HintParamsBuilder;
import codes.evolution.uihints.wizard.HintParamsGenericCreator;
import codes.evolution.uihints.wizard.HintsViewFactoryImpl;
import codes.evolution.uihints.wizard.WizardManager;
import codes.evolution.uihintslib.HintsFlowController;
import codes.evolution.uihintslib.HintsStorage;
import codes.evolution.uihintslib.HintsStorageImpl;
import codes.evolution.uihintslib.ui.HintParams;
import codes.evolution.uihintslib.ui.HintsViewFactory;
import dagger.Module;
import dagger.Provides;

@Module
abstract class WizardModule<ParamsType extends HintParams> {

    private final FragmentActivity mActivity;

    WizardModule(FragmentActivity activity) {
        mActivity = activity;
    }

    @Provides
    @Singleton
    UiFlowNavigator provideUiFlowNavigator() {
        return new UiFlowNavigator(mActivity.getSupportFragmentManager());
    }

    @Provides
    @Singleton
    HintParamsBuilder<ParamsType> provideHintParamsBuilder(HintsFlowController<ParamsType> hintsFlowController) {
        return new HintParamsBuilder<>(mActivity, hintsFlowController, getHintParamsGenericCreator());
    }

    @Provides
    @Singleton
    WizardManager<ParamsType> provideWizardManager(HintsStorage<ParamsType> hintsStorage,
                                                   HintParamsBuilder<ParamsType> hintParamsBuilder) {
        return new WizardManager<>(hintsStorage, hintParamsBuilder);
    }

    @Provides
    @Singleton
    HintsStorage<ParamsType> provideHintsStorage() {
        return new HintsStorageImpl<>();
    }

    @Provides
    @Singleton
    HintsFlowController<ParamsType> provideHintsFlowController(HintsStorage<ParamsType> hintsStorage,
                                                               HintsViewFactory hintsViewFactory) {
        return new HintsFlowController<>(mActivity, hintsStorage, hintsViewFactory);
    }

    @Provides
    @Singleton
    HintsViewFactory provideHintsViewFactory() {
        return new HintsViewFactoryImpl(mActivity);
    }

    public abstract <P extends ParamsType> HintParamsGenericCreator<P> getHintParamsGenericCreator();
}
