package mg.yvan.truth.ui.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.view.View;

import butterknife.Bind;
import mg.yvan.truth.R;
import mg.yvan.truth.models.Book;
import mg.yvan.truth.models.Verse;
import mg.yvan.truth.ui.adapter.BiblePagerAdapter;

/**
 * Created by Yvan on 30/05/16.
 */
public class BibleFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_ID = 1;

    @Bind(R.id.viewpager)
    ViewPager mViewPager;
    @Bind(R.id.fab)
    FloatingActionButton mFab;

    private BiblePagerAdapter mAdapter;
    private Book mCurrentBook;
    private Verse selectedVerse;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getLoaderManager().initLoader(LOADER_ID, null, this);

        mViewPager.setOffscreenPageLimit(5);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_bible;
    }

    @Override
    public int getTitle() {
        return R.string.menu_bible;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        long bookId = 1;
        if (bundle != null) {
            bookId = bundle.getLong(Book.ID);
        }
        String selection = Book.ID + "=" + bookId;
        return new CursorLoader(getActivity(), Book.CONTENT_URI, null, selection, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int currentChapter = 0;
                int currentVerse = 0;
                if (selectedVerse != null) {
                    currentChapter = selectedVerse.getChapter();
                    currentVerse = selectedVerse.getVerse();
                }

                Book book = new Book().fromCursor(cursor);
                mAdapter = new BiblePagerAdapter(getChildFragmentManager(), getActivity(), book, currentChapter, currentVerse);
                mViewPager.setAdapter(mAdapter);
                mViewPager.setCurrentItem(currentChapter - 1);
                mCurrentBook = book;

                /*titleTextView.setText(book.getName());
                chapterTextView.setText("Chap " + (currentChapter + 1) + " / " + mAdapter.getCount());
                titleTextView.setText(book.getName());
                setCurrentBookGrid();

                if (slidingMenu.isMenuShowing())
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            slidingMenu.toggle();
                        }
                    }, 300);
                    */
            }
        }
        selectedVerse = null;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
