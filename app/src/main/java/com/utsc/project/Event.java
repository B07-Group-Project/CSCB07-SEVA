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
    public String startTime; //might change type in future
    public String endTime;

    @Exclude
    public HashSet<User> attendees;

    public int venueID;

    public Event() {
        this.attendees = new HashSet<User>();
    }

    public Event(int id, String creatorID, String name, String description, int maxPlayers,
                 String startTime, String endTime, int venueID) {
        this.id = id;
        this.creatorID = creatorID;
        this.name = name;
        this.description = description;
        this.maxPlayers = maxPlayers;
        this.startTime = startTime;
        this.endTime = endTime;
        this.venueID = venueID;
        this.attendees = new HashSet<>();
    }

    @Exclude
    public int getId() {
        return this.id;
    }
    @Exclude
    public String getName() {
        return this.name;
    }
    @Exclude
    public String getDescription() {
        return this.description;
    }
    @Exclude
    public HashSet<User> getAttendees() {
        return this.attendees;
    }
    @Exclude
    public String getCreatorID() {
        return this.creatorID;
    }
    @Exclude
    public int getMaxPlayers() {
        return this.maxPlayers;
    }
    @Exclude
    public String getStartTime() {
        return this.startTime;
    }
    @Exclude
    public String getEndTime() {
        return this.endTime;
    }
    @Exclude
    public int getVenueID() {
        return this.venueID;
    }

    public void addAttendee(String id) {
        this.attendees.add(new User(id));
    }

    public void removeAttendee(String id) {
        this.attendees.remove(new User(id));
    }

    @Exclude
    public int getUserCount() {
        return attendees.size();
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