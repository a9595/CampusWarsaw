package tieorange.com.campuswarsaw;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.ocpsoft.prettytime.nlp.PrettyTimeParser;

import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.LandingAnimator;
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
//        setupWindowAnimations();
        ButterKnife.bind(this);


        setupRetrofit();

        setupRecyclerView();



    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setupWindowAnimations() {
        /*Fade fade = new Fade();
        fade.setDuration(1000);
        getWindow().setExitTransition(fade);

        Slide slide = new Slide();
        slide.setDuration(1000);
        getWindow().setReturnTransition(slide);*/
        /*Transition slide = TransitionInflater.from(MainActivity.this).inflateTransition(R.transition.transition_slide);
        getWindow().setExitTransition(slide);*/
    }

    private void setupRecyclerView() {
        mAdapter = new EventsAdapter(CampusApplication.eventsList);
        mUiRecyclerView.setHasFixedSize(true);
        mUiRecyclerView.setItemAnimator(new LandingAnimator());

        mUiRecyclerView.setAdapter(mAdapter);
        mUiRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        ItemClickSupport.addTo(mUiRecyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Intent i = new Intent(MainActivity.this, DetailsActivity.class);

                View viewTitle = v.findViewById(R.id.row_title);
                String transitionTitle = getString(R.string.transition_title);
                ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, viewTitle, transitionTitle);


                i.putExtra(Intent.EXTRA_UID, position);
//                startActivity(i, transitionActivityOptions.toBundle());
                startActivity(i);
            }
        });
    }

    private void setupRetrofit() {
        // setup Retrofit:
        if (CampusApplication.apiRetro == null) {
            final String BASE_URL = "https://api.import.io/";
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            CampusApplication.apiRetro = retrofit.create(ApiRetro.class);
        }
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
            }

            @Override
            public void onFailure(Call<Events> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.toString());
                Toast.makeText(MainActivity.this, "Try to reconnect to Internet", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
