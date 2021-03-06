package mg.yvan.truth.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import mg.yvan.truth.models.Comment;
import mg.yvan.truth.models.Reference;
import mg.yvan.truth.models.database.RealmHelper;
import mg.yvan.truth.ui.view.ReferenceView;

/**
 * Created by Yvan on 14/06/16.
 */
public class MyReferenceAdapter extends RecyclerView.Adapter<MyReferenceAdapter.CommentViewHolder> {

    private List<Reference> mReferences;

    public MyReferenceAdapter(List<Reference> references) {
        mReferences = new ArrayList<>(references);
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CommentViewHolder(new ReferenceView(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        holder.mCommentView.populate(mReferences.get(position), false);
    }

    @Override
    public int getItemCount() {
        return mReferences == null ? 0 : mReferences.size();
    }

    public void remove(Reference reference) {
        int position = mReferences.indexOf(reference);
        Realm realm = RealmHelper.getInstance().getRealmForMainThread();
        //TODO
        realm.executeTransaction(realm1 -> {
            List<Comment> comments = mReferences.get(position).getComments();
            for (Comment comment : comments) {
                comment.deleteFromRealm();
            }
            mReferences.get(position).deleteFromRealm();
        });
        mReferences.remove(position);
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
