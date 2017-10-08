package codes.evolution.uihints.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import codes.evolution.uihints.R;

public class UiFlowNavigator {

    private static final String FRAGMENTS_BACK_STACK_KEY = "fragments_back_stack_key";

    private final FragmentManager mFragmentManager;

    public UiFlowNavigator(FragmentManager manager) {
        mFragmentManager = manager;
    }

    void openFirstFragment() {
        openFragment(new FirstFragment(), false);
    }

    void openSecondFragment() {
        openFragment(new SecondFragment(), true);
    }

    private void openFragment(Fragment fragment, boolean saveInBackStack) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        if (saveInBackStack) {
            transaction.addToBackStack(FRAGMENTS_BACK_STACK_KEY);
        }
        transaction.commit();
    }
}
