package mg.yvan.truth.ui.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import butterknife.Bind;
import butterknife.OnClick;
import mg.yvan.truth.Defines;
import mg.yvan.truth.R;
import mg.yvan.truth.models.Book;
import mg.yvan.truth.models.Verse;
import mg.yvan.truth.ui.adapter.BiblePagerAdapter;
import mg.yvan.truth.ui.adapter.BookAdapter;

/**
 * Created by Yvan on 30/05/16.
 */
public class BibleFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int FIRST_PAGE = 0;
    private static final int SECOND_PAGE = 1;
    private static final int LOADER_ID = 1;
    private static final int BOOK_CONTENT_LOADER_ID = 2;
    private static final int SEARCH_BOOK_LOADER_ID = 3;
    private static final String ARG_TESTAMENT = "testament";

    @Bind(R.id.viewpager)
    ViewPager mViewPager;
    @Bind(R.id.fab)
    FloatingActionButton mFab;
    @Bind(R.id.et_search)
    EditText mEtSearch;
    @Bind(R.id.book_recycler)
    RecyclerView mBookRecycler;
    @Bind(R.id.flipper)
    ViewFlipper mFlipper;
    @Bind(R.id.search_recycler)
    RecyclerView mSearchRecycler;
    @Bind(R.id.sliding_pannel)
    SlidingUpPanelLayout mSlidingPannel;
    @Bind(R.id.panel_container)
    LinearLayout mPanelContainer;
    @Bind(R.id.btn_back)
    ImageButton mBtnBack;

    private BiblePagerAdapter mAdapter;
    private Book mCurrentBook;
    private Verse selectedVerse;

    private String mCurrentSearchKey;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getLoaderManager().initLoader(LOADER_ID, null, this);

        mViewPager.setOffscreenPageLimit(5);

        mPanelContainer.setLayoutParams(new SlidingUpPanelLayout.LayoutParams(
                SlidingUpPanelLayout.LayoutParams.MATCH_PARENT,
                getResources().getDisplayMetrics().heightPixels / 2 + getResources().getDimensionPixelSize(R.dimen.panel_handle)
        ));

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSlidingPannel.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
                    mSlidingPannel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                } else {
                    mSlidingPannel.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                }
            }
        });

        configureBottomPanel();
    }

    private void configureBottomPanel() {

        mBookRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mSearchRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show(FIRST_PAGE);
            }
        });

        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (TextUtils.isEmpty(charSequence)) {
                    mFlipper.setVisibility(View.VISIBLE);
                    mSearchRecycler.setVisibility(View.GONE);
                    if (mFlipper.getDisplayedChild() == FIRST_PAGE) {
                        mBtnBack.setVisibility(View.GONE);
                    } else {
                        mBtnBack.setVisibility(View.VISIBLE);
                    }
                } else {
                    mBtnBack.setVisibility(View.GONE);
                    mFlipper.setVisibility(View.GONE);
                    mSearchRecycler.setVisibility(View.VISIBLE);
                    if (charSequence.length() > 2) {
                        mCurrentSearchKey = charSequence.toString();
                        getLoaderManager().restartLoader(SEARCH_BOOK_LOADER_ID, null, BibleFragment.this);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

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
        if (id == LOADER_ID) {
            long bookId = 1;
            if (bundle != null) {
                bookId = bundle.getLong(Book.ID);
            }
            String selection = Book.ID + "=" + bookId;
            return new CursorLoader(getActivity(), Book.CONTENT_URI, null, selection, null, null);
        } else if (id == BOOK_CONTENT_LOADER_ID) {
            int defaultValue = 1;
            if (bundle != null) {
                defaultValue = bundle.getInt(ARG_TESTAMENT);
            }
            String selection = Book.TESTAMENT_REF_ID + "=" + (defaultValue == Defines.OLD_TESTAMENT ? 1 : 2);
            return new CursorLoader(getActivity(), Book.CONTENT_URI, Book.PROJECTION_ALL, selection, null, null);
        } else {
            String selection = Book.NORMALIZED_NAME + " LIKE '%" + mCurrentSearchKey + "%'";
            selection = selection.replace("'", "\'");
            return new CursorLoader(getActivity(), Book.CONTENT_URI, Book.PROJECTION_ALL, selection, null, Book.ID + " ASC");
        }
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

        } else if (id == BOOK_CONTENT_LOADER_ID) {
            BookAdapter bookAdapter = new BookAdapter(getActivity(), cursor);
            mBookRecycler.setAdapter(bookAdapter);
            show(SECOND_PAGE);
        } else if (id == SEARCH_BOOK_LOADER_ID) {
            BookAdapter bookAdapter = new BookAdapter(getActivity(), cursor);
            mSearchRecycler.setAdapter(bookAdapter);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @OnClick(R.id.view_old_testament)
    void loadOldTestament() {
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_TESTAMENT, Defines.OLD_TESTAMENT);
        getLoaderManager().restartLoader(BOOK_CONTENT_LOADER_ID, bundle, this);
    }

    @OnClick(R.id.view_new_testament)
    void loadNewTestament() {
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_TESTAMENT, Defines.NEW_TESTAMENT);
        getLoaderManager().restartLoader(BOOK_CONTENT_LOADER_ID, bundle, this);
    }

    private void show(int page) {
        switch (page) {
            case FIRST_PAGE:
                mFlipper.setInAnimation(getActivity(), R.anim.slide_in_left);
                mFlipper.setOutAnimation(getActivity(), R.anim.slide_out_right);
                mFlipper.showPrevious();
                mBtnBack.setVisibility(View.INVISIBLE);
                break;
            case SECOND_PAGE:
                mFlipper.setInAnimation(getActivity(), R.anim.slide_in_right);
                mFlipper.setOutAnimation(getActivity(), R.anim.slide_out_left);
                mFlipper.showNext();
                Animation fading = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
                fading.setFillAfter(false);
                mBtnBack.startAnimation(fading);
                mBtnBack.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

}
