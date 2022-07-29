package com.utsc.project;

import java.util.HashSet;
import java.util.Objects;

public class User {
    String id;
    HashSet<Event> createdEvents; //load from db on login?
    HashSet<Event> joinedEvents; //load from db on login?

    public User(String id) {
        this.id = id;
        this.createdEvents = new HashSet<>();
        this.joinedEvents = new HashSet<>();
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
