package com.utsc.project;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminEventsByVenueFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminEventsByVenueFragment extends Fragment {

    ArrayList<Event> eventList;
    RecyclerView recyclerView;
    AdminRecyclerAdapter adapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String VENUE_NAME = "venue name";
    private static final String VENUE_ID = "venue id";

    // TODO: Rename and change types of parameters
    private String venueName;
    private int venueID;

    public AdminEventsByVenueFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param venueName Parameter 1.
     * @param venueID Parameter 2.
     * @return A new instance of fragment UpcomingEventsFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        recyclerView = view.findViewById(R.id.eventsRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        eventList = new ArrayList<Event>();
        adapter = new AdminRecyclerAdapter(eventList);
        recyclerView.setAdapter(adapter);

        //String message = HomeActivity.venueName;
        //TextView textView = view.findViewById(R.id.eventName);
        //textView.setText("Events for " + message);

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
                Collections.sort(eventList);
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