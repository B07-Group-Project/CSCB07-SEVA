package com.utsc.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LoginPage extends AppCompatActivity {

    private EditText username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        username = (EditText) findViewById(R.id.editTextTextPersonName2);
        password = (EditText) findViewById(R.id.editTextTextPassword);

        getSupportActionBar().hide();

    }

    public void take_to_register(View view) {
        Intent intent = new Intent(this, RegisterPage.class);
        startActivity(intent);
    }

    public void onClickLogin(View view1) {
        if (view1.getId() == R.id.upcomingEventsBackButton) {
            validate_user();
        }
    }

    private void validate_user() {

        String name = username.getText().toString();
        String pw = password.getText().toString();

        DatabaseReference a_ref =
                FirebaseDatabase.getInstance("https://b07project-e4016-default-rtdb.firebaseio.com"
                ).getReference().child("Admins");

        a_ref.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot a_snapshot) {
                for (DataSnapshot snapshot_a : a_snapshot.getChildren()) {
                    User admin = snapshot_a.getValue(User.class);
                    if (admin.id.equals(name) && admin.password.equals(pw)) {
                        Intent alogin = new Intent(LoginPage.this,
                                AdminHomeActivity.class);
                        startActivity(alogin);
                        finish();
                        return;
                    }
                    else if (admin.id.equals(name)) {
                        username.setError("Invalid username or password!");
                        username.requestFocus();
                        return;
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error_a) {
                Log.w("warning", "loadPost:onCancelled", error_a.toException());

            }
        });

        DatabaseReference u_ref =
                FirebaseDatabase.getInstance("https://b07project-e4016-default-rtdb.firebaseio.com"
                ).getReference().child("Users");

        // when logged in store user object in current user public static current_user()
        u_ref.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot u_snapshot) {
                for (DataSnapshot snapshot_u : u_snapshot.getChildren()) {
                    User registered = snapshot_u.getValue(User.class);
                    if (registered.id.equals(name) && registered.password.equals(pw)) {
                        Database.setCurrentUser(name);
                        Intent log_in = new Intent(LoginPage.this, HomeActivity.class);
                        startActivity(log_in);
                        finish();
                    }
                    else if (registered.id.equals(name)) {
                        username.setError("Invalid username or password!");
                        username.requestFocus();
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("warning", "loadPost:onCancelled", error.toException());

            }

        });

    }

}