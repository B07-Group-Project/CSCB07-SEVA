package com.utsc.project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register_page extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private TextView register;
    private EditText username, password, c_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        register = (Button) findViewById(R.id.button3);

        username = (EditText) findViewById(R.id.editTextTextPersonName);
        password = (EditText) findViewById(R.id.editTextTextPassword2);
        c_password = (EditText) findViewById(R.id.editTextTextPassword3);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button3) {
            register_user();
        }

    }

    private void register_user() {
        String name = username.getText().toString().trim();
        String pw = password.getText().toString().trim();
        String cpw = c_password.getText().toString().trim();

        if(name.isEmpty()){
            username.setError("User name cannot be empty!");
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
        User user = new User(name, pw);
        ref.child("Users").child(name).setValue(user);
    }
}