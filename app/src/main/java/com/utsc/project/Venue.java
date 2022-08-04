package com.utsc.project;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;

public class Venue {
    String name;
    int id;
    int courts;

    @Exclude
    ArrayList<String> eventTypes;


    public Venue(String name, int id, int courts) {
        this.name = name;
        this.id = id;
        this.courts = courts;
        this.eventTypes = new ArrayList<>();
    }

    public static Venue getByID(ArrayList<Venue> l, int id) {
        for (int i = 0; i < l.size(); i++) {
            if (l.get(i).id == id) {
                return l.get(i);
            }
        }

        return null;
    }

    public Venue() {
        this.eventTypes = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getCourts() {
        return courts;
    }
}
