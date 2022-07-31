package com.utsc.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class EventDetails extends AppCompatActivity {
    void addEventButton(Event e) {
        Button join = new Button(this);
        join.setBackgroundColor(Color.rgb(104, 4, 236));
        join.setTextColor(Color.WHITE);
        join.setText("Join");
        ValueEventListener getJoined = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    User u = child.getValue(User.class);
                    if (Objects.equals(u.id, "DemoUser")) { //get USERID from login class
                        join.setText("Joined");
                        join.setEnabled(false);
                        join.setBackgroundColor(Color.rgb(62, 2, 142));
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

        LinearLayout buttonContainer = (LinearLayout) findViewById(R.id.eventlistlayout);
        Button b = new Button(this);
        b.setText(e.name);
        Context c = this;
        Button desc = new Button(this);
        desc.setText(e.description);
        buttonContainer.addView(b);
        buttonContainer.addView(desc);
        View.OnClickListener l = new View.OnClickListener() {
            public void onClick(View v) {
                Database.joinEvent(e.id);
                Button conv = (Button)v;
                conv.setText("Joined");
                conv.setEnabled(false);
                conv.setBackgroundColor(Color.rgb(62, 2, 142));
            }
        };
        join.setOnClickListener(l);
        buttonContainer.addView(join);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

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
                LinearLayout buttonContainer = (LinearLayout) findViewById(R.id.eventlistlayout);
                buttonContainer.removeAllViews();
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