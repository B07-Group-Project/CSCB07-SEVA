package com.utsc.project;

public class Event {
    User [] attendees; //should these be private?
    User creator;
    int maxPlayers;
    String startTime; //might change type in future
    String endTime;
    Venue venue;

    public Event(User[] attendees, User creator, int maxPlayers, String startTime, String endTime,
                 Venue venue) {
        this.attendees = attendees;
        this.creator = creator;
        this.maxPlayers = maxPlayers;
        this.startTime = startTime;
        this.endTime = endTime;
        this.venue = venue;
    }

    public int getUserCount() {
        return attendees.length;
    }
}
