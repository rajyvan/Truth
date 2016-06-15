package mg.yvan.truth.models.database;

import io.realm.Realm;

/**
 * Created by Yvan on 14/06/16.
 */
public class RealmHelper {

    private static RealmHelper singleInstance;

    private Realm mRealmMainThread;

    public RealmHelper() {
        mRealmMainThread = Realm.getDefaultInstance();
    }

    public static RealmHelper getInstance() {
        if (singleInstance == null) {
            singleInstance = new RealmHelper();
        }
        return singleInstance;
    }

    public Realm getRealmForMainThread() {
        return mRealmMainThread;
    }

    public void release() {
        mRealmMainThread.close();
        mRealmMainThread = null;
        singleInstance = null;
    }
}
