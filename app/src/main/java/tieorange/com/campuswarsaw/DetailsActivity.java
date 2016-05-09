package tieorange.com.campuswarsaw;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import tieorange.com.campuswarsaw.api.Event;

public class DetailsActivity extends AppCompatActivity {
    private static final String TAG = DetailsActivity.class.getCanonicalName();
    @Bind(R.id.details_description)
    public TextView mUiDescription;
    @Bind(R.id.details_title)
    public TextView mUiTitle;
    @Bind(R.id.details_time)
    public TextView mUiTime;
    @Bind(R.id.details_relative_layout)
    public RelativeLayout mUiRelativeLayout;
    private Event mEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
//        setupWindowAnimations();
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getExtras();
        setupFAB();

        setTitle(mEvent.time);
        mUiDescription.setText(mEvent.full_description);
        mUiTitle.setText(mEvent.title);
        mUiTime.setText(mEvent.date);
    }

    private void setupFAB() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    createCalendarEvent();
                }
            });
        }
    }

    private void openEventLink() {
        String url = mEvent.link;
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    private void getExtras() {
        Intent intent = getIntent();
        int selectedEventPosition = intent.getIntExtra(Intent.EXTRA_UID, 0);
        mEvent = CampusApplication.eventsList.get(selectedEventPosition);
    }

    private List<Date> getParsedDates() {
        List<Date> parsedDatesList = new ArrayList<>();
        if (mEvent == null || mEvent.date == null || mEvent.time == null) {
            return parsedDatesList;
        }

        parsedDatesList = DateTimeParser.Parse(mEvent.time, mEvent.date);
        if (parsedDatesList != null && parsedDatesList.size() == 2) {
            Log.d(TAG, "getParsedDates: from = " + parsedDatesList.get(0).toString() + " to = " + parsedDatesList.get(1).toString());
        }
        return parsedDatesList;
    }

    public void onClickAddToCalendar(View view) {

    }

    private void createCalendarEvent() {
        List<Date> parsedDatesList = getParsedDates();
        if (parsedDatesList == null || parsedDatesList.size() != 2) {
            Log.d(TAG, "onClickAddToCalendar: parsedDatesList is NULL or empty. Size = " + parsedDatesList.size());
            return;
        }

        Calendar calStart = Calendar.getInstance();
        Calendar calEnd = Calendar.getInstance();
        calStart.setTime(parsedDatesList.get(0));
        calEnd.setTime(parsedDatesList.get(1));

        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, calStart.getTimeInMillis());
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, calEnd.getTimeInMillis());
        intent.putExtra(CalendarContract.Events.TITLE, mEvent.title);
        intent.putExtra(CalendarContract.Events.DESCRIPTION, mEvent.full_description);
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, getString(R.string.campus_warsaw_address));
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // This is the up button
            case R.id.action_open_event_link:
                openEventLink();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }
}
