package com.utsc.project;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashSet;

public class Venue {
    String name;
    int id;
    int courts;

    @Exclude
    HashSet<String> eventTypes;


    public Venue(String name, int id, int courts) {
        this.name = name;
        this.id = id;
        this.courts = courts;
        this.eventTypes = new HashSet<>();
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
        this.eventTypes = new HashSet<>();
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
