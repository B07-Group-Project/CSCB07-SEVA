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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminVenueDisplayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminVenueDisplayFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AdminVenueDisplayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminVenueDisplayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminVenueDisplayFragment newInstance(String param1, String param2) {
        AdminVenueDisplayFragment fragment = new AdminVenueDisplayFragment();
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

    void addVenueButton(Venue ve, LinearLayout ll) {

        Context c = getActivity();
        Button b = new Button(getActivity());
        b.setText(ve.name);
        View.OnClickListener l = new View.OnClickListener() {
            public void onClick(View v) {
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