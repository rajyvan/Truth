package mg.yvan.truth.models.parse;

import android.text.TextUtils;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.Date;

import mg.yvan.truth.models.Comment;

/**
 * Created by Yvan on 20/06/16.
 */
@ParseClassName("Comment")
public class ParseComment extends ParseObject {

    public static ParseComment from(Comment comment) {
        ParseComment parseComment = new ParseComment();
        parseComment.setObjectId(comment.getParseId());
        parseComment.setReference(ParseReference.from(comment.getReference()));
        parseComment.setText(comment.getText());
        parseComment.setAddedDate(comment.getAddedDate());
        parseComment.setAuthor(comment.getAuthor());
        parseComment.setAuthorUrl(comment.getAuthorUrl());
        String parseId = comment.getParseId();
        parseComment.setObjectId(parseId.startsWith("none") ? null : parseId);
        return parseComment;
    }

    public static Comment toComment(ParseComment parseComment) {
        Comment comment = new Comment();
        comment.setParseId(parseComment.getObjectId());
        comment.setText(parseComment.getText());
        comment.setAddedDate(parseComment.getAddedDate());
        comment.setAuthor(parseComment.getAuthor());
        comment.setAuthorUrl(parseComment.getAuthorUrl());
        return comment;
    }

    public ParseReference getReference() {
        return (ParseReference) getParseObject("mReference");
    }

    public void setReference(ParseReference reference) {
        put("mReference", reference);
    }

    public String getText() {
        return getString("text");
    }

    public void setText(String text) {
        put("text", text);
    }

    public Date getAddedDate() {
        return getDate("addedDate");
    }

    public void setAddedDate(Date addedDate) {
        put("addedDate", addedDate);
    }

    public String getAuthor() {
        return getString("author");
    }

    public void setAuthor(String author) {
        if (!TextUtils.isEmpty(author)) {
            put("author", author);
        }
    }

    public String getAuthorUrl() {
        return getString("authorUrl");
    }

    public void setAuthorUrl(String authorUrl) {
        if (!TextUtils.isEmpty(authorUrl)) {
            put("authorUrl", authorUrl);
        }
    }
}
