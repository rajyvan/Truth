package mg.yvan.truth.ui.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ViewFlipper;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import mg.yvan.truth.Defines;
import mg.yvan.truth.R;
import mg.yvan.truth.event.OnBookChangeEvent;
import mg.yvan.truth.models.database.DataBook;
import mg.yvan.truth.ui.adapter.BookAdapter;

/**
 * Created by Yvan on 07/06/16.
 */
public class SelectBookDialog extends DialogFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String TAG = "select_book_dialog";
    private static final int FIRST_PAGE = 0;
    private static final int SECOND_PAGE = 1;
    private static final int BOOK_CONTENT_LOADER_ID = 2;
    private static final int SEARCH_BOOK_LOADER_ID = 3;
    private static final String ARG_TESTAMENT = "testament";

    @Bind(R.id.btn_back)
    ImageButton mBtnBack;
    @Bind(R.id.book_recycler)
    RecyclerView mBookRecycler;
    @Bind(R.id.flipper)
    ViewFlipper mFlipper;
    @Bind(R.id.search_recycler)
    RecyclerView mSearchRecycler;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    private String mCurrentSearchKey;

    public static void show(AppCompatActivity activity) {
        SelectBookDialog dialog = new SelectBookDialog();
        dialog.show(activity.getSupportFragmentManager(), TAG);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.dialog_select_book, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.85f);
        getDialog().getWindow().setLayout(width, height);

        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(android.content.DialogInterface dialog, int keyCode,
                                 android.view.KeyEvent event) {

                if ((keyCode == android.view.KeyEvent.KEYCODE_BACK)) {
                    //This is the filter
                    if (event.getAction() != KeyEvent.ACTION_DOWN)
                        return true;
                    else {
                        if (mFlipper.getDisplayedChild() != FIRST_PAGE) {
                            show(FIRST_PAGE);
                        } else {
                            dismiss();
                        }
                        return true;
                    }
                } else
                    return false;
            }
        });
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mToolbar.inflateMenu(R.menu.main_menu);
        setupMenu();

        mBookRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mSearchRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show(FIRST_PAGE);
            }
        });
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
        dismiss();
    }

    private void setupMenu() {
        final MenuItem searchItem = mToolbar.getMenu().findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String charSequence) {
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
                    mCurrentSearchKey = charSequence;
                    getLoaderManager().restartLoader(SEARCH_BOOK_LOADER_ID, null, SelectBookDialog.this);
                }
                return true;
            }
        });
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        if (id == BOOK_CONTENT_LOADER_ID) {
            int defaultValue = 1;
            if (bundle != null) {
                defaultValue = bundle.getInt(ARG_TESTAMENT);
            }
            String selection = DataBook.TESTAMENT_REF_ID + "=" + (defaultValue == Defines.OLD_TESTAMENT ? 1 : 2);
            return new CursorLoader(getActivity(), DataBook.CONTENT_URI, DataBook.PROJECTION_ALL, selection, null, null);
        } else {
            String selection = DataBook.NORMALIZED_NAME + " LIKE '%" + mCurrentSearchKey + "%'";
            selection = selection.replace("'", "\'");
            return new CursorLoader(getActivity(), DataBook.CONTENT_URI, DataBook.PROJECTION_ALL, selection, null, DataBook.ID + " ASC");
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        int id = loader.getId();
        if (id == BOOK_CONTENT_LOADER_ID) {
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

}
