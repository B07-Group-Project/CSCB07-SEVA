package com.utsc.project;

import java.util.HashSet;
import java.util.Objects;

public class User {
    String id;
    HashSet<Event> createdEvents;
    HashSet<Event> joinedEvents;

    public User(String id) {
        this.id = id;
        this.createdEvents = new HashSet<>();
        this.joinedEvents = new HashSet<>();
    }
    public boolean equals(Object o) {
        if (o instanceof User) {
            User u = (User)o;
            return Objects.equals(this.id, u.id);
        } else {
            return false;
        }
    }
}
