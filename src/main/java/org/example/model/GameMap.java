package org.example.model;

import java.util.Objects;

public class GameMap {
    public final String name;
    public final User user;
    
    public GameMap (String name, User user) {
        this.name = name;
        this.user = user;
    }
    
    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameMap gameMap = (GameMap) o;
        return name.equals(gameMap.name) && user.equals(gameMap.user);
    }
    
    @Override
    public int hashCode () {
        return Objects.hash(name, user);
    }
}
