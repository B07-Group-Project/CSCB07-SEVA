package com.utsc.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class login_page extends AppCompatActivity {

    private EditText username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        username = (EditText) findViewById(R.id.editTextTextPersonName2);
        password = (EditText) findViewById(R.id.editTextTextPassword);


    }

    public void take_to_register(View view){
        Intent intent = new Intent(this, Register_page.class);
        startActivity(intent);
    }

    public void onClick(View view1){
        if(view1.getId() == R.id.button){
            validate_user();
        }
    }

    private void validate_user() {
        String name = username.getText().toString().trim();
        String pw = password.getText().toString().trim();

        DatabaseReference u_ref =
                FirebaseDatabase.getInstance("https://b07project-e4016-default-rtdb.firebaseio.com"
                ).getReference().child("Users");

        u_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot u_snapshot) {
                for (DataSnapshot snapshot_u : u_snapshot.getChildren()){
                    User registered = snapshot_u.getValue(User.class);
                    if(registered.id.equals(name) && registered.password.equals(pw)){
                        Intent log_in = new Intent(this, HomeActivity.class);
                        startActivity(log_in);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("warning", "loadPost:onCancelled", error.toException());

            }


        });

        username.setError("Invalid username or password!");
        username.requestFocus();
        return;


    }

}