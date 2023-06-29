package org.example.model;

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
}
