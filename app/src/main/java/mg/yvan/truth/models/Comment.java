package mg.yvan.truth.models;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by Yvan on 09/06/16.
 */
public class Comment extends RealmObject {

    public final static String DATE_ADDED = "addedDate";

    private Reference mReference;
    private String text;
    private Date addedDate;
    private String author;
    private String authorUrl;

    public Reference getReference() {
        return mReference;
    }

    public void setReference(Reference reference) {
        mReference = reference;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(Date addedDate) {
        this.addedDate = addedDate;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthorUrl() {
        return authorUrl;
    }

    public void setAuthorUrl(String authorUrl) {
        this.authorUrl = authorUrl;
    }
}
