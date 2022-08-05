package com.utsc.project;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyEventsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyEventsFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerAdapter adapter;
    ArrayList<Event> myEvents;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyEventsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment myEventsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyEventsFragment newInstance(String param1, String param2) {
        MyEventsFragment fragment = new MyEventsFragment();
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

        View view = inflater.inflate(R.layout.fragment_my_events, container, false);
        recyclerView = view.findViewById(R.id.myEventsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        myEvents = new ArrayList<Event>();
        adapter = new RecyclerAdapter(myEvents, Database.currentUser);
        recyclerView.setAdapter(adapter);

        ValueEventListener l = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myEvents.clear();
                for (DataSnapshot eventData : snapshot.getChildren()) { // for all children under Events
                    Event e = eventData.getValue(Event.class);

                    boolean joined = false;

                    for (DataSnapshot attendeeChild : snapshot.child(e.id + "/attendees").getChildren()) { // for all children under attendees
                        User u = attendeeChild.getValue(User.class);
                        if (u != null) {
                            e.addAttendee(u.id);
                        }

                        if (u.id.equals(Database.currentUser)) {
                            joined = true;
                        }
                    }

                    if (joined) {
                        addEventButton(e);
                    }

                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("warning", "loadPost:onCancelled", error.toException());
            }
        };

        Database.listEvents(l);

        return view;
    }

    void addEventButton(Event e) {
        ValueEventListener getJoined = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    User u = child.getValue(User.class);
                    if (u.id.equals(Database.currentUser)) {
                        e.addAttendee(u.id);
                        adapter.setJoined(e);
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
        myEvents.add(e);
        adapter.notifyDataSetChanged();
    }

}