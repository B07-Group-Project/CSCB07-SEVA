package com.utsc.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class EventDetails extends AppCompatActivity {

    ArrayList<Event> eventList;
    RecyclerView recyclerView;
    RecyclerAdapter adapter;

    void addEventButton(Event e) {
        ValueEventListener getJoined = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    User u = child.getValue(User.class);
                    if (Objects.equals(u.id, "DemoUser")) { //get USERID from login class
                        adapter.setJoined(e);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("warning", "loadPost:onCancelled", databaseError.toException());
            }
        };
        Database.loadAttendees(getJoined, e.id);
        eventList.add(e);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        recyclerView = findViewById(R.id.eventsRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        eventList = new ArrayList<Event>();
        String uid = "DemoUser";
        adapter = new RecyclerAdapter(eventList, uid);
        recyclerView.setAdapter(adapter);

        Intent intent = getIntent();
        String message = intent.getStringExtra("com.utsc.project.VENUENAME");
        TextView textView = findViewById(R.id.eventName);
        textView.setText("Events for " + message);

    }

    @Override
    protected void onStart() {
        super.onStart();

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                eventList.clear();
                adapter.notifyDataSetChanged();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Event e = child.getValue(Event.class);
                    for (DataSnapshot currentAttendee : child.child("attendees").getChildren()) {
                        User u = currentAttendee.getValue(User.class);
                        if (u == null) {
                            e.addAttendee(u.id);
                        }
                    }
                    Intent intent = getIntent();
                    Integer id = intent.getIntExtra("com.utsc.project.VENUEID", 0);
                    if (e.venueID == id) {
                        addEventButton(e);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("warning", "loadPost:onCancelled", databaseError.toException());
            }
        };
        Database.listEvents(listener);
    }

    public void goBack(View v) {
        Intent intent = new Intent(this, VenueDisplay.class);
        startActivity(intent);
    }
}