package tieorange.com.campuswarsaw;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.crashlytics.android.answers.CustomEvent;
import com.github.glomadrian.loadingballs.BallView;

import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.schedulers.Schedulers;
import tieorange.com.campuswarsaw.api.Event;
import tieorange.com.campuswarsaw.api.Events;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getCanonicalName();

    @Bind(R.id.main_recycler_view)
    RecyclerView mUiRecyclerView;
    @Bind(R.id.main_loading_animation)
    BallView mUiLoadingAnimation;
    private EventsAdapter mAdapter;
    private Subscription mSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setupRecyclerView();

        CacheHelper.putCachedDataToRecyclerView(this);
//        setupRetrofit();
        setupRx();

        Log.d(TAG, "onCreate: listSize = " + CampusApplication.eventsList.size());
    }

    @Nullable
    private Events getEvents() throws IOException {
        Call<Events> call = CampusApplication.apiRetro.getEvents();
        Response<Events> response = call.execute();
        if (response.isSuccessful()) {
            Events retrievedEvents = response.body();
            return retrievedEvents;
        }
        return null;
    }

    public Observable<Events> getEventsObservable() {
        return Observable.defer(new Func0<Observable<Events>>() {
            @Override
            public Observable<Events> call() {
                try {
                    return Observable.just(getEvents());
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        });
    }

    public void setupRx() {
        mSubscription = getEventsObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Events>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: " + e.getMessage());
                    }

                    @Override
                    public void onNext(Events events) {
                        List<Event> results = events.results;
                        afterDataDownloaded(results);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSubscription != null && mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        putCachedDataToRecyclerView();
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
                metricEventSelection(position);

                Intent i = new Intent(MainActivity.this, DetailsActivity.class);
                i.putExtra(Intent.EXTRA_UID, position);
                startActivity(i);
            }
        });
    }

    private void metricEventSelection(int position) {
        final Event selectedEvent = CampusApplication.eventsList.get(position);
        Answers.getInstance().logCustom(new CustomEvent("Event selected from the list")
                .putCustomAttribute("Event title", selectedEvent.title)
                .putCustomAttribute("DateTime", selectedEvent.date + " " + selectedEvent.time));
    }

    private void afterDataDownloaded(List<Event> results) {
        updateRecyclerView(results);
        hideLoadingAndShowRecyclerView();
        CacheHelper.clearCache();
        CacheHelper.saveDataToCache(this, mAdapter, results);
    }


    void hideLoadingAndShowRecyclerView() {
        mUiLoadingAnimation.setVisibility(View.GONE);
        mUiRecyclerView.setVisibility(View.VISIBLE);
    }

    void updateRecyclerView(List<Event> results) {
        if (results.size() <= 2) return;
        CampusApplication.eventsList.clear();

        for (int i = 0, resultsSize = results.size(); i < resultsSize; i++) {
            Event result = results.get(i);
            CampusApplication.eventsList.add(result);
//            mAdapter.notifyItemInserted(i);
        }
        mAdapter.notifyDataSetChanged();
    }
}
