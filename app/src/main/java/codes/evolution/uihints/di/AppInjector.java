package codes.evolution.uihints.di;

import android.support.v4.app.FragmentActivity;

public class AppInjector {

    private static AppComponent mAppComponent;

    public static void buildComponent(FragmentActivity activity) {
        mAppComponent = DaggerAppComponent.builder()
                .mainModule(new MainModule(activity))
                .build();
    }

    public static AppComponent get() {
        return mAppComponent;
    }
}
