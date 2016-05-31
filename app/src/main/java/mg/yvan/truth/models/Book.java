package mg.yvan.truth.models;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import mg.yvan.truth.provider.BibleContentProvider;


/**
 * Created by raj_yvan on 12/07/2014.
 */
public class Book {

    public static final String OLD_TESTAMENT = "Ancien testament"; //1
    public static final String NEW_TESTAMENT = "Nouveau testament"; //2

    public static final String ID = "_id";
    public static final String BOOK_REF_ID = "book_reference_id";
    public static final String TESTAMENT_REF_ID = "testament_reference_id";
    public static final String NAME = "name";
    public static final String NORMALIZED_NAME = "normalized_name";
    public static final String READ = "read";
    public static final String FAVORITE = "favorite";
    /**
     * DATABASE UTILS
     */
    public static final Uri CONTENT_URI = Uri.withAppendedPath(BibleContentProvider.CONTENT_URI, "book");
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.mg.rajras.truth.provider.book";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.mg.rajras.truth.provider.book";
    public static final String[] PROJECTION_ALL = {ID, BOOK_REF_ID, TESTAMENT_REF_ID, NAME, NORMALIZED_NAME, READ, FAVORITE};
    public static final String SORT_ORDER_DEFAULT = BOOK_REF_ID + " ASC";
    private long id;
    private long bookRefId;
    private long testamentRefId;
    private String name;
    private String normalizedName;
    private boolean read;
    private boolean favorite;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getBookRefId() {
        return bookRefId;
    }

    public void setBookRefId(long bookRefId) {
        this.bookRefId = bookRefId;
    }

    public long getTestamentRefId() {
        return testamentRefId;
    }

    public void setTestamentRefId(long testamentRefId) {
        this.testamentRefId = testamentRefId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNormalizedName() {
        return normalizedName;
    }

    public void setNormalizedName(String normalizedName) {
        this.normalizedName = normalizedName;
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

    public Book fromCursor(Cursor cursor) {
        setId(cursor.getLong(cursor.getColumnIndex(Book.ID)));
        setBookRefId(cursor.getLong(cursor.getColumnIndex(Book.BOOK_REF_ID)));
        setTestamentRefId(cursor.getLong(cursor.getColumnIndex(Book.TESTAMENT_REF_ID)));
        setName(cursor.getString(cursor.getColumnIndex(Book.NAME)));
        setNormalizedName(cursor.getString(cursor.getColumnIndex(Book.NORMALIZED_NAME)));
        setRead(cursor.getInt(cursor.getColumnIndex(Book.READ)) > 0);
        setFavorite(cursor.getInt(cursor.getColumnIndex(Book.FAVORITE)) > 0);
        return this;
    }

}
