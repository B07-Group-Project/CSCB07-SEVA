package com.utsc.project;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
public class Database {

    public static User currentUser = new User("default");

    private Database(){
    }
    static void listVenues(ValueEventListener v) {
        DatabaseReference ref = FirebaseDatabase.getInstance("https://b07project-e4016-default-rtdb.firebaseio.com").getReference("Venues");
        ref.addValueEventListener(v);
    }

    static void showEventNumber(ValueEventListener v) {
        DatabaseReference ref = FirebaseDatabase.getInstance("https://b07project-e4016-default-rtdb.firebaseio.com").getReference("EventNumber");
        ref.addValueEventListener(v);
    }

    static  void writeEventNumber(int i) {
        DatabaseReference ref = FirebaseDatabase.getInstance("https://b07project-e4016-default-rtdb.firebaseio.com").getReference("EventNumber");
        ref.setValue(i);
    }

    static void storeEvent(Event e) {
        DatabaseReference ref = FirebaseDatabase.getInstance("https://b07project-e4016-default-rtdb.firebaseio.com").getReference("Events");
        String key = Integer.toString(e.id);
        ref.child(key).setValue(e);
        int i = 0;
        for (User u : e.attendees) {
            ref.child(key).child("attendees").child(Integer.toString(i)).setValue(u);
            i++;
        }
    }
}
