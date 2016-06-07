package mg.yvan.truth.ui.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import mg.yvan.truth.ui.view.BookView;

/**
 * Created by Yvan on 31/05/16.
 */
public class BookAdapter extends CursorRecyclerViewAdapter<BookAdapter.BookViewHolder> {

    public BookAdapter(Context context, Cursor cursor) {
        super(context, cursor);
    }

    @Override
    public void onBindViewHolder(BookViewHolder viewHolder, Cursor cursor) {
        viewHolder.mBookView.populate(cursor);
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BookViewHolder(new BookView(parent.getContext()));
    }

    static class BookViewHolder extends RecyclerView.ViewHolder {

        BookView mBookView;

        public BookViewHolder(BookView itemView) {
            super(itemView);
            mBookView = itemView;
        }
    }

}
