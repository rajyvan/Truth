package mg.yvan.truth.manager;

import com.facebook.AccessToken;

/**
 * Created by Yvan on 09/06/16.
 */
public class SessionManager {

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

}
