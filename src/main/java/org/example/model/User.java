package org.example.model;

import com.google.gson.Gson;

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
    
    public static User jsonToUser (String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, User.class);
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
    
    public String toJson () {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
    
    public GameMap getMapByName (String name) {
        for (GameMap map : maps) {
            if (map.name.equals(name)) return map;
        }
        return null;
    }
}
