package com.utsc.project;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;

public class Venue {
    public String name = "Loading...";
    public int id = -1;
    public int courts = -1;

    @Exclude
    ArrayList<String> eventTypes = new ArrayList<>();


    public Venue(String name, int id, int courts) {
        this.name = name;
        this.id = id;
        this.courts = courts;
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
    }
}