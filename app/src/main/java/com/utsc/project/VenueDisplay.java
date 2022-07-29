package com.utsc.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class VenueDisplay extends AppCompatActivity {

    void addVenueButton(Venue v) {
        LinearLayout buttonContainer = (LinearLayout) findViewById(R.id.venuelistlayout);
        Button b = new Button(this);
        b.setText(v.name);
        buttonContainer.addView(b);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue_display);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Button b = new Button(this);
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Venue v = child.getValue(Venue.class);
                    addVenueButton(v);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("warning", "loadPost:onCancelled", databaseError.toException());
            }
        };
        Database.listVenues(listener);
    }
}