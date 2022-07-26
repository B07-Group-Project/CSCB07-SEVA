package com.utsc.project;


import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.firebase.database.Exclude;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;

public class Event implements Comparable<Event>{

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
    public String eventType;

    public Event() {
        this.attendees = new HashSet<>();
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
        this.eventType = eventType;
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

    @Override
    public int compareTo(Event event) { // compares based on start time
        if (this.startTime == event.startTime) {
            return 0;
        }
        else if (this.startTime < event.startTime) {
            return -1;
        }
        return 1;
    }

    @Exclude
    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean isOver() {
        LocalDateTime current = LocalDateTime.now();
        LocalDateTime scheduledEnd = LocalDateTime.ofInstant(Instant.ofEpochSecond(this.endTime), ZoneId.systemDefault());

        return scheduledEnd.isBefore(current);
    }
}