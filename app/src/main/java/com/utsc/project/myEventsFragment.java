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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link myEventsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class myEventsFragment extends Fragment {

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

    public myEventsFragment() {
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
    public static myEventsFragment newInstance(String param1, String param2) {
        myEventsFragment fragment = new myEventsFragment();
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

        String uid = "DemoUser"; // need to figure out how to get user id

        View view = inflater.inflate(R.layout.fragment_my_events, container, false);
        recyclerView = view.findViewById(R.id.myEventsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        myEvents = new ArrayList<Event>();
        adapter = new RecyclerAdapter(myEvents, uid);
        recyclerView.setAdapter(adapter);

        Database.listEvents(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot eventData : snapshot.getChildren()) { // for all children under Events
                    Event e = eventData.getValue(Event.class);
                    HashSet<User> attendees = new HashSet<User>();
                    boolean joined = false;

                    for (DataSnapshot attendeeChild: snapshot.child(e.getId()+"/attendees").getChildren()) { // for all children under attendees
                        User a = attendeeChild.getValue(User.class);
                        attendees.add(a);

                        if (a.id.equals(uid)) {
                            joined = true;
                        }
                    }

                    if (joined) {
                        e.attendees = attendees;
                        myEvents.add(e);
                    }

                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("warning", "loadPost:onCancelled", error.toException());
            }
        });

        return view;
    }
}