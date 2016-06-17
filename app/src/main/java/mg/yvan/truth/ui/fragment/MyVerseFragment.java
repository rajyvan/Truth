package mg.yvan.truth.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import de.greenrobot.event.EventBus;
import io.realm.Sort;
import mg.yvan.truth.R;
import mg.yvan.truth.event.OnFavoriteRemovedEvent;
import mg.yvan.truth.event.OnReferenceDetailEvent;
import mg.yvan.truth.manager.TruthFragmentManager;
import mg.yvan.truth.models.Verse;
import mg.yvan.truth.models.database.RealmHelper;
import mg.yvan.truth.ui.adapter.MyVerseAdapter;

/**
 * Created by Yvan on 10/06/16.
 */
public class MyVerseFragment extends BaseFragment {

    @Bind(R.id.tv_number)
    TextView mTvNumber;
    @Bind(R.id.recylerview)
    RecyclerView mRecyclerView;
    @Bind(R.id.placeholder)
    TextView mPlaceholder;

    private MyVerseAdapter mVerseAdapter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        final List<Verse> verses = RealmHelper.getInstance().getRealmForMainThread()
                .where(Verse.class)
                .findAllSorted(Verse.DATE_ADDED, Sort.DESCENDING);

        if (!verses.isEmpty()) {
            mPlaceholder.setVisibility(View.GONE);
            mVerseAdapter = new MyVerseAdapter(verses);
            mRecyclerView.setAdapter(mVerseAdapter);
            mTvNumber.setText(getResources().getQuantityString(R.plurals.nb_verses, mVerseAdapter.getItemCount(), mVerseAdapter.getItemCount()));
        } else {
            mPlaceholder.setVisibility(View.VISIBLE);
        }

    }

    public void onEventMainThread(OnFavoriteRemovedEvent event) {
        if (mVerseAdapter != null) {
            mVerseAdapter.remove(event.getVerse());
            int count = mVerseAdapter.getItemCount();
            mTvNumber.setText(getResources().getQuantityString(R.plurals.nb_verses, count, count));
            if (count == 0) {
                mPlaceholder.setVisibility(View.VISIBLE);
                mTvNumber.setVisibility(View.GONE);
            }
        }
    }

    public void onEventMainThread(OnReferenceDetailEvent event) {
        TruthFragmentManager.displayEditComment((AppCompatActivity) getActivity(), this, R.string.transition_verse, event.getSharedView(), event.getReference());
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

    @Override
    protected int getLayout() {
        return R.layout.fragment_my_verse;
    }

    @Override
    public int getTitle() {
        return R.string.menu_my_verse;
    }

    @Override
    public boolean showBackButton() {
        return false;
    }

}
