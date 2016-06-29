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
import io.realm.RealmList;
import mg.yvan.truth.TruthApplication;
import mg.yvan.truth.models.Comment;
import mg.yvan.truth.models.Reference;
import mg.yvan.truth.models.Verse;
import mg.yvan.truth.models.parse.ParseComment;
import mg.yvan.truth.models.parse.ParseReference;
import mg.yvan.truth.models.parse.ParseVerse;

/**
 * Created by Yvan on 14/06/16.
 */
public class ServiceManager {

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

    public static void sync() {
        if (isSyncAllowed()) {
            syncFavorite();
            syncReference();
        }
    }

    private static void syncFavorite() {
        ParseUser user = ParseUser.getCurrentUser();
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        final ParseRelation<ParseVerse> relation = user.getRelation("verses");

        // Send all new local verses to server
        List<Verse> localVerses = realm.where(Verse.class).findAll();
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
                try {
                    user.save();
                } catch (ParseException e) {
                    // do nothing
                }
            }
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
    }

    private static void syncReference() {
        ParseUser user = ParseUser.getCurrentUser();
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        final ParseRelation<ParseReference> relation = user.getRelation("references");

        // Send all new local references to server
        List<Reference> localReferences = realm.where(Reference.class).findAll();
        for (Reference reference : localReferences) {
            ParseReference parseReference = ParseReference.from(reference);
            try {
                parseReference.save();
            } catch (ParseException e) {
                continue;
            }
            if (!TextUtils.isEmpty(parseReference.getObjectId())) {

                reference.setParseId(parseReference.getObjectId());
                realm.copyToRealmOrUpdate(reference);
                relation.add(parseReference);
                user.saveEventually();

                final ParseRelation<ParseComment> relationReference = parseReference.getRelation("comments");

                // Save comment for current reference
                List<Comment> localComments = reference.getComments();
                if (localComments != null) {
                    for (Comment comment : localComments) {
                        if (TextUtils.isEmpty(comment.getAuthor())) {
                            comment.setAuthor(user.getUsername());
                        }
                        if (TextUtils.isEmpty(comment.getAuthorUrl())) {
                            comment.setAuthorUrl(user.getString("photo"));
                        }
                        ParseComment parseComment = ParseComment.from(comment);
                        try {
                            parseComment.save();
                        } catch (ParseException e) {
                            continue;
                        }
                        if (!TextUtils.isEmpty(parseComment.getObjectId())) {
                            comment.setParseId(parseComment.getObjectId());
                            realm.copyToRealmOrUpdate(comment);
                            relationReference.add(parseComment);
                            parseReference.saveEventually();
                        }
                    }
                }
            }
        }

        // Get all distant references to replace local
        ParseQuery<ParseReference> query = relation.getQuery();
        try {
            List<ParseReference> parseReferences = query.find();
            if (parseReferences != null) {
                realm.delete(Reference.class);
                realm.delete(Comment.class);
                for (ParseReference parseReference : parseReferences) {
                    Reference reference = ParseReference.toReference(parseReference);

                    // Persist comments
                    final ParseRelation<ParseComment> relationReference = parseReference.getRelation("comments");
                    List<ParseComment> parseComments = relationReference.getQuery().find();
                    if (parseComments != null) {
                        RealmList<Comment> comments = new RealmList<>();
                        for (ParseComment parseComment : parseComments) {
                            final Comment comment = ParseComment.toComment(parseComment);
                            comment.setReference(reference);
                            comments.add(comment);
                        }
                        reference.setComments(comments);
                    }

                    realm.copyToRealmOrUpdate(reference);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        realm.commitTransaction();
        realm.close();

    }

}
