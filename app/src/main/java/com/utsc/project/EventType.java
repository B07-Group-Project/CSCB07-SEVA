package com.utsc.project;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class EventType {
    public String name;
    public int iconId;

    public static EventType findByName(ArrayList<EventType> a, String name) {
        EventType curr;
        for (int i = 0; i < a.size(); i++) {
            curr = a.get(i);
            if (curr.name.equals(name)) {
                return curr;
            }
        }

        return null;
    }

    @NonNull
    @Override
    public String toString() {
        return this.name;
    }
}
