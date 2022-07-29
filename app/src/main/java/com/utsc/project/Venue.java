package com.utsc.project;

public class Venue {
    String name;
    int id;
    int courts;

    public Venue(String name, int id, int courts) {
        this.name = name;
        this.id = id;
        this.courts = courts;
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
