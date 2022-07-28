package com.utsc.project;

import java.util.HashSet;

public class Event {
    int id;
    HashSet<User> attendees; //should these be private?
    User creator;
    int maxPlayers;
    String startTime; //might change type in future
    String endTime;
    Venue venue;

    public Event(User creator, int maxPlayers, String startTime, String endTime,
                 Venue venue, int id) {
        this.creator = creator;
        this.maxPlayers = maxPlayers;
        this.startTime = startTime;
        this.endTime = endTime;
        this.venue = venue;
        this.id = id;
        this.attendees = new HashSet<>();
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
