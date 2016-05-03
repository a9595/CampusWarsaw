package tieorange.com.campuswarsaw;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.LandingAnimator;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tieorange.com.campuswarsaw.api.Event;
import tieorange.com.campuswarsaw.api.Events;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getCanonicalName();

    @Bind(R.id.main_recycler_view)
    RecyclerView mUiRecyclerView;
    private EventsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setupRetrofit();

        mAdapter = new EventsAdapter(CampusApplication.eventsList);
        mUiRecyclerView.setHasFixedSize(true);
        mUiRecyclerView.setItemAnimator(new LandingAnimator());

        mUiRecyclerView.setAdapter(mAdapter);
        mUiRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

    }

    private void setupRetrofit() {
        // setup Retrofit:
        Call<Events> call = CampusApplication.apiRetro.getEvents();
        call.enqueue(new Callback<Events>() {
            @Override
            public void onResponse(Call<Events> call, Response<Events> response) {
                if (response == null) return;

                List<Event> results = response.body().results;
                for (int i = 0; i < results.size(); i++) {
                    Event result = results.get(i);
                    CampusApplication.eventsList.add(result);
                    mAdapter.notifyItemInserted(i);
                }
//                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<Events> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.toString());
                Toast.makeText(MainActivity.this, "Try to reconnect to Internet", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
