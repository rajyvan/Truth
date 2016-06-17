package mg.yvan.truth.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import mg.yvan.truth.models.Comment;
import mg.yvan.truth.models.database.RealmHelper;
import mg.yvan.truth.ui.view.ReferenceView;

/**
 * Created by Yvan on 14/06/16.
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private List<Comment> mComments;
    private boolean mIsPublic;

    public CommentAdapter(List<Comment> Comments, boolean isPublic) {
        mComments = new ArrayList<>(Comments);
        mIsPublic = isPublic;
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CommentViewHolder(new ReferenceView(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        //holder.mCommentView.populate(mComments.get(position));
    }

    @Override
    public int getItemCount() {
        return mComments == null ? 0 : mComments.size();
    }

    public void remove(Comment Comment) {
        int position = mComments.indexOf(Comment);
        Realm realm = RealmHelper.getInstance().getRealmForMainThread();
        realm.executeTransaction(realm1 -> mComments.get(position).deleteFromRealm());
        mComments.remove(position);
        notifyItemRemoved(position);
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {

        ReferenceView mCommentView;

        public CommentViewHolder(ReferenceView itemView) {
            super(itemView);
            mCommentView = itemView;
        }
    }

}
