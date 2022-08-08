package com.utsc.project;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminAddVenueFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminAddVenueFragment extends Fragment {

    ArrayList<String> eTypeList = new ArrayList<>();
    int totalVenues = -1;
    RecyclerView recyclerView;
    SelectEventTypeAdapter adapter;

    
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AdminAddVenueFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddVenueFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminAddVenueFragment newInstance(String param1, String param2) {
        AdminAddVenueFragment fragment = new AdminAddVenueFragment();
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

    public void addEventTypeUniq(String e) {
        if (this.eTypeList.contains(e)) return;
        this.eTypeList.add(e);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_add_venue, container, false);

        recyclerView = view.findViewById(R.id.eventTypeRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        eTypeList = new ArrayList<String>();

        adapter = new SelectEventTypeAdapter(eTypeList);
        recyclerView.setAdapter(adapter);

        // listener for when admin adds a new event type to list
        view.findViewById(R.id.enterNewEventType).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addEventType(getActivity().findViewById(R.id.newEventTypeConstraintLayout));
            }
        });


        AdminAddVenueFragment self = this;

        ValueEventListener eventArrayUpdate = new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                self.eTypeList.clear();
                self.totalVenues = 0;

                for (DataSnapshot venue : snapshot.getChildren()) {
                    for (String eventType : venue.child("eventTypes").getValue(String.class).split(",")) {
                        self.addEventTypeUniq(eventType);
                        self.totalVenues++;
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        Database.listVenues(eventArrayUpdate);

        recyclerView = view.findViewById(R.id.eventTypeRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        adapter = new SelectEventTypeAdapter(eTypeList);
        recyclerView.setAdapter(adapter);

        // listener for when admin adds a new event type to list
        view.findViewById(R.id.enterNewEventType).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addEventType(getActivity().findViewById(R.id.newEventTypeConstraintLayout));
            }
        });

        view.findViewById(R.id.create_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit(getActivity().findViewById(R.id.createPageLinearLayout));
            }
        });


        return view;
    }

    public void addEventType(View v) {
        EditText inputBox = v.findViewById(R.id.addVenue_newEventType);
        String eType = inputBox.getText().toString();

        if (eType.equals("")) {
            inputBox.setError("Cannot be empty.");
            inputBox.requestFocus();
            return;
        }

        if (!adapter.selectedTypes.contains(eType)) {
            adapter.selectedTypes.add(eType);
        }
        if (!adapter.eTypeList.contains(eType)) {
            adapter.eTypeList.add(eType);
        }
        inputBox.setText("");
        adapter.notifyDataSetChanged();
    }

    public void submit(View v) {
        EditText name = v.findViewById(R.id.addVenue_name);
        EditText courts = v.findViewById(R.id.create_courtno);

        // Validation
        if (courts.getText().toString().equals("")) {
            courts.setError("Enter number of courts");
            courts.requestFocus();
            return;
        } else if (Integer.parseInt(courts.getText().toString()) < 0) {
            courts.setError("Enter positive number of courts lol");
            courts.requestFocus();
            return;
        } else if (name.getText().toString().equals("")) {
            name.setError("Enter Venue name");
            name.requestFocus();
            return;
        } else if (this.adapter.eTypeList.size() == 0) {
            Toast.makeText(getContext(), "Enter an event type", Toast.LENGTH_SHORT).show();
            return;
        }

        Venue venue = new Venue(name.getText().toString(), this.totalVenues + 1, Integer.parseInt(courts.getText().toString()));
        venue.eventTypes.addAll(this.adapter.selectedTypes);
        Database.storeVenue(venue);
    }

}