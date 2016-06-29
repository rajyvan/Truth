package mg.yvan.truth.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import mg.yvan.truth.network.ServiceManager;

/**
 * Created by Yvan on 22/06/16.
 */

public class SynchroService extends IntentService {

    /**
     * Actions
     */
    public final static String ACTION_SYNC = "sync";
    public final static String ACTION_BROADCAST_END = "end";

    private final static String WORKER_THREAD_NAME = "TruthSync";

    public SynchroService() {
        super(WORKER_THREAD_NAME);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();

        switch (action) {
            case ACTION_SYNC:
                ServiceManager.sync();
                Intent localIntent = new Intent(ACTION_BROADCAST_END);
                LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
                break;

            default:
                break;
        }
    }

}
