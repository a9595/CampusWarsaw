package tieorange.com.campuswarsaw;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.anupcowkur.reservoir.Reservoir;
import com.anupcowkur.reservoir.ReservoirGetCallback;
import com.anupcowkur.reservoir.ReservoirPutCallback;
import com.github.glomadrian.loadingballs.BallView;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tieorange.com.campuswarsaw.api.Event;
import tieorange.com.campuswarsaw.api.Events;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getCanonicalName();

    @Bind(R.id.main_recycler_view)
    RecyclerView mUiRecyclerView;
    @Bind(R.id.main_loading_animation)
    BallView mUiLoadingAnimation;
    private EventsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        setupWindowAnimations();
        ButterKnife.bind(this);
        setupRecyclerView();

        getCache();
        setupRetrofit();

        Log.d(TAG, "onCreate: listSize = " + CampusApplication.eventsList.size());
    }

    private void getCache() {
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
                    mUiLoadingAnimation.setVisibility(View.GONE);
                    mUiRecyclerView.setVisibility(View.VISIBLE);
                    updateRecyclerView(cachedEventsList);
                }
                Log.d(TAG, "onSuccess: Reservoir.getAsync, size = " + cachedEventsList.size());

            }

            @Override
            public void onFailure(Exception e) {
                Log.d(TAG, "onFailure: Reservoir.getAsync " + e.toString());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
//        getCache();
//        if (CampusApplication.eventsList == null || CampusApplication.eventsList.size() <= 2) {
//            setupRetrofit();
//        }
        Log.d(TAG, "onResume: listSize = " + CampusApplication.eventsList.size());
    }

    private void setupRecyclerView() {
        mAdapter = new EventsAdapter(CampusApplication.eventsList);

        mUiRecyclerView.setAdapter(mAdapter);
        mUiRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        ItemClickSupport.addTo(mUiRecyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Intent i = new Intent(MainActivity.this, DetailsActivity.class);
                i.putExtra(Intent.EXTRA_UID, position);
                startActivity(i);
            }
        });
    }

    private void setupRetrofit() {
        // setup Retrofit:
        if (CampusApplication.apiRetro == null) {
            Log.d(TAG, "setupRetrofit: CampusApplication.apiRetro is NULL");
            CampusApplication.initRetrofit();
        }
        Call<Events> call = CampusApplication.apiRetro.getEvents();
        call.enqueue(new Callback<Events>() {
            @Override
            public void onResponse(Call<Events> call, Response<Events> response) {
                if (response == null) return;
                List<Event> results = response.body().results;
                if (results.size() < 2) {
                    setupRetrofit();
                    return;
                }
                updateRecyclerView(results);

                mUiLoadingAnimation.setVisibility(View.GONE);
                mUiRecyclerView.setVisibility(View.VISIBLE);

                try {
                    Reservoir.clear(); // clear cache
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, "onResponse: Reservoir.clear() failed");
                }

                // cache:
                Reservoir.putAsync(CampusApplication.CACHE_KEY, results, new ReservoirPutCallback() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "onSuccess: cache saved");
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d(TAG, "onFailure: cache saving FAILURE " + e.toString());
                    }
                });

            }

            @Override
            public void onFailure(Call<Events> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.toString());
                Toast.makeText(MainActivity.this, "Try to reconnect to Internet", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateRecyclerView(List<Event> results) {
        if (results.size() <= 2) return;

        CampusApplication.eventsList.clear();
        for (int i = 0; i < results.size(); i++) {
            Event result = results.get(i);
            CampusApplication.eventsList.add(result);
//            mAdapter.notifyItemInserted(i);
        }
        mAdapter.notifyDataSetChanged();
    }
}
