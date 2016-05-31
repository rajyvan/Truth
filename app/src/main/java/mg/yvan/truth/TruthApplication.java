package mg.yvan.truth;

import android.app.Application;
import android.content.Context;

import com.facebook.FacebookSdk;

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
    }

}
