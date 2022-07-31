package com.utsc.project;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Database {

    private Database(){
    }
    static void listVenues(ValueEventListener v) {
        DatabaseReference ref = FirebaseDatabase.getInstance("https://b07project-e4016-default-rtdb.firebaseio.com").getReference("Venues");
        ref.addValueEventListener(v);
    }
    static void listEvents(ValueEventListener v) {
        DatabaseReference ref = FirebaseDatabase.getInstance("https://b07project-e4016-default-rtdb.firebaseio.com").getReference("Events");
        ref.addValueEventListener(v);
    }
    static void joinEvent(int id) {
        DatabaseReference ref = FirebaseDatabase.getInstance("https://b07project-e4016-default-rtdb.firebaseio.com").getReference("Events/"+Integer.toString(id) + "/attendees");
        User loggedin = new User("DemoUser"); //get from Login class once its done
        ref.child(loggedin.id).setValue(loggedin);
    }
    static void loadAttendees(ValueEventListener v, int id) {
        DatabaseReference ref = FirebaseDatabase.getInstance("https://b07project-e4016-default-rtdb.firebaseio.com").getReference("Events/"+Integer.toString(id)+"/attendees");
        ref.addValueEventListener(v);
    }
}