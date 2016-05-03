package tieorange.com.campuswarsaw;

import android.app.Application;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tieorange.com.campuswarsaw.api.Event;

/**
 * Created by tieorange on 03/05/16.
 */
public class CampusApplication extends Application {
    public static SharedPreferences sharedPreferences;
    public static List<Event> eventsList = new ArrayList<>();
    public static ApiRetro apiRetro;
    public static final String BASE_URL = "https://api.import.io/";

    @Override
    public void onCreate() {
        super.onCreate();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiRetro = retrofit.create(ApiRetro.class);

    }


}
