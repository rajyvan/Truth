package mg.yvan.truth.event;

import android.view.View;

import mg.yvan.truth.models.Verse;

/**
 * Created by Yvan on 15/06/16.
 */
public class OnEditVerseEvent {

    private View mSharedView;
    private Verse verse;

    public OnEditVerseEvent(View sharedView, Verse verse) {
        mSharedView = sharedView;
        this.verse = verse;
    }

    public View getSharedView() {
        return mSharedView;
    }

    public Verse getVerse() {
        return verse;
    }
}
