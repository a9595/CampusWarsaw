package tieorange.com.campuswarsaw;

import android.util.Log;

import com.anupcowkur.reservoir.Reservoir;
import com.anupcowkur.reservoir.ReservoirGetCallback;
import com.anupcowkur.reservoir.ReservoirPutCallback;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import tieorange.com.campuswarsaw.api.Event;

/**
 * Created by tieorange on 09/05/16.
 */
public class CacheHelper {
    static void clearCache() {
        try {
            Reservoir.clear();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(MainActivity.TAG, "onResponse: Reservoir.clear() failed");
        }
    }

    static void saveDataToCache(MainActivity activity, final EventsAdapter mAdapter, List<Event> results) {
        // cache:
        Reservoir.putAsync(CampusApplication.CACHE_KEY, results, new ReservoirPutCallback() {
            @Override
            public void onSuccess() {
                Log.d(MainActivity.TAG, "onSuccess: cache saved");
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Exception e) {
                Log.d(MainActivity.TAG, "onFailure: cache saving FAILURE " + e.toString());
            }
        });
    }

    static void putCachedDataToRecyclerView(final MainActivity activity) {
        boolean isCacheExist = false;
        try {
            isCacheExist = Reservoir.contains(CampusApplication.CACHE_KEY);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!isCacheExist) return;

        Type resultType = new TypeToken<List<Event>>() {
        }.getType();
        Reservoir.getAsync(CampusApplication.CACHE_KEY, resultType, new ReservoirGetCallback<List<Event>>() {
            @Override
            public void onSuccess(List<Event> cachedEventsList) {
                if (cachedEventsList.size() >= 1) {
                    activity.updateRecyclerView(cachedEventsList);
                    activity.hideLoadingAndShowRecyclerView();
                }
                Log.d(MainActivity.TAG, "onSuccess: Reservoir.getAsync, size = " + cachedEventsList.size());

            }

            @Override
            public void onFailure(Exception e) {
                Log.d(MainActivity.TAG, "onFailure: Reservoir.getAsync " + e.toString());
            }
        });
    }
}
