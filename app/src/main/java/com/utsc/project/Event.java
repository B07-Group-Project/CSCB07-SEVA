package com.utsc.project;


import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Event {

    public int id;
    public String creatorID;
    public String name;
    public String description;
    public int maxPlayers;
    public long startTime;
    public long endTime;
    @Exclude
    HashSet<User> attendees;
    public int venueID;
    public int courtNumber;

    public Event() {
        this.attendees = new HashSet<User>();
    }

    public Event(int id, String creatorID, String name, String description, int maxPlayers,
                 long startTime, long endTime, int venueID, String eventType, int courtNumber) {
        this.id = id;
        this.creatorID = creatorID;
        this.name = name;
        this.description = description;
        this.maxPlayers = maxPlayers;
        this.startTime = startTime;
        this.endTime = endTime;
        this.venueID = venueID;
        this.attendees = new HashSet<>();
        this.courtNumber = courtNumber;
    }

    public void addAttendee(String id) {
        this.attendees.add(new User(id));
    }

    public void removeAttendee(String id) {
        this.attendees.remove(new User(id));
    }

    public boolean isAttendee(User u) {
        return this.attendees.contains(u);
    }

    @Exclude
    public int getUserCount() {
        return attendees.size();
    }

    @Override
    public int hashCode() {
        return this.id;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Event) {
            Event e = (Event)o;
            return e.id == this.id;
        } else {
            return false;
        }
    }
}