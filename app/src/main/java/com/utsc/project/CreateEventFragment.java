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
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateEventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateEventFragment extends Fragment {

    ArrayList<Venue> venues = new ArrayList<>();
    ArrayList<String> eventTypes = new ArrayList<>();
    int totalEvents = 0;
    int venueID;
    String eventType;

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
                eventTypes.clear();
                venues.clear();

                // Update spinner
                Spinner venue_spinner = view.findViewById(R.id.create_venue);
                Spinner et_spinner = view.findViewById(R.id.create_eventType);
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
                    venues.add(v);
                }

                String [] venueArray =  venueList.toArray(new String[0]); // https://stackoverflow.com/questions/53284214/toarray-with-pre-sized-array
                String [] evenTypeArray =  eventTypes.toArray(new String[0]);
                ArrayAdapter<String> venueAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, venueArray);
                venueAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ArrayAdapter<String> eventAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, evenTypeArray);
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

        venue_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                venueID = venues.get(i).id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        et_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                eventType = eventTypes.get(i);
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

        return view;
    }

    private void addEventType(String et) {
        if (!eventTypes.contains(et)) {
            eventTypes.add(et);
        }
    }
}