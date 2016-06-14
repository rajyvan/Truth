package mg.yvan.truth.manager;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.facebook.AccessToken;

import mg.yvan.truth.TruthApplication;

/**
 * Created by Yvan on 09/06/16.
 */
public class SessionManager {

    private final static String LAST_BOOK = "book";
    private final static String LAST_CHAPTER = "chapter";

    private static SessionManager singleInstance;

    public static SessionManager getInstance() {
        if (singleInstance == null) {
            singleInstance = new SessionManager();
        }
        return singleInstance;
    }

    public boolean isLogged() {
        return AccessToken.getCurrentAccessToken() != null;
    }

    public void saveLastBookAndChapter(int bookId, int chapter) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(TruthApplication.getAppContext()).edit();
        editor.putInt(LAST_BOOK, bookId);
        editor.putInt(LAST_CHAPTER, chapter);
        editor.apply();
    }

    public int[] retrieveLastBookAndChapter() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(TruthApplication.getAppContext());
        final int bookId = settings.getInt(LAST_BOOK, 0);
        final int chapter = settings.getInt(LAST_CHAPTER, 0);
        int[] result = new int[2];
        result[0] = bookId;
        result[1] = chapter;
        return result;
    }

}
