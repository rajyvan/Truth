package mg.yvan.truth;

import android.app.Application;
import android.content.Context;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import java.io.IOException;

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
    }

}
