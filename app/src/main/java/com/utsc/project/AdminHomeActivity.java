package com.utsc.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.utsc.project.databinding.ActivityAdminHomeBinding;
import com.utsc.project.databinding.ActivityHomeBinding;

public class AdminHomeActivity extends AppCompatActivity {

    ActivityAdminHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        binding = ActivityAdminHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.adminToolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_log_out:
                    Intent intent = new Intent(this, LoginPage.class);
                    startActivity(intent);
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        });

        binding.adminToolbar.setTitle("Upcoming Events by Venue");

        // default page is Venue Filter Display
        replaceFragment(new AdminVenueDisplayFragment());

        // selects the "Upcoming Events" icon
        binding.adminNavBarView.setSelectedItemId(R.id.admin_upcomingEventsItem);

        // switches fragments depending on which item the user clicked
        binding.adminNavBarView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.admin_upcomingEventsItem:
                    binding.adminToolbar.setTitle("Upcoming Events by Venue");
                    replaceFragment(new AdminVenueDisplayFragment());
                    break;
                case R.id.admin_addVenueItem:
                    binding.adminToolbar.setTitle("Add a Venue");
                    replaceFragment(new AdminAddVenueFragment());
                    break;
            }

            return true;
        });
    }

    public void replaceFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.admin_homeFrameLayout, fragment);
        fragmentTransaction.commit();
    }

}