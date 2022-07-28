package com.utsc.project;

import java.util.HashSet;

public class User {
    int id;
    HashSet<Event> createdEvents;
    HashSet<Event> joinedEvents;

    public User(int id) {
        this.id = id;
    }
}
