package com.utsc.project;

import android.annotation.SuppressLint;
import android.app.usage.UsageEvents;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class VenueDisplayFragment extends Fragment {

    private ArrayList<Venue> venueList;

    public VenueDisplayFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    void addVenueButton(Venue ve, LinearLayout ll) {

        Button b = new Button(getActivity());
        b.setText(ve.name);
        View.OnClickListener l = new View.OnClickListener() {
            public void onClick(View v) {
                ((HomeActivity) getActivity()).binding.userToolbar.setTitle("Events at " + ve.name);

                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.homeFrameLayout, EventsByVenueFragment.newInstance(ve.name, ve.id));
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        };
        b.setOnClickListener(l);
        ll.addView(b);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.venueList = new ArrayList<Venue>();

        View view = inflater.inflate(R.layout.fragment_venue_display, container, false);
        LinearLayout ll = (LinearLayout) view.findViewById(R.id.venuelistlayout);

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                venueList.clear();
                ll.removeAllViews();

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Venue v = child.getValue(Venue.class);
                    if (v != null && child.child("eventTypes").getValue(String.class) != null) {
                        for (String eventString : child.child("eventTypes").getValue(String.class).split(",")) {
                            v.eventTypes.add(eventString);
                        }
                        venueList.add(v);
                    }
                }


                Collections.sort(venueList);
                for (Venue v : venueList) {
                    addVenueButton(v, ll);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("warning", "loadPost:onCancelled", databaseError.toException());
            }
        };
        Database.listVenues(listener);

        return view;
    }

}