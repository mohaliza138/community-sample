package org.example.model;

import com.google.gson.Gson;

import java.util.Objects;

public class GameMap {
    public final String name;
    
    public GameMap (String name, User user) {
        this.name = name;
    }
    
    public GameMap (GameMap gameMap, User user) {
        this.name = gameMap.name;
    }
    
    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameMap gameMap = (GameMap) o;
        return name.equals(gameMap.name);
    }
    
    @Override
    public int hashCode () {
        return Objects.hash(name);
    }
    
    public String toJson () {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
    
    public static GameMap jsonToGameMap (String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, GameMap.class);
    }
}
