package mg.yvan.truth.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;

import java.util.Collections;

import io.realm.Realm;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;
import mg.yvan.truth.models.Comment;
import mg.yvan.truth.models.Reference;
import mg.yvan.truth.models.database.RealmHelper;
import mg.yvan.truth.models.parse.ParseComment;
import mg.yvan.truth.models.parse.ParseReference;
import mg.yvan.truth.ui.view.CommentView;

/**
 * Created by Yvan on 14/06/16.
 */
public class CommentAdapter extends RealmBasedRecyclerViewAdapter<Comment, CommentAdapter.CommentViewHolder> {

    public CommentAdapter(Context context, RealmResults<Comment> realmResults, boolean automaticUpdate, boolean animateResults) {
        super(context, realmResults, automaticUpdate, animateResults);
    }

    @Override
    public CommentViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int i) {
        return new CommentViewHolder(new CommentView(viewGroup.getContext()));
    }

    @Override
    public void onBindRealmViewHolder(CommentViewHolder commentViewHolder, int i) {
        commentViewHolder.mCommentView.populate(realmResults.get(i));
    }

    public void remove(Comment comment) {
        Realm realm = RealmHelper.getInstance().getRealmForMainThread();
        realm.executeTransaction(realm1 -> {
            final Reference reference = comment.getReference();
            ParseComment parseComment = ParseComment.from(comment);
            parseComment.deleteEventually();
            comment.deleteFromRealm();
            if (reference.getComments().size() == 0) {
                ParseReference parseReference = ParseReference.from(reference);
                parseReference.deleteEventually();
                reference.deleteFromRealm();
            }
        });
    }

    static class CommentViewHolder extends RealmViewHolder {

        CommentView mCommentView;

        public CommentViewHolder(CommentView itemView) {
            super(itemView);
            mCommentView = itemView;
        }
    }

}
