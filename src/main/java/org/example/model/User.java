package org.example.model;

import java.util.ArrayList;

public class User {
    public static User user;
    public final String name;
    public final ArrayList<GameMap> maps;
    
    public User (String name) {
        this.name = name;
        this.maps = new ArrayList<>();
    }
}
