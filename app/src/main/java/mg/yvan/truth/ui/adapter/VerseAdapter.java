package mg.yvan.truth.ui.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import mg.yvan.truth.models.Verse;
import mg.yvan.truth.ui.view.VerseView;

/**
 * Created by Yvan on 31/05/16.
 */
public class VerseAdapter extends CursorRecyclerViewAdapter<VerseAdapter.VerseViewHolder> {

    public VerseAdapter(Context context, Cursor cursor) {
        super(context, cursor);
    }

    @Override
    public void onBindViewHolder(VerseViewHolder viewHolder, Cursor cursor) {
        int number = cursor.getInt(cursor.getColumnIndex(Verse.VERSE));
        String text = cursor.getString(cursor.getColumnIndex(Verse.TEXT));
        viewHolder.mVerseView.populate(number, text);
    }

    @Override
    public VerseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VerseViewHolder(new VerseView(parent.getContext()));
    }

    static class VerseViewHolder extends RecyclerView.ViewHolder {

        VerseView mVerseView;

        public VerseViewHolder(VerseView itemView) {
            super(itemView);
            mVerseView = itemView;
        }
    }

}
