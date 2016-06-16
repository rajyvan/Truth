package mg.yvan.truth.event;

import mg.yvan.truth.models.Comment;

/**
 * Created by Yvan on 16/06/16.
 */
public class OnNewCommentEvent {

    private Comment mComment;

    public OnNewCommentEvent(Comment comment) {
        mComment = comment;
    }

    public Comment getComment() {
        return mComment;
    }
}
