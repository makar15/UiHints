package codes.evolution.uihints.di;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import javax.inject.Singleton;

import codes.evolution.uihints.ui.UiFlowNavigator;
import codes.evolution.uihintslib.HintsFlowController;
import codes.evolution.uihintslib.HintsStorage;
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
    Context provideContext() {
        return mActivity;
    }

    @Provides
    @Singleton
    HintsFlowController provideHintsFlowController(HintsStorage hintsStorage) {
        return new HintsFlowController(mActivity, hintsStorage);
    }

    @Provides
    @Singleton
    HintsStorage provideHintsStorage() {
        return new HintsStorage();
    }

    @Provides
    @Singleton
    UiFlowNavigator provideUiFlowNavigator() {
        return new UiFlowNavigator(mActivity.getSupportFragmentManager());
    }
}
