package com.utsc.project;

import com.google.firebase.database.Exclude;

import java.util.HashSet;

public class User {
    public String id;
    public String password;



    public User(){}

    public User(String username, String password){
        this.id = username;
        this.password = password;
    }
}
