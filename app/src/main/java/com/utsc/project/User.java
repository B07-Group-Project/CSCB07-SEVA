package com.utsc.project;

import com.google.firebase.database.Exclude;

import java.util.HashSet;

public class User {
    String id;
    String password;
    @Exclude
    HashSet<Event> createdEvents;
    @Exclude
    HashSet<Event> joinedEvents;


    public User(){}

    public User(String username, String password){
        this.id = username;
        this.password = password;
    }
}
