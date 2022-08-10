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

    ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.userToolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_log_out:
                    Database.setCurrentUser("");
                    Intent intent = new Intent(this, LoginPage.class);
                    startActivity(intent);
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        });

        binding.userToolbar.setTitle("Upcoming Events by Venue");
        replaceFragment(new VenueDisplayFragment());

        // selects the "My Events" icon
        binding.bottomNavigationView.setSelectedItemId(R.id.upcomingEventsItem);

        // switches fragments depending on which item the user clicked
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            binding.userToolbar.setNavigationIcon(null);
            switch (item.getItemId()) {
                case R.id.upcomingEventsItem:
                    binding.userToolbar.setTitle("Upcoming Events by Venue");
                    replaceFragment(new VenueDisplayFragment());
                    break;
                case R.id.myEventsItem:
                    binding.userToolbar.setTitle("My Events");
                    replaceFragment(new MyEventsFragment());
                    break;
                case R.id.createEventItem:
                    binding.userToolbar.setTitle("Create an Event");
                    replaceFragment(new CreateEventFragment());
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