package com.utsc.project;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateEventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateEventFragment extends Fragment {

    ArrayList<Venue> venues = new ArrayList<>();
    int totalEvents = 0;
    int venueID;
    int courtno;
    EventType eventType;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CreateEventFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment createEventFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateEventFragment newInstance(String param1, String param2) {
        CreateEventFragment fragment = new CreateEventFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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
                    venueList.add(v.name);
                    for (DataSnapshot eType : child.child("eventTypes").getChildren()) {
                        EventType eventType = eType.getValue(EventType.class);
                        assert eventType != null;
                        v.eventTypes.add(eventType);
                    }
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

                EventType [] evenTypeArray = v.eventTypes.toArray(new EventType[0]);
                ArrayAdapter<EventType> eventAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, evenTypeArray);
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
                courtno = i + 1;
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

        startTime.setHour(d.getHour());
        startTime.setMinute(d.getMinute());
        endTime.setHour(d.getHour());
        endTime.setMinute(d.getMinute());

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
        long SD = LocalDateTime.of(startDate.getYear(), startDate.getMonth(), startDate.getDayOfMonth(), startTime.getHour(), startTime.getMinute()).atZone(ZoneId.systemDefault()).toEpochSecond();

        // Calculate enddate
        long ED = LocalDateTime.of(endDate.getYear(), endDate.getMonth(), endDate.getDayOfMonth(), endTime.getHour(), endTime.getMinute()).atZone(ZoneId.systemDefault()).toEpochSecond();

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
        }

        Event e = new Event(totalEvents + 1, Database.currentUser, name.getText().toString(), desc.getText().toString(), Integer.parseInt(maxplayer.getText().toString()),
                SD, ED, this.venueID, this.eventType,
                this.courtno);

        e.attendees.add(new User(Database.currentUser));

        Database.storeEvent(e);
        Database.writeEventNumber(totalEvents + 1);

        // Disable submit button
        s.setText(R.string.create_event_submitted_text);
        s.setEnabled(false);

    }
}