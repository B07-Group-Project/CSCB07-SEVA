package com.utsc.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.utsc.project.databinding.ActivityHomeBinding;

public class AdminHomeActivity extends AppCompatActivity {

    ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // default page is "My Events"
        replaceFragment(new VenueDisplayFragment());

        // selects the "My Events" icon
        binding.bottomNavigationView.setSelectedItemId(R.id.admin_upcomingEventsItem);

        // switches fragments depending on which item the user clicked
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.admin_upcomingEventsItem:
                    replaceFragment(new VenueDisplayFragment());
                    break;
                case R.id.admin_addVenueItem:
                    replaceFragment(new AdminAddVenueFragment());
                    break;
            }

            return true;
        });
    }

    public void replaceFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.homeFrameLayout, fragment);
        fragmentTransaction.commit();
    }
}