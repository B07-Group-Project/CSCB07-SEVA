package com.utsc.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterPage extends AppCompatActivity implements View.OnClickListener {

    private TextView register;
    private EditText username, password, c_password;
    public boolean taken_by_admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        register = (Button) findViewById(R.id.button3);

        username = (EditText) findViewById(R.id.editTextTextPersonName);
        password = (EditText) findViewById(R.id.editTextTextPassword2);
        c_password = (EditText) findViewById(R.id.editTextTextPassword3);

        getSupportActionBar().show();

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button3) {
            register_user();
        }

    }

    private void register_user() {
        taken_by_admin = false;

        String name = username.getText().toString();
        String pw = password.getText().toString();
        String cpw = c_password.getText().toString();

        if(name.isEmpty()){
            username.setError("Username cannot be empty!");
            username.requestFocus();
            return;
        }

        if(pw.isEmpty()){
            password.setError("Password cannot be empty!");
            password.requestFocus();
            return;
        }

        if(!pw.equals(cpw)){
            c_password.setError("Password mismatch");
            c_password.requestFocus();
            return;
        }

        if(password.length() < 6){
            password.setError("Password needs to be at least 6 characters!");
            password.requestFocus();
            return;
        }

        DatabaseReference ref =
                FirebaseDatabase.getInstance("https://b07project-e4016-default-rtdb.firebaseio.com"
                ).getReference();

        DatabaseReference admin_ref = ref.child("Admins");
        admin_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    User admin = child.getValue(User.class);
                    if (admin.id.equals(name)) {
                        username.setError("Username taken!");
                        username.requestFocus();
                        taken_by_admin = true;
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if (taken_by_admin) { // username taken by admin
            return;
        }

        DatabaseReference user_ref = ref.child("Users");
        user_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                for (DataSnapshot snapshot : datasnapshot.getChildren()){
                    User u = (snapshot.getValue(User.class));
                    if(u.id.equals(name)){
                        username.setError("Username taken!");
                        username.requestFocus();
                        return;
                    }
                }

                User user = new User(name, pw);
                ref.child("Users").child(name).setValue(user);

                Database.setCurrentUser(name);
                Intent log_in = new Intent(RegisterPage.this, HomeActivity.class);
                startActivity(log_in);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("warning", "loadPost:onCancelled", error.toException());

            }
        });

    }
}