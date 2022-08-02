package com.utsc.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;

public class CreateEvent extends AppCompatActivity {
    HashSet<Venue> venues = new HashSet<>();
    HashSet<String> eventTypes = new HashSet<>();
    int totalEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        CreateEvent self = this;

        ValueEventListener spinnerPop = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Update spinner
                Spinner venue_spinner = findViewById(R.id.venue);
                Spinner et_spinner = findViewById(R.id.eventType);
                ArrayList<String> venueList = new ArrayList<>();

                for (DataSnapshot child : snapshot.getChildren()) {
                    Venue v = child.getValue(Venue.class);
                    assert v != null;
                    venueList.add(v.name);
                    for (DataSnapshot eType : child.child("eventTypes").getChildren()) {
                        EventType eventType = eType.getValue(EventType.class);
                        assert eventType != null;
                        v.eventTypes.add(eventType.name);
                        eventTypes.add(eventType.name);
                        Log.w("warning", eventType.name);
                    }
                    self.venues.add(v);
                }

                String [] venueArray =  venueList.toArray(new String[venueList.size()]);
                String [] evenTypeArray =  eventTypes.toArray(new String[eventTypes.size()]);
                ArrayAdapter<String> venueAdapter = new ArrayAdapter<>(self, android.R.layout.simple_spinner_item, venueArray);
                venueAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ArrayAdapter<String> eventAdapter = new ArrayAdapter<>(self, android.R.layout.simple_spinner_item, evenTypeArray);
                eventAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                venue_spinner.setAdapter(venueAdapter);
                et_spinner.setAdapter(eventAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("warning", "spinnerpop:onCancelled", error.toException());
            }
        };

        Database.listVenues(spinnerPop);
    }
}