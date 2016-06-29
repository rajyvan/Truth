package mg.yvan.truth.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import de.greenrobot.event.EventBus;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmResults;
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
public class MyVerseFragment extends BaseListFragment {

    private MyVerseAdapter mVerseAdapter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RealmHelper.getInstance().getRealmForMainThread().addChangeListener(element -> {
            if (isAdded()) {
                int count = (int) element.where(Verse.class).count();
                mTvNumber.setText(getResources().getQuantityString(R.plurals.nb_verses, count, count));
                mTvNumber.setVisibility(count == 0 ? View.GONE : View.VISIBLE);
            }
        });
    }

    @Override
    protected RealmBasedRecyclerViewAdapter getAdapter() {
        final RealmResults<Verse> verses = RealmHelper.getInstance().getRealmForMainThread()
                .where(Verse.class)
                .findAllSorted(Verse.DATE_ADDED, Sort.DESCENDING);
        mVerseAdapter = new MyVerseAdapter(getActivity(), verses, true, true);
        mTvNumber.setText(getResources().getQuantityString(R.plurals.nb_verses, mVerseAdapter.getItemCount(), mVerseAdapter.getItemCount()));
        mTvNumber.setVisibility(mVerseAdapter.getItemCount() == 0 ? View.GONE : View.VISIBLE);
        return mVerseAdapter;
    }

    public void onEventMainThread(OnFavoriteRemovedEvent event) {
        if (mVerseAdapter != null) {
            mVerseAdapter.remove(event.getVerse());
            int count = mVerseAdapter.getItemCount();
            mTvNumber.setText(getResources().getQuantityString(R.plurals.nb_verses, count, count));
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
    public int getTitle() {
        return R.string.menu_my_verse;
    }

    @Override
    public boolean showBackButton() {
        return false;
    }

}
