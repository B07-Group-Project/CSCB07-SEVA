package com.utsc.project;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminAddVenueFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminAddVenueFragment extends Fragment {

    ArrayList<String> eTypeList;
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

}