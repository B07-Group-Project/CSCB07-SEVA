package com.utsc.project;

import com.google.firebase.database.Exclude;

import java.util.HashSet;

import java.util.Objects;

public class User {
    public String id;
    public String password;

    public User(String id) {
        this.id = id;
    }

    public User() {}

    public User(String username, String password){
        this.id = username;
        this.password = password;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof User) {
            User u = (User)o;
            return u.id.equals(this.id);
        } else {
            return false;
        }
    }
}

