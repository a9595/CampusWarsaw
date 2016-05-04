package tieorange.com.campuswarsaw;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import com.anupcowkur.reservoir.Reservoir;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

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
        initCache();
        initRetrofit();
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
