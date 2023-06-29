package org.example.model;

import java.util.ArrayList;
import java.util.Objects;

public class User {
    public static User user;
    public final String name;
    public final ArrayList<GameMap> maps;
    
    public User (String name) {
        this.name = name;
        this.maps = new ArrayList<>();
    }
    
    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return name.equals(user.name) && maps.equals(user.maps);
    }
    
    @Override
    public int hashCode () {
        return Objects.hash(name, maps);
    }
}
