package mg.yvan.truth.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;

import io.realm.Realm;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;
import mg.yvan.truth.models.Verse;
import mg.yvan.truth.models.database.RealmHelper;
import mg.yvan.truth.models.parse.ParseVerse;
import mg.yvan.truth.ui.view.VerseView;

/**
 * Created by Yvan on 14/06/16.
 */
public class MyVerseAdapter extends RealmBasedRecyclerViewAdapter<Verse, MyVerseAdapter.VerseViewHolder> {

    public MyVerseAdapter(Context context, RealmResults<Verse> realmResults, boolean automaticUpdate, boolean animateResults) {
        super(context, realmResults, automaticUpdate, animateResults);
    }

    @Override
    public VerseViewHolder onCreateRealmViewHolder(ViewGroup parent, int viewType) {
        return new MyVerseAdapter.VerseViewHolder(new VerseView(parent.getContext()));
    }

    @Override
    public void onBindRealmViewHolder(VerseViewHolder verseViewHolder, int i) {
        final Verse verse = realmResults.get(i);
        verseViewHolder.mVerseView.populate(verse);
    }

    public void remove(Verse verse) {
        ParseVerse parseVerse = ParseVerse.from(verse);
        Realm realm = RealmHelper.getInstance().getRealmForMainThread();
        realm.executeTransaction(realm1 -> verse.deleteFromRealm());
        parseVerse.deleteEventually();
    }

    static class VerseViewHolder extends RealmViewHolder {

        VerseView mVerseView;

        public VerseViewHolder(VerseView itemView) {
            super(itemView);
            mVerseView = itemView;
        }
    }

}
