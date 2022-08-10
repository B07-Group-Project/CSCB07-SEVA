package com.utsc.project;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Objects;

public class CreateEventFragment extends Fragment {

    ArrayList<Venue> venues = new ArrayList<>();
    int totalEvents = 0;
    int venueID;
    int courtNo;
    String eventType;

    public CreateEventFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_create_event, container, false);

        // Add event listeners for both spinners since they communicate with firebase to get their option content

        ValueEventListener spinnerPop = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Reset Arrays
                venues.clear();

                // Update spinner
                Spinner venue_spinner = view.findViewById(R.id.create_venue);
                ArrayList<String> venueList = new ArrayList<>();

                for (DataSnapshot child : snapshot.getChildren()) {
                    Venue v = child.getValue(Venue.class);
                    assert v != null;
                    for (String eType : child.child("eventTypes").getValue(String.class).split(",")) {
                        v.eventTypes.add(eType);
                    }
                    venueList.add(v.name);
                    venues.add(v);
                }

                String [] venueArray =  venueList.toArray(new String[0]); // https://stackoverflow.com/questions/53284214/toarray-with-pre-sized-array
                ArrayAdapter<String> venueAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, venueArray);
                venueAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                venue_spinner.setAdapter(venueAdapter);
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
                totalEvents = snapshot.getValue(Integer.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        Database.showEventNumber(eventNumberUpdate);

        // Add event listener for Spinner values

        Spinner venue_spinner = view.findViewById(R.id.create_venue);
        Spinner et_spinner = view.findViewById(R.id.create_eventType);
        Spinner court_spinner = view.findViewById(R.id.create_courtno);

        venue_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Venue v = venues.get(i);
                venueID = v.id;

                // Populate Event Type spinner values

                String [] evenTypeArray = v.eventTypes.toArray(new String[0]);
                ArrayAdapter<String> eventAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, evenTypeArray);
                eventAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                et_spinner.setAdapter(eventAdapter);

                // Populate Court Spinner

                String [] courtArray = new String[v.courts];
                for (int j = 1; j <= v.courts; j++) {
                    courtArray[j-1] = Integer.toString(j);
                }
                ArrayAdapter<String> courtAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, courtArray);
                courtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                court_spinner.setAdapter(courtAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        et_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                eventType = Objects.requireNonNull(Venue.getByID(venues, venueID)).eventTypes.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        court_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                courtNo = i + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        // Set default values of DatePickers to today, not epoch

        LocalDateTime d = LocalDateTime.now();
        DatePicker startDate = view.findViewById(R.id.create_startdate);
        DatePicker endDate = view.findViewById(R.id.create_enddate);

        startDate.updateDate(d.getYear(), d.getMonth().getValue() - 1, d.getDayOfMonth());
        endDate.updateDate(d.getYear(), d.getMonth().getValue() - 1, d.getDayOfMonth());

        // Similarly, set default of timepickers

        TimePicker startTime = view.findViewById(R.id.create_starttime);
        TimePicker endTime = view.findViewById(R.id.create_endtime);

        startTime.setHour(d.getHour() + 1);
        startTime.setMinute(0);
        endTime.setHour(d.getHour() + 2);
        endTime.setMinute(0);

        view.findViewById(R.id.create_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit(getActivity().findViewById(R.id.createPageLinearLayout));
            }
        });
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void submit(View v) {
        EditText name = v.findViewById(R.id.create_name);
        EditText desc = v.findViewById(R.id.create_description);
        EditText maxplayer = v.findViewById(R.id.create_maxplayers);
        TimePicker startTime = v.findViewById(R.id.create_starttime);
        TimePicker endTime = v.findViewById(R.id.create_endtime);
        DatePicker startDate = v.findViewById(R.id.create_startdate);
        DatePicker endDate = v.findViewById(R.id.create_enddate);

        Button s = v.findViewById(R.id.create_submit);

        // Calculate startdate
        long SD = LocalDateTime.of(startDate.getYear(), startDate.getMonth() + 1, startDate.getDayOfMonth(), startTime.getHour(), startTime.getMinute()).atZone(ZoneId.systemDefault()).toEpochSecond();

        // Calculate enddate
        long ED = LocalDateTime.of(endDate.getYear(), endDate.getMonth() + 1, endDate.getDayOfMonth(), endTime.getHour(), endTime.getMinute()).atZone(ZoneId.systemDefault()).toEpochSecond();

        // Verification
        Venue venObj = Venue.getByID(this.venues, this.venueID);
        assert venObj != null;
        if (name.getText().toString().equals("")) {
            name.setError("Name cannot be empty.");
            name.requestFocus();
            return;
        } else if (maxplayer.getText().toString().equals("")) {
            maxplayer.setError("Max players cannot be empty.");
            maxplayer.requestFocus();
            return;
        } else if (SD >= ED) {
            Toast.makeText(getContext(), "Start time must be before end time", Toast.LENGTH_SHORT).show();
            return;
        }

        Event e = new Event(totalEvents + 1, Database.currentUser, name.getText().toString(), desc.getText().toString(), Integer.parseInt(maxplayer.getText().toString()),
                SD, ED, this.venueID, this.eventType,
                this.courtNo);

        e.attendees.add(new User(Database.currentUser));

        Database.storeEvent(e);
        Database.writeEventNumber(totalEvents + 1);

        // Disable submit button
        s.setText(R.string.create_event_submitted_text);
        s.setEnabled(false);

    }
}