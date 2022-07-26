package com.utsc.project;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;

public class Database {

    public static String currentUser;

    private Database(){
    }

    static void setCurrentUser(String userID) {
        currentUser = userID;
    }

    static void listVenues(ValueEventListener v) {
        DatabaseReference ref = FirebaseDatabase.getInstance("https://b07project-e4016-default-rtdb.firebaseio.com").getReference("Venues");
        ref.addValueEventListener(v);
    }

    static void showEventNumber(ValueEventListener v) {
        DatabaseReference ref = FirebaseDatabase.getInstance("https://b07project-e4016-default-rtdb.firebaseio.com").getReference("EventNumber");
        ref.addValueEventListener(v);
    }

    static void writeEventNumber(int i) {
        DatabaseReference ref = FirebaseDatabase.getInstance("https://b07project-e4016-default-rtdb.firebaseio.com").getReference("EventNumber");
        ref.setValue(i);
    }

    static void storeEvent(Event e) {
        DatabaseReference ref = FirebaseDatabase.getInstance("https://b07project-e4016-default-rtdb.firebaseio.com").getReference("Events");
        String key = Integer.toString(e.id);
        ref.child(key).setValue(e);
        for (User u : e.attendees) {
            ref.child(key).child("attendees").child(u.id).setValue(u);
        }
    }
    static void listEvents(ValueEventListener v) {
        DatabaseReference ref = FirebaseDatabase.getInstance("https://b07project-e4016-default-rtdb.firebaseio.com").getReference("Events");
        ref.addValueEventListener(v);
    }
    static void joinEvent(int id) {
        DatabaseReference ref = FirebaseDatabase.getInstance("https://b07project-e4016-default-rtdb.firebaseio.com").getReference("Events/"+Integer.toString(id) + "/attendees");
        User loggedin = new User(currentUser); //get from Login class once its done
        ref.child(loggedin.id).setValue(loggedin);
    }
    static void leaveEvent(int id) {
        DatabaseReference ref = FirebaseDatabase.getInstance("https://b07project-e4016-default-rtdb.firebaseio.com").getReference("Events/"+Integer.toString(id) + "/attendees");
        User loggedin = new User(currentUser); //get from Login class once its done
        ref.child(loggedin.id).removeValue();
    }
    static void loadAttendees(ValueEventListener v, int id) {
        DatabaseReference ref = FirebaseDatabase.getInstance("https://b07project-e4016-default-rtdb.firebaseio.com").getReference("Events/"+Integer.toString(id)+"/attendees");
        ref.addValueEventListener(v);
    }

    static void storeVenue(Venue v) {
        DatabaseReference ref = FirebaseDatabase.getInstance("https://b07project-e4016-default-rtdb.firebaseio.com").getReference("Venues");
        ref.child(Integer.toString(v.id)).setValue(v);
        Collections.sort(v.eventTypes);
        // Store eventTypes
        StringBuilder etstring = new StringBuilder();
        for (String et : v.eventTypes) {
            etstring.append(et).append(",");
        }
        ref.child(Integer.toString(v.id)).child("eventTypes").setValue(etstring.toString());
    }
}
