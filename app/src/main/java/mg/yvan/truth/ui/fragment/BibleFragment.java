package mg.yvan.truth.ui.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.Bind;
import de.greenrobot.event.EventBus;
import mg.yvan.truth.R;
import mg.yvan.truth.event.OnBookChangeEvent;
import mg.yvan.truth.models.Book;
import mg.yvan.truth.models.Verse;
import mg.yvan.truth.ui.adapter.BiblePagerAdapter;
import mg.yvan.truth.ui.dialog.SelectBookDialog;

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

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectBookDialog.show((AppCompatActivity) getActivity());
            }
        });

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

        int id = loader.getId();

        if (id == LOADER_ID) {
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
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

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

    public void onEvent(OnBookChangeEvent event) {
        final long bookId = event.getBookId();
        final int chapter = event.getChapter();
        final int verse = event.getVerse();
        if (chapter > 0 && verse > 0) {
            selectedVerse = new Verse();
            selectedVerse.setBookId(bookId);
            selectedVerse.setChapter(chapter);
            selectedVerse.setVerse(verse);
        }

        Bundle bundle = new Bundle();
        bundle.putLong(Book.ID, bookId);
        bundle.putLong(Verse.CHAPTER, chapter);
        bundle.putLong(Verse.VERSE, verse);
        getLoaderManager().restartLoader(LOADER_ID, bundle, this);
    }

}
