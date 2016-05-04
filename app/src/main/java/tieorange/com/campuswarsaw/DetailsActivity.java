package tieorange.com.campuswarsaw;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.ocpsoft.prettytime.nlp.PrettyTimeParser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

        Intent intent = getIntent();
        int selectedEventPosition = intent.getIntExtra(Intent.EXTRA_UID, 0);
        mEvent = CampusApplication.eventsList.get(selectedEventPosition);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        }

        setTitle(mEvent.date);
        mUiDescription.setText(mEvent.full_description);
        mUiTitle.setText(mEvent.title);
        mUiTime.setText(mEvent.time);

        parseDate();


    }

    private void parseDate() {
        if (mEvent == null || mEvent.date == null || mEvent.time == null) {
            return;
        }

        List<Date> parsedDates = DateTimeParser.Parse(mEvent.time, mEvent.date);
        if (parsedDates != null && parsedDates.size() == 2) {
            Log.d(TAG, "parseDate: from = " + parsedDates.get(0).toString() + " to = " + parsedDates.get(1).toString());
        }
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void animateRevealShow(View viewRoot) {
        int cx = (viewRoot.getLeft() + viewRoot.getRight()) / 2;
        int cy = (viewRoot.getTop() + viewRoot.getBottom()) / 2;
        int finalRadius = Math.max(viewRoot.getWidth(), viewRoot.getHeight());

        Animator anim = ViewAnimationUtils.createCircularReveal(viewRoot, cx, cy, 0, finalRadius);
        viewRoot.setVisibility(View.VISIBLE);
        anim.setDuration(1000);
        anim.setInterpolator(new AccelerateInterpolator());
        anim.start();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setupWindowAnimations() {
        Fade fade = new Fade();
        fade.setDuration(1000);
        getWindow().setEnterTransition(fade);

        Slide slide = new Slide();
        slide.setDuration(1000);
        getWindow().setReturnTransition(slide);
        /*Transition fade = TransitionInflater.from(DetailsActivity.this).inflateTransition(R.transition.transition_fade);
        getWindow().setEnterTransition(fade);*/
    }
}
