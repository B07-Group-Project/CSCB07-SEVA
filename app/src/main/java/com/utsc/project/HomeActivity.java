package com.utsc.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.utsc.project.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {

    public static String venueName;
    public static int venueID;
    ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // default page is "My Events"
        replaceFragment(new VenueDisplayFragment());

        // selects the "My Events" icon
        binding.bottomNavigationView.setSelectedItemId(R.id.upcomingEventsItem);

        // switches fragments depending on which item the user clicked
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.upcomingEventsItem:
                    replaceFragment(new VenueDisplayFragment());
                    break;
                case R.id.myEventsItem:
                    replaceFragment(new MyEventsFragment());
                    break;
                case R.id.createEventItem:
                    replaceFragment(new CreateEventFragment());
                    break;
            }

            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.homeFrameLayout, fragment);
        fragmentTransaction.commit();
    }

    public static void setVenueData(String vName, int vID) {
        venueName = vName;
        venueID = vID;
    }

    public void goBack(View view) {
        replaceFragment(new VenueDisplayFragment());
    }

}