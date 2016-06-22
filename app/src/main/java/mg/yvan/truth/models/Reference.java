package mg.yvan.truth.models;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Yvan on 16/06/16.
 */
public class Reference extends RealmObject {

    public final static String UPDATE_DATE = "updateDate";

    public final static String BOOK_ID = "bookId";
    public final static String CHAPTER = "chapter";
    public final static String START_VERSE = "startVerse";
    public final static String END_VERSE = "endVerse";

    @PrimaryKey
    private String parseId;
    private int bookId;
    private String bookName;
    private int chapter;
    private int startVerse;
    private int endVerse;
    private RealmList<Comment> mComments;
    private Date updateDate;
    private boolean deleted;

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public int getChapter() {
        return chapter;
    }

    public void setChapter(int chapter) {
        this.chapter = chapter;
    }

    public int getStartVerse() {
        return startVerse;
    }

    public void setStartVerse(int startVerse) {
        this.startVerse = startVerse;
    }

    public int getEndVerse() {
        return endVerse;
    }

    public void setEndVerse(int endVerse) {
        this.endVerse = endVerse;
    }

    public RealmList<Comment> getComments() {
        return mComments;
    }

    public void setComments(RealmList<Comment> comments) {
        mComments = comments;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getParseId() {
        return parseId;
    }

    public void setParseId(String parseId) {
        this.parseId = parseId;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
