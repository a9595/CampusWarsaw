package tieorange.com.campuswarsaw;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
    private List<Date> mParsedDatedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
//        setupWindowAnimations();
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        int selectedEventPosition = intent.getIntExtra(Intent.EXTRA_UID, 0);
        mEvent = CampusApplication.eventsList.get(selectedEventPosition);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url = mEvent.link;
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            });
        }

        setTitle(mEvent.time);
        mUiDescription.setText(mEvent.full_description);
        mUiTitle.setText(mEvent.title);
        mUiTime.setText(mEvent.date);

        parseDate();


    }

    private void parseDate() {
        if (mEvent == null || mEvent.date == null || mEvent.time == null) {
            return;
        }

        mParsedDatedList = DateTimeParser.Parse(mEvent.time, mEvent.date);
        if (mParsedDatedList != null && mParsedDatedList.size() == 2) {
            Log.d(TAG, "parseDate: from = " + mParsedDatedList.get(0).toString() + " to = " + mParsedDatedList.get(1).toString());
        }
    }


    public void onClickAddToCalendar(View view) {
        if (mParsedDatedList == null || mParsedDatedList.size() != 2)
            return;

        Calendar calStart = Calendar.getInstance();
        Calendar calEnd = Calendar.getInstance();
        calStart.setTime(mParsedDatedList.get(0));
        calEnd.setTime(mParsedDatedList.get(1));

        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, calStart.getTimeInMillis());
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, calEnd.getTimeInMillis());
        intent.putExtra(CalendarContract.Events.TITLE, mEvent.title);
        intent.putExtra(CalendarContract.Events.DESCRIPTION, mEvent.full_description);
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, "Ząbkowska 27/31, 03-736 Warszawa");
        startActivity(intent);

    }

    public void forceCrash(View view) {
        throw new RuntimeException("This is a crash");
    }



}
