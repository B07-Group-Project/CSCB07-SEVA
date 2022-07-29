package com.utsc.project;

import java.util.HashSet;

public class Event {
    int id;
    User creator;
    String name;
    String description;
    int maxPlayers;
    String startTime; //might change type in future
    String endTime;
    HashSet<User> attendees; //should these be private?
    Venue venue;

    public Event(int id, User creator, String name, String description, int maxPlayers,
                 String startTime, String endTime, Venue venue) {
        this.id = id;
        this.creator = creator;
        this.name = name;
        this.description = description;
        this.maxPlayers = maxPlayers;
        this.startTime = startTime;
        this.endTime = endTime;
        this.venue = venue;
        this.attendees = new HashSet<>();
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public HashSet<User> getAttendees() {
        return this.attendees;
    }

    public User getCreator() {
        return this.creator;
    }

    public int getMaxPlayers() {
        return this.maxPlayers;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public String getEndTime() {
        return this.endTime;
    }

    public Venue getVenue() {
        return this.venue;
    }

    public void addAttendee(String id) {
        this.attendees.add(new User(id));
    }

    public void removeAttendee(String id) {
        this.attendees.remove(new User(id));
    }

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
