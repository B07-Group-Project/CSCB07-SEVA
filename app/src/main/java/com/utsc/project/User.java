package com.utsc.project;

public class User {
    public String id;

    public User(String id) {
        this.id = id;
    }

    public User(){
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