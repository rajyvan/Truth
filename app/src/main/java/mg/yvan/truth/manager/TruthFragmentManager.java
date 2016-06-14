package mg.yvan.truth.manager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import mg.yvan.truth.R;
import mg.yvan.truth.ui.fragment.BibleFragment;
import mg.yvan.truth.ui.fragment.CommentsFragment;
import mg.yvan.truth.ui.fragment.MyStatisticFragment;
import mg.yvan.truth.ui.fragment.MyVerseFragment;
import mg.yvan.truth.ui.fragment.SearchResultFragment;

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
            fm.executePendingTransactions();
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

    public static void displayMyVerse(AppCompatActivity activity) {
        clearBackStack(activity);
        changeFragment(activity, new MyVerseFragment(), R.id.fragment_container, false, null);
    }

    public static void displayComments(AppCompatActivity activity) {
        clearBackStack(activity);
        changeFragment(activity, new CommentsFragment(), R.id.fragment_container, false, null);
    }

    public static void displayMyStatitistic(AppCompatActivity activity) {
        clearBackStack(activity);
        changeFragment(activity, new MyStatisticFragment(), R.id.fragment_container, false, null);
    }

    public static void displaySearchResult(AppCompatActivity activity, String key) {
        if (!(getCurrentFragment(activity) instanceof SearchResultFragment)) {
            changeFragment(activity, SearchResultFragment.newInstance(key), R.id.fragment_container, true, null);
        }
    }

    /*public static void displaySearchResult(AppCompatActivity activity, String key) {
        if (!(getCurrentFragment(activity) instanceof SearchResultFragment)) {
            changeFragment(activity, SearchResultFragment.newInstance(key), R.id.fragment_container, true, null,
                    R.anim.translate_left_in,
                    R.anim.translate_left_out,
                    R.anim.translate_right_in,
                    R.anim.translate_right_out);
        }
    }*/

}
