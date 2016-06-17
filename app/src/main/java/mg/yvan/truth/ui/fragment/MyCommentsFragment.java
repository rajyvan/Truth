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
import mg.yvan.truth.event.OnReferenceDeletedEvent;
import mg.yvan.truth.event.OnReferenceDetailEvent;
import mg.yvan.truth.manager.TruthFragmentManager;
import mg.yvan.truth.models.Reference;
import mg.yvan.truth.models.database.RealmHelper;
import mg.yvan.truth.ui.adapter.MyReferenceAdapter;

/**
 * Created by Yvan on 10/06/16.
 */
public class MyCommentsFragment extends BaseFragment {

    @Bind(R.id.recylerview)
    RecyclerView mRecyclerView;
    @Bind(R.id.tv_number)
    TextView mTvNumber;
    @Bind(R.id.placeholder)
    TextView mPlaceholder;

    private MyReferenceAdapter mMyCommentAdapter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        final List<Reference> references = RealmHelper.getInstance().getRealmForMainThread()
                .where(Reference.class)
                .findAllSorted(Reference.UPDATE_DATE, Sort.DESCENDING);

        if (!references.isEmpty()) {
            mPlaceholder.setVisibility(View.GONE);
            mMyCommentAdapter = new MyReferenceAdapter(references);
            mRecyclerView.setAdapter(mMyCommentAdapter);
            mTvNumber.setText(getResources().getQuantityString(R.plurals.nb_references, mMyCommentAdapter.getItemCount(), mMyCommentAdapter.getItemCount()));
        } else {
            mPlaceholder.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_my_comments;
    }

    @Override
    public int getTitle() {
        return R.string.menu_my_comments;
    }

    @Override
    public boolean showBackButton() {
        return false;
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

    public void onEvent(OnReferenceDeletedEvent event) {
        mMyCommentAdapter.remove(event.getReference());
    }

    public void onEvent(OnReferenceDetailEvent event) {
        TruthFragmentManager.displayEditComment((AppCompatActivity) getActivity(),
                this,
                R.transition.edit_comment_transition,
                event.getSharedView(),
                event.getReference());
    }

}
