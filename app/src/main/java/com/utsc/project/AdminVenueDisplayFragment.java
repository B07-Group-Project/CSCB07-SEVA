package com.utsc.project;

import android.app.usage.UsageEvents;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

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

public class AdminVenueDisplayFragment extends Fragment {

    public AdminVenueDisplayFragment() {
        // Required empty public constructor
    }

    public static AdminVenueDisplayFragment newInstance() {
        return new AdminVenueDisplayFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    void addVenueButton(Venue ve, LinearLayout ll) {

        Context c = getActivity();
        Button b = new Button(getActivity());
        b.setText(ve.name);
        View.OnClickListener l = new View.OnClickListener() {
            public void onClick(View v) {
                ((AdminHomeActivity) getActivity()).binding.adminToolbar.setTitle("Events at " + ve.name);

                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.admin_homeFrameLayout, AdminEventsByVenueFragment.newInstance(ve.name, ve.id));
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

        View view = inflater.inflate(R.layout.fragment_admin_venue_display, container, false);
        LinearLayout ll = (LinearLayout) view.findViewById(R.id.venuelistlayout);

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Venue v = child.getValue(Venue.class);
                    assert v != null;
                    for (String eventString : child.child("eventTypes").getValue(String.class).split(",")) {
                        v.eventTypes.add(eventString);
                    }
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