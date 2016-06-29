package mg.yvan.truth.ui.fragment;


import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.Bind;
import mg.yvan.truth.R;
import mg.yvan.truth.models.database.DataVerse;
import mg.yvan.truth.ui.adapter.VerseAdapter;


/**
 * Created by raj_yvan on 12/07/2014.
 */
public class BibleItemFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public final static String DEFAULT_CHAPTER = "default_chapter";
    public final static String DEFAULT_VERSE = "default_verse";
    private static final int BIBLE_LOADER_ID = 1;
    private static final int SCROLL_DELAY = 400;

    @Bind(R.id.recyclerview)
    RecyclerView mRecylerView;

    private long bookId;
    private long chapterId;
    private int defaultChapter;
    private int defaultVerse;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            bookId = bundle.getLong(DataVerse.BOOK_ID);
            chapterId = bundle.getLong(DataVerse.CHAPTER);
            defaultChapter = bundle.getInt(DEFAULT_CHAPTER);
            defaultVerse = bundle.getInt(DEFAULT_VERSE);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getLoaderManager().initLoader(BIBLE_LOADER_ID, null, this);

        mRecylerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
    }

    @Override
    protected int getLayout() {
        return R.layout.recyclerview;
    }

    @Override
    public int getTitle() {
        return 0;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] args = new String[]{String.valueOf(bookId), String.valueOf(chapterId)};
        return new CursorLoader(getActivity(), DataVerse.CONTENT_URI, DataVerse.PROJECTION_ALL, DataVerse.BOOK_ID + " = ? AND " + DataVerse.CHAPTER + " = ?", args, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        if (cursorLoader.getId() == BIBLE_LOADER_ID) {
            if (cursor != null) {
                mRecylerView.setAdapter(new VerseAdapter(getActivity(), cursor));
                if (chapterId == defaultChapter && defaultVerse > 0) {
                    mRecylerView.postDelayed(() -> mRecylerView.smoothScrollToPosition(defaultVerse), SCROLL_DELAY);
                }
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    @Override
    public boolean showBackButton() {
        return false;
    }
}
