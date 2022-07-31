package com.utsc.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashSet;

public class DisplayMyEventsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference db;
    RecyclerAdapter adapter;
    ArrayList<Event> myEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_my_events);

        String uid = "DemoUser";

        recyclerView = findViewById(R.id.myEventsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        myEvents = new ArrayList<Event>();
        adapter = new RecyclerAdapter(myEvents);
        recyclerView.setAdapter(adapter);

        Database.listEvents(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot eventData : snapshot.getChildren()) { // for all children under Events
                    Event e = eventData.getValue(Event.class);
                    HashSet<User> attendees = new HashSet<User>();
                    boolean joined = false;

                    for (DataSnapshot attendeeChild: snapshot.child(e.getId()+"/attendees").getChildren()) { // for all children under attendees
                        User a = attendeeChild.getValue(User.class);
                        attendees.add(a);

                        if (a.id.equals(uid)) {
                            joined = true;
                        }
                    }

                    if (joined) {
                        e.attendees = attendees;
                        myEvents.add(e);
                    }

                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("warning", "loadPost:onCancelled", error.toException());
            }
        });

    }
}