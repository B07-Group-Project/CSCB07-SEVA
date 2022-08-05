package com.utsc.project;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventsByVenueFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventsByVenueFragment extends Fragment {

    ArrayList<Event> eventList;
    RecyclerView recyclerView;
    RecyclerAdapter adapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EventsByVenueFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UpcomingEventsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EventsByVenueFragment newInstance(String param1, String param2) {
        EventsByVenueFragment fragment = new EventsByVenueFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upcoming_events, container, false);
        recyclerView = view.findViewById(R.id.eventsRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        eventList = new ArrayList<Event>();
        adapter = new RecyclerAdapter(eventList, Database.currentUser);
        recyclerView.setAdapter(adapter);

        String message = HomeActivity.venueName;
        //TextView textView = view.findViewById(R.id.eventName);
        //textView.setText("Events for " + message);

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                eventList.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Event e = child.getValue(Event.class);

                    for (DataSnapshot currentAttendee : child.child("attendees").getChildren()) {
                        User u = currentAttendee.getValue(User.class);
                        if (u != null) {
                            e.addAttendee(u.id);
                        }
                    }
                    if (e.venueID == HomeActivity.venueID) {
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