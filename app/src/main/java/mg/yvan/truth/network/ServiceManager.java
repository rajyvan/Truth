package mg.yvan.truth.network;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Yvan on 14/06/16.
 */
public class ServiceManager {

    public final static String BASE_URL = "http://92.222.71.163:3001/truth/";

    private static ServiceManager singleInstance;
    private TruthService mTruthService;

    private ServiceManager() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        mTruthService = retrofit.create(TruthService.class);
    }

    public static ServiceManager getInstance() {
        if (singleInstance == null) {
            singleInstance = new ServiceManager();
        }
        return singleInstance;
    }



}
