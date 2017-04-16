package codes.evolution.uihints.di;

import javax.inject.Singleton;

import codes.evolution.uihints.ui.ExampleActivity;
import codes.evolution.uihints.ui.FirstFragment;
import codes.evolution.uihints.ui.SecondFragment;
import dagger.Component;

@Singleton
@Component(modules = {MainModule.class})
public interface AppComponent {

    void inject(ExampleActivity exampleActivity);
    void inject(FirstFragment firstFragment);
    void inject(SecondFragment secondFragment);
}
