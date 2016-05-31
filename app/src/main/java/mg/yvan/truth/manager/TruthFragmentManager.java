package mg.yvan.truth.manager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import mg.yvan.truth.R;
import mg.yvan.truth.ui.fragment.BibleFragment;

/**
 * Created by Yvan on 26/10/15.
 */
public class TruthFragmentManager {

    private static void changeFragment(AppCompatActivity activity, Fragment fragment, int containerResID, boolean addToBackStack, String tag) {
        changeFragment(activity, fragment, containerResID, addToBackStack, tag, 0, 0, 0, 0);
    }

    private static void changeFragment(AppCompatActivity activity, Fragment fragment, int containerResID, boolean addToBackStack, String tag, int enter, int exit, int popEnter, int popExit) {
        if (activity != null && !activity.isFinishing()) {
            FragmentManager fm = activity.getSupportFragmentManager();

            final FragmentTransaction transaction = fm.beginTransaction();

            if (enter > 0 && exit > 0 && popEnter > 0 && popExit > 0) {
                transaction.setCustomAnimations(enter, exit, popEnter, popExit);
            }

            transaction.replace(containerResID, fragment, tag);
            if (addToBackStack) {
                transaction.addToBackStack(null);
            } else {
                clearBackStack(activity);
            }
            transaction.commit();
        }
    }

    public static Fragment getCurrentFragment(AppCompatActivity activity) {
        return activity.getSupportFragmentManager().findFragmentById(R.id.fragment_container);
    }

    private static void clearBackStack(AppCompatActivity activity) {
        FragmentManager manager = activity.getSupportFragmentManager();
        for (int i = 0; i < manager.getBackStackEntryCount(); i++) {
            manager.popBackStack();
        }
    }

    public static void displayBible(AppCompatActivity activity) {
        clearBackStack(activity);
        changeFragment(activity, new BibleFragment(), R.id.fragment_container, false, null);
    }

    /*public static void displaySettings(AppCompatActivity activity) {
        if (!(getCurrentFragment(activity) instanceof SettingsFragment)) {
            changeFragment(activity, new SettingsFragment(), R.id.fragment_container, true, null,
                    R.anim.translate_top_in,
                    R.anim.translate_top_out,
                    R.anim.translate_bottom_in,
                    R.anim.translate_bottom_out);
        }
    }*/

    /*public static void displayBonusDetail(AppCompatActivity activity, Bonus bonus) {
        changeFragment(activity, BonusDetailFragment.newInstance(bonus), R.id.fragment_container, true, null,
                R.anim.translate_left_in,
                R.anim.translate_left_out,
                R.anim.translate_right_in,
                R.anim.translate_right_out);
    }*/

}
