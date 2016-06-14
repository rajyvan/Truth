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
import android.widget.TextView;

import butterknife.Bind;
import de.greenrobot.event.EventBus;
import mg.yvan.truth.R;
import mg.yvan.truth.event.OnSearchQueryChange;
import mg.yvan.truth.models.database.DataVerse;
import mg.yvan.truth.ui.adapter.SearchResultAdapter;

/**
 * Created by Yvan on 08/06/16.
 */
public class SearchResultFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_ID = 1;

    @Bind(R.id.tv_result)
    TextView mTvResult;
    @Bind(R.id.recylerview)
    RecyclerView mRecylerview;

    private String mCurrentSearchKey;
    private SearchResultAdapter mAdapter;

    public static SearchResultFragment newInstance(String key) {
        SearchResultFragment fragment = new SearchResultFragment();
        fragment.setCurrentSearchKey(key);
        return fragment;
    }

    public void setCurrentSearchKey(String currentSearchKey) {
        mCurrentSearchKey = currentSearchKey;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_search_result;
    }

    @Override
    public int getTitle() {
        return R.string.search_result;
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

    public void onEvent(OnSearchQueryChange event) {
        mCurrentSearchKey = event.getQuery();
        getLoaderManager().restartLoader(LOADER_ID, null, this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecylerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String selection = DataVerse.NORMALIZED_TEXT + " LIKE '%" + mCurrentSearchKey + "%'";
        selection = selection.replace("'", "\'");
        return new CursorLoader(getActivity(), DataVerse.CONTENT_URI, DataVerse.PROJECTION_ALL, selection, null, DataVerse.BOOK_ID + " ASC, " + DataVerse.CHAPTER + " ASC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mAdapter = new SearchResultAdapter(getActivity(), cursor, mCurrentSearchKey);
        mRecylerview.setAdapter(mAdapter);
        mTvResult.setText(getResources().getQuantityString(R.plurals.nb_results, mAdapter.getItemCount(), mAdapter.getItemCount()));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
