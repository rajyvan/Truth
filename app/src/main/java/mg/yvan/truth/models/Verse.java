package mg.yvan.truth.models;

import android.content.Context;
import android.database.Cursor;

import java.util.Date;

import io.realm.RealmObject;
import mg.yvan.truth.models.database.DataBook;

/**
 * Created by Yvan on 13/06/16.
 */
public class Verse extends RealmObject {

    public final static String BOOK_ID = "bookId";
    public final static String CHAPTER = "chapter";
    public final static String VERSE = "verse";
    public final static String DATE_ADDED = "dateAdded";
    private final static String REF_FORMAT = "%s %d:%d";

    private String objectId;
    private int bookId;
    private int chapter;
    private int verse;
    private String text;
    private String book;
    private Date dateAdded;

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

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String formatReference(Context context) {
        String selection = DataBook.BOOK_REF_ID + "=" + bookId;
        Cursor cursor = context.getContentResolver().query(DataBook.CONTENT_URI, DataBook.PROJECTION_ALL, selection, null, null);
        if (cursor.moveToFirst()) {
            final DataBook book = new DataBook().fromCursor(cursor);
            return String.format(REF_FORMAT, book.getName(), chapter, verse);
        }
        return "";
    }
}
