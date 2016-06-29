package mg.yvan.truth.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.RealmBasedRecyclerViewAdapter;
import mg.yvan.truth.R;

/**
 * Created by Yvan on 29/06/16.
 */

public abstract class BaseListFragment extends BaseFragment {

    @Bind(R.id.tv_number)
    TextView mTvNumber;
    @Bind(R.id.recyclerview)
    RealmRecyclerView mRecyclerView;

    protected abstract RealmBasedRecyclerViewAdapter getAdapter();

    @Override
    protected int getLayout() {
        return R.layout.fragment_base_list;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView.setAdapter(getAdapter());
    }

}
