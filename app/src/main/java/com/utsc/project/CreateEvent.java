package com.utsc.project;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

public class CreateEvent extends AppCompatActivity {
    ArrayList<Venue> venues = new ArrayList<>();
    ArrayList<String> eventTypes = new ArrayList<>();
    int totalEvents = 0;
    int venueID;
    String eventType;

    private void addEventType(String et) {
        if (!eventTypes.contains(et)) {
            eventTypes.add(et);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        CreateEvent self = this;

        // Add event listeners for both spinners since they communicate with firebase to get their option content

        ValueEventListener spinnerPop = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Reset Arrays
                self.eventTypes.clear();
                self.venues.clear();

                // Update spinner
                Spinner venue_spinner = findViewById(R.id.create_venue);
                Spinner et_spinner = findViewById(R.id.create_eventType);
                ArrayList<String> venueList = new ArrayList<>();

                for (DataSnapshot child : snapshot.getChildren()) {
                    Venue v = child.getValue(Venue.class);
                    assert v != null;
                    venueList.add(v.name);
                    for (DataSnapshot eType : child.child("eventTypes").getChildren()) {
                        EventType eventType = eType.getValue(EventType.class);
                        assert eventType != null;
                        v.eventTypes.add(eventType.name);
                        addEventType(eventType.name);
                    }
                    self.venues.add(v);
                }

                String [] venueArray =  venueList.toArray(new String[0]); // https://stackoverflow.com/questions/53284214/toarray-with-pre-sized-array
                String [] evenTypeArray =  eventTypes.toArray(new String[0]);
                ArrayAdapter<String> venueAdapter = new ArrayAdapter<>(self, android.R.layout.simple_spinner_item, venueArray);
                venueAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ArrayAdapter<String> eventAdapter = new ArrayAdapter<>(self, android.R.layout.simple_spinner_item, evenTypeArray);
                eventAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                venue_spinner.setAdapter(venueAdapter);
                et_spinner.setAdapter(eventAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("warning", "spinnerPop:onCancelled", error.toException());
            }
        };
        Database.listVenues(spinnerPop);

        // Event Listener to track number of events
        ValueEventListener eventNumberUpdate = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                self.totalEvents = snapshot.getValue(Integer.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        Database.showEventNumber(eventNumberUpdate);

        // Add event listener for Spinner values

        Spinner venue_spinner = findViewById(R.id.create_venue);
        Spinner et_spinner = findViewById(R.id.create_eventType);

        venue_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                self.venueID = self.venues.get(i).id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        et_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                self.eventType = self.eventTypes.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        // Set default values of DatePickers to today, not epoch

        LocalDateTime d = LocalDateTime.now();
        DatePicker startDate = findViewById(R.id.create_startdate);
        DatePicker endDate = findViewById(R.id.create_enddate);

        startDate.updateDate(d.getYear(), d.getMonth().getValue() - 1, d.getDayOfMonth());
        endDate.updateDate(d.getYear(), d.getMonth().getValue() - 1, d.getDayOfMonth());

        // Similarly, set default of timepickers

        TimePicker startTime = findViewById(R.id.create_starttime);
        TimePicker endTime = findViewById(R.id.create_endtime);

        startTime.setHour(d.getHour());
        startTime.setMinute(d.getMinute());
        endTime.setHour(d.getHour());
        endTime.setMinute(d.getMinute());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void submit(View v) {
        EditText name = findViewById(R.id.create_name);
        EditText desc = findViewById(R.id.create_description);
        EditText court = findViewById(R.id.create_courtno);
        EditText maxplayer = findViewById(R.id.create_maxplayers);
        TimePicker startTime = findViewById(R.id.create_starttime);
        TimePicker endTime = findViewById(R.id.create_endtime);
        DatePicker startDate = findViewById(R.id.create_startdate);
        DatePicker endDate = findViewById(R.id.create_enddate);

        Button s = findViewById(R.id.create_submit);

        // Calculate startdate
        long SD = LocalDateTime.of(startDate.getYear(), startDate.getMonth(), startDate.getDayOfMonth(), startTime.getHour(), startTime.getMinute()).atZone(ZoneId.systemDefault()).toEpochSecond();

        // Calculate enddate
        long ED = LocalDateTime.of(endDate.getYear(), endDate.getMonth(), endDate.getDayOfMonth(), endTime.getHour(), endTime.getMinute()).atZone(ZoneId.systemDefault()).toEpochSecond();

        // Verification
        Venue venObj = Venue.getByID(this.venues, this.venueID);
        assert venObj != null;
        int courtNum = Integer.parseInt(court.getText().toString());
        String buttonMessage = "";
        if (name.getText().toString().equals("")) {
            buttonMessage = "Fill in name";
        } else if (maxplayer.getText().toString().equals("")) {
            buttonMessage = "Enter max players";
        } else if (court.getText().toString().equals("")) {
            buttonMessage = "Enter court number";
        } else if (courtNum > venObj.courts || courtNum < 0){
            buttonMessage = "Invalid Court Number";
        }

        if (!buttonMessage.equals("")) {
            s.setText(buttonMessage);
            return;
        }

        Event e = new Event(totalEvents + 1, Database.currentUser, name.getText().toString(), desc.getText().toString(), Integer.parseInt(maxplayer.getText().toString()),
                SD, ED, this.venueID, this.eventType,
                Integer.parseInt(court.getText().toString()));

        e.attendees.add(new User(Database.currentUser));

        Database.storeEvent(e);
        Database.writeEventNumber(totalEvents + 1);

        // Disable submit button
        s.setText(R.string.create_event_submitted_text);
        s.setEnabled(false);

    }
}