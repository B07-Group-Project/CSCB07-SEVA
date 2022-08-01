package com.utsc.project;

import java.util.Objects;

public class User {
    public String id;

    public User(String id) {
        this.id = id;
    }

    public User(){

    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof User) {
            User u = (User)o;
            return u.id.equals(this.id);
        } else {
            return false;
        }
    }
}