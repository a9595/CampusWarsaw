package tieorange.com.campuswarsaw;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import com.anupcowkur.reservoir.Reservoir;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;

import io.fabric.sdk.android.*;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tieorange.com.campuswarsaw.api.Event;

/**
 * Created by tieorange on 03/05/16.
 */
public class CampusApplication extends Application {
    private static final String TAG = CampusApplication.class.getCanonicalName();
    public static SharedPreferences sharedPreferences;
    public static List<Event> eventsList = new ArrayList<>();
    public static ApiRetro apiRetro;
    public static final String BASE_URL = "https://api.import.io/";
    public static final String CACHE_KEY = "CacheKey";

    @Override
    public void onCreate() {
        super.onCreate();
        setupCrashlytics();
        initCache();
        initRetrofit();
    }

    private void setupCrashlytics() {
        if (BuildConfig.USE_CRASHLYTICS == true) {
            CrashlyticsCore core = new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build();
            Crashlytics builder = new Crashlytics.Builder()
                    .core(core)
                    .build();

            Fabric.with(this, builder, new Crashlytics());
            Log.d(TAG, "onCreate: Crashlytics set up");
        }
    }

    private void initCache() {
        try {
            Reservoir.init(this, 20048); //in bytes
        } catch (Exception e) {
            Log.d(TAG, "initCache: FAILED " + e.toString());
        }
    }

    public static void initRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiRetro = retrofit.create(ApiRetro.class);
    }


}
