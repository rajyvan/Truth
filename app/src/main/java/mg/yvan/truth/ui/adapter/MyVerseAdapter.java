package mg.yvan.truth.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import mg.yvan.truth.models.Verse;
import mg.yvan.truth.models.database.RealmHelper;
import mg.yvan.truth.ui.view.VerseView;

/**
 * Created by Yvan on 14/06/16.
 */
public class MyVerseAdapter extends RecyclerView.Adapter<MyVerseAdapter.VerseViewHolder> {

    private List<Verse> mVerses;

    public MyVerseAdapter(List<Verse> verses) {
        mVerses = new ArrayList<>(verses);
    }

    @Override
    public VerseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VerseViewHolder(new VerseView(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(VerseViewHolder holder, int position) {
        holder.mVerseView.populate(mVerses.get(position));
    }

    @Override
    public int getItemCount() {
        return mVerses == null ? 0 : mVerses.size();
    }

    public void remove(Verse verse) {
        int position = mVerses.indexOf(verse);
        Realm realm = RealmHelper.getInstance().getRealmForMainThread();
        realm.executeTransaction(realm1 -> mVerses.get(position).setDeleted(true));
        mVerses.remove(position);
        notifyItemRemoved(position);
    }

    static class VerseViewHolder extends RecyclerView.ViewHolder {

        VerseView mVerseView;

        public VerseViewHolder(VerseView itemView) {
            super(itemView);
            mVerseView = itemView;
        }
    }

}
