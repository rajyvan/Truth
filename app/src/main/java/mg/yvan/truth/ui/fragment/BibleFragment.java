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

import com.astuetz.PagerSlidingTabStrip;

import butterknife.Bind;
import de.greenrobot.event.EventBus;
import mg.yvan.truth.R;
import mg.yvan.truth.event.OnBookChangeEvent;
import mg.yvan.truth.event.OnReferenceDetailEvent;
import mg.yvan.truth.manager.TruthFragmentManager;
import mg.yvan.truth.models.database.DataBook;
import mg.yvan.truth.models.database.DataVerse;
import mg.yvan.truth.ui.adapter.BiblePagerAdapter;
import mg.yvan.truth.ui.dialog.SelectBookDialog;

/**
 * Created by Yvan on 30/05/16.
 */
public class BibleFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_ID = 1;
    private static final String TITLE_FORMAT = "%s %d/%d";

    @Bind(R.id.viewpager)
    ViewPager mViewPager;
    @Bind(R.id.fab)
    FloatingActionButton mFab;
    @Bind(R.id.tab_strip)
    PagerSlidingTabStrip mTabStrip;

    private BiblePagerAdapter mAdapter;
    private DataBook mCurrentBook;
    private DataVerse selectedVerse;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getLoaderManager().initLoader(LOADER_ID, null, this);

        mViewPager.setOffscreenPageLimit(5);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setTitle(String.format(TITLE_FORMAT, mCurrentBook.getName(), position + 1, mAdapter.getCount()));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mFab.setOnClickListener(v -> SelectBookDialog.show((AppCompatActivity) getActivity()));


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
            bookId = bundle.getLong(DataBook.ID);
        }
        String selection = DataBook.ID + "=" + bookId;
        return new CursorLoader(getActivity(), DataBook.CONTENT_URI, null, selection, null, null);
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

                    mCurrentBook = new DataBook().fromCursor(cursor);
                    mAdapter = new BiblePagerAdapter(getChildFragmentManager(), getActivity(), mCurrentBook, currentChapter, currentVerse);
                    mViewPager.setAdapter(mAdapter);
                    mTabStrip.setViewPager(mViewPager);
                    mViewPager.setCurrentItem(currentChapter - 1);

                    setTitle(String.format(TITLE_FORMAT, mCurrentBook.getName(), currentChapter + 1, mAdapter.getCount()));
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
            selectedVerse = new DataVerse();
            selectedVerse.setBookId(bookId);
            selectedVerse.setChapter(chapter);
            selectedVerse.setVerse(verse);
        }

        Bundle bundle = new Bundle();
        bundle.putLong(DataBook.ID, bookId);
        bundle.putLong(DataVerse.CHAPTER, chapter);
        bundle.putLong(DataVerse.VERSE, verse);
        getLoaderManager().restartLoader(LOADER_ID, bundle, this);
    }

    public void onEventMainThread(OnReferenceDetailEvent event) {
        TruthFragmentManager.displayEditComment((AppCompatActivity) getActivity(), this, R.string.transition_verse, event.getSharedView(), event.getReference());
    }

    @Override
    public boolean showBackButton() {
        return false;
    }
}
