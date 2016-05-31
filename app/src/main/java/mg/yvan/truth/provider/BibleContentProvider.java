package mg.yvan.truth.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import mg.yvan.truth.models.Book;
import mg.yvan.truth.models.Verse;


/**
 * Created by raj_yvan on 12/07/2014.
 */
public class BibleContentProvider extends ContentProvider {

    private final static String AUTHORITY = "mg.rajras.truth.provider";

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    // Contant for use with uri matcher
    private static final int BOOK_LIST = 1;
    private static final int BOOK_ID = 2;
    private static final int VERSE_LIST = 3;
    private static final int VERSE_ID = 4;
    // Uri matcher
    private static final UriMatcher URI_MATCHER;

    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(AUTHORITY, "book", BOOK_LIST);
        URI_MATCHER.addURI(AUTHORITY, "book/#", BOOK_ID);
        URI_MATCHER.addURI(AUTHORITY, "verse", VERSE_LIST);
        URI_MATCHER.addURI(AUTHORITY, "verse/#", VERSE_ID);
    }

    private BibleDbOpenHelper bibleDbOpenHelper;

    @Override
    public boolean onCreate() {
        bibleDbOpenHelper = new BibleDbOpenHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] args, String sortOrder) {
        SQLiteDatabase db = bibleDbOpenHelper.getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

        builder.setTables(getTableFromUri(uri));

        switch (URI_MATCHER.match(uri)) {
            case BOOK_LIST:
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = Book.SORT_ORDER_DEFAULT;
                }
                break;
            case BOOK_ID:
                builder.appendWhere(Book.ID + " = " + uri.getLastPathSegment());
                break;
            case VERSE_LIST:
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = Verse.SORT_ORDER_DEFAULT;
                }
                break;
            case VERSE_ID:
                builder.appendWhere(Verse.ID + " = " + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unsupported uri: " + uri);
        }

        Cursor cursor = builder.query(db, projection, selection, args, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }


    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        SQLiteDatabase db = bibleDbOpenHelper.getWritableDatabase();
        int count = db.update(getTableFromUri(uri), contentValues, s, strings);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case BOOK_LIST:
                return Book.CONTENT_TYPE;
            case BOOK_ID:
                return Book.CONTENT_ITEM_TYPE;
            case VERSE_LIST:
                return Verse.CONTENT_TYPE;
            case VERSE_ID:
                return Verse.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {

        if (URI_MATCHER.match(uri) != BOOK_LIST && URI_MATCHER.match(uri) != VERSE_LIST) {
            throw new IllegalArgumentException("Unsupported URI for insertion" + uri);
        }

        SQLiteDatabase db = bibleDbOpenHelper.getWritableDatabase();

        long id = db.insert(getTableFromUri(uri), null, contentValues);
        if (id > 0) {
            Uri itemUri = ContentUris.withAppendedId(uri, id);
            getContext().getContentResolver().notifyChange(itemUri, null);
            return itemUri;
        }

        throw new SQLiteException("problem when inserting uri: " + uri);
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        SQLiteDatabase db = bibleDbOpenHelper.getWritableDatabase();
        int count = db.delete(getTableFromUri(uri), s, strings);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    private String getTableFromUri(Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case BOOK_LIST:
                return BibleDbOpenHelper.TBL_BOOK;
            case BOOK_ID:
                return BibleDbOpenHelper.TBL_BOOK;
            case VERSE_LIST:
                return BibleDbOpenHelper.TBL_VERSE;
            case VERSE_ID:
                return BibleDbOpenHelper.TBL_VERSE;
            default:
                throw new IllegalArgumentException("Uri not recognized!");
        }
    }

}
