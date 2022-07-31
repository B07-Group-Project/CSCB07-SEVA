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
                Spinner s = findViewById(R.id.venue);
                ArrayList<String> venueList = new ArrayList<>();

                for (DataSnapshot child : snapshot.getChildren()) {
                    Venue v = child.getValue(Venue.class);
                    assert v != null;
                    venueList.add(v.name);
                    for (DataSnapshot eType : snapshot.child("eventTypes").getChildren()) {
                        String eventString = eType.getValue(String.class);
                        v.eventTypes.add(eventString);
                    }
                    self.venues.add(v);
                }

                String [] venueArray =  venueList.toArray(new String[venueList.size()]);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(self, android.R.layout.simple_spinner_item, venueArray);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                s.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("warning", "spinnerpop:onCancelled", error.toException());
            }
        };

        Database.listVenues(spinnerPop);
    }
}