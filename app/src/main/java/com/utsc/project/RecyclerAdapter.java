package com.utsc.project;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private ArrayList<Event> eventsList;
    private RecyclerView rv;
    String uid;

    public RecyclerAdapter(ArrayList<Event> myEvents, String uid) {
        this.eventsList = myEvents;
        this.uid = uid;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView eventName, creator, dateTime, description, venue, attendees;
        private ToggleButton join_button;
        private ImageView image;

        public MyViewHolder(final View view) {
            super(view);

            this.eventName = view.findViewById(R.id.eventNameTextView);
            this.creator = view.findViewById(R.id.creatorTextView);
            this.dateTime = view.findViewById(R.id.dateTimeTextView);
            this.venue = view.findViewById(R.id.venueTextView);
            this.description = view.findViewById(R.id.descriptionTextView);
            this.attendees = view.findViewById(R.id.playerCountTextView);
            this.join_button = view.findViewById(R.id.joinToggleButton);
            this.image = view.findViewById(R.id.eventImageView);

        }
    }

    @NonNull
    @Override
    public RecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_my_events, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.MyViewHolder holder, int position) {
        Event currentEvent = eventsList.get(position);
        holder.eventName.setText(currentEvent.getName());

        if (currentEvent.getCreatorID().equals(this.uid)) {
            holder.creator.setText("Created by: Me");
        }
        else {
            holder.creator.setText("Created by: " + currentEvent.getCreatorID());
        }

        // need to update date and time display
        holder.dateTime.setText(currentEvent.getStartTime() + " to " + currentEvent.getEndTime());

        holder.description.setText(currentEvent.getDescription());
        holder.venue.setText("Venue: " + currentEvent.getVenueID());
        holder.attendees.setText(currentEvent.getUserCount() + "/" + eventsList.get(position).getMaxPlayers());

        if (this.uid.equals(currentEvent.creatorID)) {
            holder.join_button.setChecked(true);
        }
        else {
            holder.join_button.setChecked(currentEvent.isAttendee(new User(this.uid)));
        }

        View.OnClickListener l = new View.OnClickListener() {
            public void onClick(View v) {
                Database.joinEvent(currentEvent.id);
            }
        };

        holder.join_button.setOnClickListener(l);
    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView r) {
        rv = r;
    }

    public void setJoined(Event e) {
        if (eventsList.contains(e)) {
            MyViewHolder vh = (MyViewHolder) rv.findViewHolderForAdapterPosition(eventsList.indexOf(e));
            vh.join_button.setChecked(true);
            View.OnClickListener l = new View.OnClickListener() {
                public void onClick(View v) {
                    ToggleButton me = (ToggleButton) v;
                    if (!uid.equals(e.creatorID)) {
                        Database.leaveEvent(e.id);
                        me.setChecked(false);
                        View.OnClickListener l2 = new View.OnClickListener() {
                            public void onClick(View v) {
                                Database.joinEvent(e.id);
                            }
                        };

                        me.setOnClickListener(l2);
                    }
                    else {
                        me.setChecked(true);
                        Toast.makeText(rv.getContext(), "You cannot leave your own event.", Toast.LENGTH_SHORT).show();
                    }
                }
            };
            vh.join_button.setOnClickListener(l);
        }
    }

}