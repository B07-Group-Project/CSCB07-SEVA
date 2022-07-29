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
    public String creatorid;
    public String name;
    public String description;
    public int maxPlayers;
    public String startTime; //might change type in future
    public String endTime;

    @Exclude
    public HashSet<User> attendees;

    public int venueid;

    public Event(int id, String creatorid, String name, String description, int maxPlayers,
                 String startTime, String endTime, int venueid) {
        this.id = id;
        this.creatorid = creatorid;
        this.name = name;
        this.description = description;
        this.maxPlayers = maxPlayers;
        this.startTime = startTime;
        this.endTime = endTime;
        this.venueid = venueid;
        this.attendees = new HashSet<>();
    }

    public Event() {
    }

    public void addAttendee(String id) {
        this.attendees.add(new User(id));
    }

    /*public void loadAttendeesFromDB() { //can add callback param
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    User u = child.getValue(User.class);
                    addAttendee(u.id);
                    Log.w("warning", u.id);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("warning", "loadPost:onCancelled", databaseError.toException());
            }
        };
        Database.loadAttendees(listener, this.id);
    }*/

    @Exclude
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
