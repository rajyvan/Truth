package mg.yvan.truth.ui.adapter;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import mg.yvan.truth.models.database.DataBook;
import mg.yvan.truth.models.database.DataVerse;
import mg.yvan.truth.ui.fragment.BibleItemFragment;


/**
 * Created by raj_yvan on 12/07/2014.
 */
public class BiblePagerAdapter extends FragmentStatePagerAdapter {

    private Context context;
    private DataBook book;
    private int defaultChapter;
    private int defaultVerse;
    private int count;

    public BiblePagerAdapter(FragmentManager fm, Context context, DataBook book, int defaultChapter, int defaultVerse) {
        super(fm);
        this.book = book;
        this.context = context;
        this.defaultChapter = defaultChapter;
        this.defaultVerse = defaultVerse;
        this.count = getChapterNumber();
    }

    @Override
    public Fragment getItem(int position) {
        if (book == null)
            return null;

        BibleItemFragment fragment = new BibleItemFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(DataVerse.BOOK_ID, book.getId());
        bundle.putLong(DataVerse.CHAPTER, position + 1);
        bundle.putInt(BibleItemFragment.DEFAULT_CHAPTER, defaultChapter);
        bundle.putInt(BibleItemFragment.DEFAULT_VERSE, defaultVerse);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return count;
    }

    private int getChapterNumber() {
        int count = 0;
        String selection = DataVerse.BOOK_ID + "=" + book.getId() + ") GROUP BY (" + DataVerse.CHAPTER;
        Cursor cursor = context.getContentResolver().query(DataVerse.CONTENT_URI, DataVerse.PROJECTION_ALL, selection, null, null);
        if (cursor != null) {
            try {
                count = cursor.getCount();
            } finally {
                cursor.close();
            }
        }
        return count;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return String.valueOf(position + 1);
    }
}
