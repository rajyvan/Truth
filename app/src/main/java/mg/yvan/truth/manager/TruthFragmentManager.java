package mg.yvan.truth.manager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;

import mg.yvan.truth.R;
import mg.yvan.truth.models.Reference;
import mg.yvan.truth.models.Verse;
import mg.yvan.truth.ui.fragment.BibleFragment;
import mg.yvan.truth.ui.fragment.MyCommentsFragment;
import mg.yvan.truth.ui.fragment.EditCommentFragment;
import mg.yvan.truth.ui.fragment.CommunityFragment;
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

    private static void changeFragmentWithSharedElement(AppCompatActivity activity, Fragment toFragment, Fragment fromFragment, int containerResID, int transitionNameId, View sharedView) {

        Transition transition = TransitionInflater.from(activity).inflateTransition(R.transition.edit_comment_transition);

        toFragment.setSharedElementEnterTransition(transition);
        toFragment.setEnterTransition(new Fade());
        fromFragment.setExitTransition(new Fade());
        fromFragment.setSharedElementReturnTransition(transition);

        ViewCompat.setTransitionName(sharedView, activity.getResources().getString(transitionNameId));

        activity.getSupportFragmentManager()
                .beginTransaction()
                .addSharedElement(sharedView, activity.getResources().getString(transitionNameId))
                .replace(R.id.fragment_container, toFragment)
                .addToBackStack(null)
                .commit();
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
        changeFragment(activity, new MyCommentsFragment(), R.id.fragment_container, false, null);
    }

    public static void displayMyStatitistic(AppCompatActivity activity) {
        clearBackStack(activity);
        changeFragment(activity, new CommunityFragment(), R.id.fragment_container, false, null);
    }

    public static void displaySearchResult(AppCompatActivity activity, String key) {
        if (!(getCurrentFragment(activity) instanceof SearchResultFragment)) {
            changeFragment(activity, SearchResultFragment.newInstance(key), R.id.fragment_container, true, null);
        }
    }

    public static void displayEditComment(AppCompatActivity activity, Fragment fromFragment, int transitionNameId, View sharedView, Reference reference) {
        changeFragmentWithSharedElement(activity, EditCommentFragment.newInstance(reference), fromFragment, R.id.fragment_container, transitionNameId, sharedView);
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
