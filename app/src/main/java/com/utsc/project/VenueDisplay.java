package com.utsc.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class VenueDisplay extends AppCompatActivity {

    void addVenueButton(Venue ve) {
        LinearLayout buttonContainer = (LinearLayout) findViewById(R.id.venuelistlayout);
        Context c = this;
        Button b = new Button(this);
        b.setText(ve.name);
        View.OnClickListener l = new View.OnClickListener() {
            public void onClick(View v) {
                Button r = (Button)v;
                Intent intent = new Intent(c, EventDetails.class);
                intent.putExtra("com.utsc.project.VENUENAME", ve.name);
                intent.putExtra("com.utsc.project.VENUEID", ve.id);
                startActivity(intent);
            }
        };
        b.setOnClickListener(l);
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
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Venue v = child.getValue(Venue.class);
                    for (DataSnapshot eType : dataSnapshot.child("eventTypes").getChildren()) {
                        String eventString = eType.getValue(String.class);
                        v.eventTypes.add(eventString);
                        Log.w("warning", eventString);
                    }
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