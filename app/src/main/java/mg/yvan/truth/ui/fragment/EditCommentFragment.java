package mg.yvan.truth.ui.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import de.greenrobot.event.EventBus;
import mg.yvan.truth.R;
import mg.yvan.truth.event.OnCommentDeletedEvent;
import mg.yvan.truth.event.OnNewCommentEvent;
import mg.yvan.truth.models.Reference;
import mg.yvan.truth.models.database.DataVerse;
import mg.yvan.truth.ui.adapter.CommentAdapter;
import mg.yvan.truth.ui.dialog.NewCommentDialog;

/**
 * Created by Yvan on 15/06/16.
 */
public class EditCommentFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_ID = 1;

    @Bind(R.id.tv_ref)
    TextView mTvRef;
    @Bind(R.id.tv_verse)
    TextView mTvVerse;
    @Bind(R.id.recycler)
    RecyclerView mRecycler;
    @Bind(R.id.fab)
    FloatingActionButton mFab;

    private Reference mReference;
    private CommentAdapter mCommentAdapter;

    public static EditCommentFragment newInstance(Reference reference) {
        EditCommentFragment fragment = new EditCommentFragment();
        fragment.setReference(reference);
        return fragment;
    }

    public void setReference(Reference reference) {
        mReference = reference;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getLoaderManager().initLoader(LOADER_ID, null, this);

        ViewCompat.setTransitionName(mTvVerse, getResources().getString(R.string.transition_verse));

        mTvRef.setText(String.format(getContext().getString(R.string.comment_ref), mReference.getBookName(), mReference.getChapter(), mReference.getStartVerse(), mReference.getStartVerse()));

        mFab.setOnClickListener(v -> {
            NewCommentDialog.show((AppCompatActivity) getActivity(), mReference);
        });

        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mCommentAdapter = new CommentAdapter(mReference.getComments());
        mRecycler.setAdapter(mCommentAdapter);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_edit_comment;
    }

    @Override
    public int getTitle() {
        return 0;
    }

    @Override
    public boolean showBackButton() {
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    public void onEventMainThread(OnNewCommentEvent event) {
        mCommentAdapter.addComment(event.getComment());
    }

    public void onEventMainThread(OnCommentDeletedEvent event) {
        mCommentAdapter.remove(event.getComment());
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        String[] args = new String[]{String.valueOf(mReference.getBookId()), String.valueOf(mReference.getChapter()), String.valueOf(mReference.getStartVerse()), String.valueOf(mReference.getEndVerse())};
        return new CursorLoader(getActivity(),
                DataVerse.CONTENT_URI,
                DataVerse.PROJECTION_ALL,
                DataVerse.BOOK_ID + " = ? AND " + DataVerse.CHAPTER + " = ? AND " + DataVerse.VERSE + " >= ? AND " + DataVerse.VERSE + " <= ?",
                args,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        String text = "";
        if (cursor != null && cursor.moveToFirst()) {
            do {
                final DataVerse verse = new DataVerse().fromCursor(cursor);
                text += verse.getVerse() + " " + verse.getText() + " ";
            } while (cursor.moveToNext());
        }
        mTvVerse.setText(text);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
