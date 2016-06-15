package mg.yvan.truth.ui.view;

import android.animation.ValueAnimator;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.animation.DecelerateInterpolator;

import mg.yvan.truth.ui.activity.MainActivity;

/**
 * Created by Yvan on 15/10/15.
 */
public class CustomActionBarDrawerToggle extends ActionBarDrawerToggle {
    private static final float MENU_POSITION = 0f;
    private static final float ARROW_POSITION = 1.0f;

    private final int animationLength;
    private final DrawerLayout drawerLayout;
    private final MainActivity activity;
    private State currentState;

    public CustomActionBarDrawerToggle(MainActivity activity, DrawerLayout drawerLayout, int openDrawerContentDescriptionResource, int closeDrawerContentDescriptionResource) {
        super(activity, drawerLayout, openDrawerContentDescriptionResource, closeDrawerContentDescriptionResource);
        animationLength = activity.getResources().getInteger(android.R.integer.config_shortAnimTime);
        this.drawerLayout = drawerLayout;
        this.activity = activity;
        currentState = State.MENU;
    }

    public void animateToBackArrow() {
        ValueAnimator anim = ValueAnimator.ofFloat(MENU_POSITION, ARROW_POSITION);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float slideOffset = (Float) valueAnimator.getAnimatedValue();
                onDrawerSlide(drawerLayout, slideOffset);
            }
        });
        anim.setInterpolator(new DecelerateInterpolator());
        anim.setDuration(animationLength);
        anim.start();
        currentState = State.UP_ARROW;
    }

    public void animateToMenu() {
        ValueAnimator anim = ValueAnimator.ofFloat(ARROW_POSITION, MENU_POSITION);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float slideOffset = (Float) valueAnimator.getAnimatedValue();
                onDrawerSlide(drawerLayout, slideOffset);
            }
        });
        anim.setInterpolator(new DecelerateInterpolator());
        anim.setDuration(animationLength);
        anim.start();
        currentState = State.MENU;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (android.R.id.home):
                if (currentState == State.UP_ARROW) {
                    activity.onBackPressed();
                    return true;
                }
        }
        return super.onOptionsItemSelected(item);
    }

    private enum State {UP_ARROW, MENU}
}
