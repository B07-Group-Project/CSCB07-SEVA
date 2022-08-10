package com.utsc.project;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class EventsByVenueFragment extends Fragment {

    ArrayList<Event> eventList;
    RecyclerView recyclerView;
    RecyclerAdapter adapter;

    private static final String VENUE_NAME = "venue name";
    private static final String VENUE_ID = "venue id";

    private String venueName;
    private int venueID;

    public EventsByVenueFragment() {
        // Required empty public constructor
    }

    public static EventsByVenueFragment newInstance(String venueName, int venueID) {
        EventsByVenueFragment fragment = new EventsByVenueFragment();
        Bundle args = new Bundle();
        args.putString(VENUE_NAME, venueName);
        args.putInt(VENUE_ID, venueID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            venueName = getArguments().getString(VENUE_NAME);
            venueID = getArguments().getInt(VENUE_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upcoming_events, container, false);

        // creates back button to go back to venue display
        Toolbar toolbar = ((HomeActivity) getActivity()).binding.userToolbar;
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbar.setNavigationIcon(null);
                toolbar.setTitle("Upcoming Events by Venue");
                getActivity().onBackPressed();
            }
        });

        recyclerView = view.findViewById(R.id.eventsRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        eventList = new ArrayList<Event>();
        adapter = new RecyclerAdapter(eventList, Database.currentUser);
        recyclerView.setAdapter(adapter);

        ValueEventListener listener = new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                eventList.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Event e = child.getValue(Event.class);

                    if (e.isOver()) {
                        continue;
                    }

                    for (DataSnapshot currentAttendee : child.child("attendees").getChildren()) {
                        User u = currentAttendee.getValue(User.class);
                        if (u != null) {
                            e.addAttendee(u.id);
                        }
                    }
                    if (e.venueID == venueID) {
                        eventList.add(e);
                        Database.loadAttendees(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        }, e.id);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("warning", "loadPost:onCancelled", databaseError.toException());
            }
        };
        Database.listEvents(listener);

        return view;
    }

}