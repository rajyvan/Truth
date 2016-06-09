package mg.yvan.truth.models.database;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import mg.yvan.truth.provider.BibleContentProvider;

/**
 * Created by raj_yvan on 12/07/2014.
 */
public class DataVerse {

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
    private final static String REF_FORMAT = "%s %d:%d";
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

    public DataVerse fromCursor(Cursor cursor) {
        setId(cursor.getLong(cursor.getColumnIndex(DataVerse.ID)));
        setBookId(cursor.getLong(cursor.getColumnIndex(DataVerse.BOOK_ID)));
        setChapter(cursor.getInt(cursor.getColumnIndex(DataVerse.CHAPTER)));
        setVerse(cursor.getInt(cursor.getColumnIndex(DataVerse.VERSE)));
        setText(cursor.getString(cursor.getColumnIndex(DataVerse.TEXT)));
        setNormalizedText(cursor.getString(cursor.getColumnIndex(DataVerse.NORMALIZED_TEXT)));
        setRead(cursor.getInt(cursor.getColumnIndex(DataVerse.READ)) > 0);
        setFavorite(cursor.getInt(cursor.getColumnIndex(DataVerse.FAVORITE)) > 0);
        setComment(cursor.getString(cursor.getColumnIndex(DataVerse.COMMENT)));
        return this;
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
