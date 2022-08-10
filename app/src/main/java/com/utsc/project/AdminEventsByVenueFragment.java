package com.utsc.project;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class AdminEventsByVenueFragment extends Fragment {

    ArrayList<Event> eventList;
    RecyclerView recyclerView;
    AdminRecyclerAdapter adapter;

    private static final String VENUE_NAME = "venue name";
    private static final String VENUE_ID = "venue id";

    private String venueName;
    private int venueID;

    public AdminEventsByVenueFragment() {
        // Required empty public constructor
    }

    public static AdminEventsByVenueFragment newInstance(String venueName, int venueID) {
        AdminEventsByVenueFragment fragment = new AdminEventsByVenueFragment();
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
        View view = inflater.inflate(R.layout.fragment_admin_upcoming_events, container, false);

        // creates back button to go back to venue display
        Toolbar toolbar = ((AdminHomeActivity) getActivity()).binding.adminToolbar;
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbar.setNavigationIcon(null);
                toolbar.setTitle("Upcoming Events by Venue");
                getActivity().onBackPressed();
            }
        });

        recyclerView = view.findViewById(R.id.admineventsrecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        eventList = new ArrayList<Event>();
        adapter = new AdminRecyclerAdapter(eventList);
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

                // checks when to display the no events message
                TextView tv = view.findViewById(R.id.adminVenueNoEvents);
                if (eventList.isEmpty()) {
                    tv.setVisibility(View.VISIBLE);
                }
                else {
                    tv.setVisibility(View.GONE);
                    Collections.sort(eventList);
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