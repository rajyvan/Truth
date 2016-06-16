package mg.yvan.truth.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import de.greenrobot.event.EventBus;
import io.realm.Sort;
import mg.yvan.truth.R;
import mg.yvan.truth.event.OnCommentDeletedEvent;
import mg.yvan.truth.models.Comment;
import mg.yvan.truth.models.database.RealmHelper;
import mg.yvan.truth.ui.adapter.CommentAdapter;

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

    private CommentAdapter mCommentAdapter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        final List<Comment> comments = RealmHelper.getInstance().getRealmForMainThread()
                .where(Comment.class)
                .findAllSorted(Comment.DATE_ADDED, Sort.DESCENDING);

        if (!comments.isEmpty()) {
            mPlaceholder.setVisibility(View.GONE);
            mCommentAdapter = new CommentAdapter(comments, false);
            mRecyclerView.setAdapter(mCommentAdapter);
            mTvNumber.setText(getResources().getQuantityString(R.plurals.nb_verses, mCommentAdapter.getItemCount(), mCommentAdapter.getItemCount()));
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

    public void onEventMainThread(OnCommentDeletedEvent event) {
        mCommentAdapter.remove(event.getComment());
    }

}
