package org.example.model;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Objects;

public class User {
    private final String name;
    private ArrayList<GameMap> customMaps;
    private final ArrayList<String> receivedMapsNames = new ArrayList<>();
    
    public User (String name) {
        this.name = name;
        this.setCustomMaps(new ArrayList<>());
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
        return getName().equals(user.getName()) && getCustomMaps().equals(user.getCustomMaps());
    }
    
    @Override
    public int hashCode () {
        return Objects.hash(getName(), getCustomMaps());
    }
    
    public String toJson () {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
    
    public GameMap getMapByName (String name) {
        for (GameMap map : getCustomMaps()) {
            if (map.name.equals(name)) return map;
        }
        return null;
    }
    
    private String getName () {
        return name;
    }
    
    public ArrayList<GameMap> getCustomMaps () {
        return customMaps;
    }
    
    private void setCustomMaps (ArrayList<GameMap> customMaps) {
        this.customMaps = customMaps;
    }
    
    public ArrayList<String> getReceivedMapsNames () {
        return receivedMapsNames;
    }
}
