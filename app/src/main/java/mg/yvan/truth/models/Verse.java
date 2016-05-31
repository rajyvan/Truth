package mg.yvan.truth.models;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import mg.yvan.truth.provider.BibleContentProvider;

/**
 * Created by raj_yvan on 12/07/2014.
 */
public class Verse {

    public static final String ID = "_id";
    public static final String BOOK_ID = "book_id";
    public static final String CHAPTER = "chapter";
    public static final String VERSE = "verse";
    public static final String TEXT = "text";
    public static final String NORMALIZED_TEXT = "normalized_text";
    public static final String READ = "read";
    public static final String FAVORITE = "favorite";
    public static final String COMMENT = "comment";
    /**
     * DATABASE UTILS
     */
    public static final Uri CONTENT_URI = Uri.withAppendedPath(BibleContentProvider.CONTENT_URI, "verse");
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.mg.rajras.truth.provider.verse";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.mg.rajras.truth.provider.verse";
    public static final String[] PROJECTION_ALL = {ID, BOOK_ID, CHAPTER, VERSE, TEXT, NORMALIZED_TEXT, READ, FAVORITE, COMMENT};
    public static final String SORT_ORDER_DEFAULT = VERSE + " ASC";
    private long id;
    private long bookId;
    private int chapter;
    private int verse;
    private String text;
    private String normalizedText;
    private boolean read;
    private boolean favorite;
    private String comment;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getBookId() {
        return bookId;
    }

    public void setBookId(long bookId) {
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getNormalizedText() {
        return normalizedText;
    }

    public void setNormalizedText(String normalizedText) {
        this.normalizedText = normalizedText;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Verse fromCursor(Cursor cursor) {
        setId(cursor.getLong(cursor.getColumnIndex(Verse.ID)));
        setBookId(cursor.getLong(cursor.getColumnIndex(Verse.BOOK_ID)));
        setChapter(cursor.getInt(cursor.getColumnIndex(Verse.CHAPTER)));
        setVerse(cursor.getInt(cursor.getColumnIndex(Verse.VERSE)));
        setText(cursor.getString(cursor.getColumnIndex(Verse.TEXT)));
        setNormalizedText(cursor.getString(cursor.getColumnIndex(Verse.NORMALIZED_TEXT)));
        setRead(cursor.getInt(cursor.getColumnIndex(Verse.READ)) > 0);
        setFavorite(cursor.getInt(cursor.getColumnIndex(Verse.FAVORITE)) > 0);
        setComment(cursor.getString(cursor.getColumnIndex(Verse.COMMENT)));
        return this;
    }

    public String formatReference(Context context) {
        String selection = Book.BOOK_REF_ID + "=" + bookId;
        Cursor cursor = context.getContentResolver().query(Book.CONTENT_URI, Book.PROJECTION_ALL, selection, null, null);
        if (cursor.moveToFirst()) {
            final Book book = new Book().fromCursor(cursor);
            return book.getName() + ", Chap. " + chapter + " : " + verse;
        }
        return "";
    }

}
