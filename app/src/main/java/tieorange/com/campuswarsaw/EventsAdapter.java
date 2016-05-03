package tieorange.com.campuswarsaw;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import tieorange.com.campuswarsaw.api.Event;
import tieorange.com.campuswarsaw.api.Events;

/**
 * Created by tieorange on 03/05/16.
 */
public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {
    private List<Event> mEventsList;

    public EventsAdapter(List<Event> eventsList) {
        this.mEventsList = eventsList;
    }

    @Override
    public EventsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View eventView = inflater.inflate(R.layout.list_raw, parent, false);

        EventsAdapter.ViewHolder viewHolder = new ViewHolder(eventView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(EventsAdapter.ViewHolder holder, int position) {
        Event event = mEventsList.get(position);

        holder.mUiTitle.setText(event.title);
        holder.mUiDate.setText(event.date);
        holder.mUiTime.setText(event.time);
        holder.mUiDescription.setText(event.short_description);
    }

    @Override
    public int getItemCount() {
        return mEventsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.row_title)
        public TextView mUiTitle;
        @Bind(R.id.row_date)
        public TextView mUiDate;
        @Bind(R.id.row_time)
        public TextView mUiTime;
        @Bind(R.id.row_description)
        public TextView mUiDescription;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
