package com.utsc.project;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.List;

public class Venue {
    public String name;
    public int id;
    public int courts;

    @Exclude
    List<String> eventTypes = new ArrayList<String>();


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