package codes.evolution.uihints.di;

import android.support.v4.app.FragmentActivity;

import codes.evolution.uihints.wizard.HintParamsGenericCreator;
import codes.evolution.uihints.wizard.HintViewTypeParams;
import dagger.Module;

@Module
public class WizardTypeParamsModule extends WizardModule<HintViewTypeParams> {

    WizardTypeParamsModule(FragmentActivity activity) {
        super(activity);
    }

    @Override
    public HintParamsGenericCreator<HintViewTypeParams> getHintParamsGenericCreator() {
        return new HintParamsGenericCreator(HintViewTypeParams.class);
    }
}
