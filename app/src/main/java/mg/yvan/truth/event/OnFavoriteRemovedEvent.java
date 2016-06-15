package mg.yvan.truth.event;

import mg.yvan.truth.models.Verse;

/**
 * Created by Yvan on 15/06/16.
 */
public class OnFavoriteRemovedEvent {

    private Verse verse;

    public OnFavoriteRemovedEvent(Verse verse) {
        this.verse = verse;
    }

    public Verse getVerse() {
        return verse;
    }
}
