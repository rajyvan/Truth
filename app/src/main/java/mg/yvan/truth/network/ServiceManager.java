package mg.yvan.truth.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.List;

import io.realm.Realm;
import mg.yvan.truth.TruthApplication;
import mg.yvan.truth.models.Verse;
import mg.yvan.truth.models.parse.ParseVerse;

/**
 * Created by Yvan on 14/06/16.
 */
public class ServiceManager {

    private static ServiceManager singleInstance;

    private ServiceManager() {

    }

    public static ServiceManager getInstance() {
        if (singleInstance == null) {
            singleInstance = new ServiceManager();
        }
        return singleInstance;
    }

    private static boolean isUserLogged() {
        final ParseUser user = ParseUser.getCurrentUser();
        return user != null && ParseFacebookUtils.isLinked(user);
    }

    private static boolean isConnexionAvailable() {
        ConnectivityManager cm = (ConnectivityManager) TruthApplication.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private static boolean isSyncAllowed() {
        return isUserLogged() && isConnexionAvailable();
    }

    public void syncFavorite() {
        if (!isSyncAllowed()) return;

        new Thread(() -> {

            ParseUser user = ParseUser.getCurrentUser();
            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            final ParseRelation<ParseVerse> relation = user.getRelation("verses");

            // Send all new local verses to server
            List<Verse> localVerses = realm.where(Verse.class).isNull("parseId").findAll();
            for (Verse verse : localVerses) {
                ParseVerse parseVerse = ParseVerse.from(verse);
                try {
                    parseVerse.save();
                } catch (ParseException e) {
                    continue;
                }
                if (!TextUtils.isEmpty(parseVerse.getObjectId())) {
                    verse.setParseId(parseVerse.getObjectId());
                    realm.copyToRealmOrUpdate(verse);
                    relation.add(parseVerse);
                    user.saveEventually();
                }
            }

            // Delete all deleted verse on server
            List<Verse> deletedVerses = realm.where(Verse.class).equalTo("deleted", true).findAll();
            for (Verse verse : deletedVerses) {
                ParseVerse parseVerse = ParseVerse.from(verse);
                relation.remove(parseVerse);
                user.saveEventually();
                verse.deleteFromRealm();
                parseVerse.deleteEventually();
            }

            // Get all distant verses to replace local
            ParseQuery<ParseVerse> query = relation.getQuery();
            try {
                List<ParseVerse> parseVerses = query.find();
                if (parseVerses != null) {
                    realm.delete(Verse.class);
                    for (ParseVerse parseVerse : parseVerses) {
                        Verse verse = ParseVerse.toVerse(parseVerse);
                        realm.copyToRealmOrUpdate(verse);
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            realm.commitTransaction();
            realm.close();

        }).start();
    }

    public void syncReference() {

    }

}
