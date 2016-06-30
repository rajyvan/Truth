package mg.yvan.truth;

import android.app.Application;
import android.content.Context;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.io.IOException;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import mg.yvan.truth.models.parse.ParseComment;
import mg.yvan.truth.models.parse.ParseReference;
import mg.yvan.truth.models.parse.ParseVerse;
import mg.yvan.truth.provider.BibleDbOpenHelper;

/**
 * Created by Yvan on 30/05/16.
 */
public class TruthApplication extends Application {

    private static Context appContext;

    public static Context getAppContext() {
        return appContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        try {
            new BibleDbOpenHelper(this).createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ParseObject.registerSubclass(ParseVerse.class);
        ParseObject.registerSubclass(ParseComment.class);
        ParseObject.registerSubclass(ParseReference.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(Defines.PARSE_APP_ID)
                .clientKey(null)
                .server(Defines.PARSE_SERVER_URL)
                .build()
        );
        Parse.setLogLevel(Parse.LOG_LEVEL_VERBOSE);

        ParseFacebookUtils.initialize(this);

        RealmConfiguration configuration = new RealmConfiguration.Builder(this)
                .schemaVersion(1)
                .build();
        Realm.setDefaultConfiguration(configuration);
    }

}
