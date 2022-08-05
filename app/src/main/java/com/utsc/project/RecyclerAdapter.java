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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private ArrayList<Event> eventsList;
    private RecyclerView rv;
    String uid;
    ArrayList<RecyclerAdapter.MyViewHolder> viewHolders;

    public RecyclerAdapter(ArrayList<Event> myEvents, String uid) {
        this.eventsList = myEvents;
        this.uid = uid;
        this.viewHolders = new ArrayList<MyViewHolder>();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView eventName, creator, startTime, endTime, description, venue, courtNumber, attendees;
        public ToggleButton join_button;
        public ImageView image;

        public MyViewHolder(final View view) {
            super(view);

            this.eventName = view.findViewById(R.id.eventNameTextView);
            this.creator = view.findViewById(R.id.creatorTextView);
            this.startTime = view.findViewById(R.id.startTimeTextView);
            this.endTime = view.findViewById(R.id.endTimeTextView);
            this.venue = view.findViewById(R.id.venueTextView);
            this.courtNumber = view.findViewById(R.id.courtNumTextView);
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.MyViewHolder holder, int position) {
        Event currentEvent = eventsList.get(position);
        holder.eventName.setText(currentEvent.name);

        // sets creator
        if (currentEvent.creatorID.equals(this.uid)) {
            holder.creator.setText("Created by: Me");
        }
        else {
            holder.creator.setText("Created by: " + currentEvent.creatorID);
        }

        // sets start and end time
        LocalDateTime start = LocalDateTime.ofInstant(Instant.ofEpochSecond(currentEvent.startTime), ZoneId.systemDefault());
        LocalDateTime end = LocalDateTime.ofInstant(Instant.ofEpochSecond(currentEvent.endTime), ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd, HH:mm");
        holder.startTime.setText("Start: " + start.format(formatter));
        holder.endTime.setText("End: " + end.format(formatter));

        holder.description.setText("Description: " + currentEvent.description);
        holder.courtNumber.setText("Court #" + currentEvent.courtNumber);

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
        if (this.uid.equals(currentEvent.creatorID)) {
            holder.join_button.setChecked(true);
        }
        else {
            holder.join_button.setChecked(currentEvent.isAttendee(new User(this.uid)));
        }

        if (holder.join_button.isChecked()) {
            holder.join_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (currentEvent.creatorID.equals(Database.currentUser)) {
                        holder.join_button.setChecked(true);
                        Toast.makeText(rv.getContext(), "You cannot leave your own event.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Database.leaveEvent(currentEvent.id);
                        holder.join_button.setChecked(false);
                    }
                }
            });
        }
        else {
            holder.join_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (currentEvent.getUserCount() >= currentEvent.maxPlayers) {
                        holder.join_button.setChecked(false);
                        Toast.makeText(rv.getContext(), "This event is full!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Database.joinEvent(currentEvent.id);
                        holder.join_button.setChecked(true);
                    }
                }
            });
        }

        viewHolders.add(holder);
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
