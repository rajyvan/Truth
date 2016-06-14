package mg.yvan.truth.models;

import io.realm.RealmObject;

/**
 * Created by Yvan on 13/06/16.
 */
public class Verse extends RealmObject {

    public final static String BOOK_ID = "bookId";
    public final static String CHAPTER = "chapter";
    public final static String VERSE = "verse";

    private String objectId;
    private int bookId;
    private int chapter;
    private int verse;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getChapter() {
        return chapter;
    }

    public void setChapter(int chapter) {
        this.chapter = chapter;
    }

    public int getVerse() {
        return verse;
    }

    public void setVerse(int verse) {
        this.verse = verse;
    }
}
