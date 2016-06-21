package mg.yvan.truth.models;

import android.content.Context;
import android.database.Cursor;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import mg.yvan.truth.TruthApplication;
import mg.yvan.truth.models.database.DataBook;
import mg.yvan.truth.models.database.DataVerse;

/**
 * Created by Yvan on 13/06/16.
 */
public class Verse extends RealmObject {

    public final static String BOOK_ID = "bookId";
    public final static String CHAPTER = "chapter";
    public final static String VERSE = "verse";
    public final static String DATE_ADDED = "dateAdded";
    private final static String REF_FORMAT = "%s %d:%d";

    @PrimaryKey
    private String parseId;
    private int bookId;
    private int chapter;
    private int verse;
    private String text;
    private String book;
    private Date dateAdded;
    private boolean deleted;

    public static Verse from(DataVerse dataVerse) {
        final Verse verse = new Verse();
        String selection = DataBook.BOOK_REF_ID + "=" + dataVerse.getBookId();
        Cursor cursor = TruthApplication.getAppContext().getContentResolver().query(DataBook.CONTENT_URI, DataBook.PROJECTION_ALL, selection, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            DataBook book = new DataBook().fromCursor(cursor);
            verse.setBook(book.getName());
            verse.setText(dataVerse.getText());
            verse.setVerse(dataVerse.getVerse());
            verse.setChapter(dataVerse.getChapter());
            verse.setBookId((int) dataVerse.getBookId());
        }
        return verse;
    }

    public String getParseId() {
        return parseId;
    }

    public void setParseId(String parseId) {
        this.parseId = parseId;
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

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
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
