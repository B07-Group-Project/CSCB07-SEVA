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

    public Venue() {
    }
}
