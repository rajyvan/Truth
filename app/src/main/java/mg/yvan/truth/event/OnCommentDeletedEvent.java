package mg.yvan.truth.event;

import mg.yvan.truth.models.Comment;

/**
 * Created by Yvan on 17/06/16.
 */
public class OnCommentDeletedEvent {

    private Comment mComment;

    public OnCommentDeletedEvent(Comment comment) {
        mComment = comment;
    }

    public Comment getComment() {
        return mComment;
    }
}
