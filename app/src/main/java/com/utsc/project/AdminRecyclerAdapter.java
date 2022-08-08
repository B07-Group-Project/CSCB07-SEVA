package com.utsc.project;

import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class AdminRecyclerAdapter extends RecyclerView.Adapter<AdminRecyclerAdapter.MyViewHolder> {

    private ArrayList<Event> eventsList;
    private RecyclerView rv;

    public AdminRecyclerAdapter(ArrayList<Event> myEvents) {
        this.eventsList = myEvents;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView eventName, creator, startTime, endTime, description, venue, eventType, courtNumber, attendees;
        public ToggleButton join_button;
        public ImageView image;

        public MyViewHolder(final View view) {
            super(view);

            this.eventName = view.findViewById(R.id.eventNameTextView);
            this.creator = view.findViewById(R.id.creatorTextView);
            this.startTime = view.findViewById(R.id.startTimeTextView);
            this.endTime = view.findViewById(R.id.endTimeTextView);
            this.venue = view.findViewById(R.id.venueTextView);
            this.eventType = view.findViewById(R.id.eventTypeTextView);
            this.courtNumber = view.findViewById(R.id.courtNumTextView);
            this.description = view.findViewById(R.id.descriptionTextView);
            this.attendees = view.findViewById(R.id.playerCountTextView);
            this.join_button = view.findViewById(R.id.joinToggleButton);
            this.image = view.findViewById(R.id.eventImageView);

        }
    }

    @NonNull
    @Override
    public AdminRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_list_my_events, parent, false);
        return new MyViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull AdminRecyclerAdapter.MyViewHolder holder, int position) {
        Event currentEvent = eventsList.get(position);
        holder.eventName.setText(currentEvent.name);

        holder.creator.setText("Created by: " + currentEvent.creatorID);

        // sets start and end time
        LocalDateTime start = LocalDateTime.ofInstant(Instant.ofEpochSecond(currentEvent.startTime), ZoneId.systemDefault());
        LocalDateTime end = LocalDateTime.ofInstant(Instant.ofEpochSecond(currentEvent.endTime), ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd, HH:mm");
        holder.startTime.setText("Start time: " + start.format(formatter));
        holder.endTime.setText("End time: " + end.format(formatter));

        holder.description.setText("Description: " + currentEvent.description);
        holder.courtNumber.setText("Court #" + currentEvent.courtNumber);
        holder.eventType.setText("Event type: ");

        Database.listVenues(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot child : snapshot.getChildren()) {
                    Venue v = child.getValue(Venue.class);
                    if (v.id == currentEvent.venueID) {
                        holder.venue.setText("Venue: " + v.name);
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.attendees.setText(currentEvent.getUserCount() + "/" + eventsList.get(position).maxPlayers);
    }

    @Override
    public int getItemCount() {
        if (eventsList != null) {
            return eventsList.size();
        }
        return 0;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView r) {
        rv = r;
    }

}
