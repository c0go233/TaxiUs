package com.mad.taxius.journeysearch;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mad.taxius.R;
import com.mad.taxius.model.Journey;
import com.mad.taxius.util.DateConverter;
import com.mad.taxius.util.DistanceConverter;

import java.util.ArrayList;

/**
 * Adapter class that handles the recyclerview of journey object list
 */
public class JourneyAdapter extends RecyclerView.Adapter<JourneyAdapter.ViewHolder> {

    /**
     * ViewHolder class that contains widgets that are used in journey item view
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView departureTimeTv, departureLocationTv, destinationLocationTv,
                departureDistanceTv, destinationDistanceTv;

        private JourneySearchContract.OnJourneyClickListener mJourneyClickListener;

        public ViewHolder(View itemView, JourneySearchContract.OnJourneyClickListener journeyClickListener) {
            super(itemView);
            setViews(itemView);
            this.mJourneyClickListener = journeyClickListener;
            itemView.setOnClickListener(this);
        }

        /**
         * Register the widgets in the journey item view
         *
         * @param itemView
         */
        private void setViews(View itemView) {
            departureTimeTv = (TextView) itemView.findViewById(R.id.item_journey_departure_time_tv);
            departureLocationTv = (TextView) itemView.findViewById(R.id.item_journey_departure_location_tv);
            destinationLocationTv = (TextView) itemView.findViewById(R.id.item_journey_destination_location_tv);
            departureDistanceTv = (TextView) itemView.findViewById(R.id.item_journey_departure_distance_tv);
            destinationDistanceTv = (TextView) itemView.findViewById(R.id.item_journey_destination_distance_tv);
        }

        @Override
        public void onClick(View v) {
            this.mJourneyClickListener.onJourneyItemClicked(mJourneys.get(getAdapterPosition()));
        }
    }

    private Context mContext;
    private ArrayList<Journey> mJourneys;
    private JourneySearchContract.OnJourneyClickListener mJourneyClickListener;

    public JourneyAdapter(Context context, ArrayList<Journey> journeys,
                          JourneySearchContract.OnJourneyClickListener journeyClickListener) {
        this.mContext = context;
        this.mJourneys = journeys;
        this.mJourneyClickListener = journeyClickListener;
    }

    /**
     * Called when its Viewholder is created
     *
     * @param parent   is the parent view group
     * @param viewType is the type of view
     * @return created ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View journeyItemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_journey, parent, false);
        return new ViewHolder(journeyItemView, this.mJourneyClickListener);
    }

    /**
     * Binds the widgets of ViewHolder to actual data to be displayed
     *
     * @param holder   is the viewholder to be bound
     * @param position is the position of viewholder in the recycler view
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Journey journey = mJourneys.get(position);
        holder.departureTimeTv.setText(DateConverter.getTimePortionAsStringFrom(journey.getDepartureTime()));
        holder.departureLocationTv.setText(journey.getDepartureLocation().getName());
        holder.destinationLocationTv.setText(journey.getDestinationLocation().getName());
        holder.departureDistanceTv.setText(DistanceConverter.
                getDistanceAsString(journey.getDepartureLocation().getDistanceDifference()));
        holder.destinationDistanceTv.setText(DistanceConverter.
                getDistanceAsString(journey.getDestinationLocation().getDistanceDifference()));
    }

    /**
     * Get the total item count of message list
     *
     * @return item count of message list
     */
    @Override
    public int getItemCount() {
        return mJourneys.size();
    }
}
