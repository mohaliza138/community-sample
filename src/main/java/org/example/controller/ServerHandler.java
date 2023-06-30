package org.example.controller;

import org.example.model.GameMap;
import org.example.model.Messages;
import org.example.model.User;

import java.util.ArrayList;

public class ServerHandler {
    public static ServerHandler serverHandler = new ServerHandler();
    private final ArrayList<User> users = new ArrayList<>();
    private final ArrayList<GameMap> maps = new ArrayList<>();
    
    private boolean isTheMapAdded (GameMap map) {
        for (GameMap gameMap : maps) {
            if (gameMap.equals(map)) return true;
        }
        return false;
    }
    
    public ArrayList<GameMap> getMaps () {
        return maps;
    }
    
    public GameMap getMapByName (String name) {
        for (GameMap map : maps) {
            if (map.name.equals(name)) return map;
        }
        return null;
    }
    
    public User referenceToNewlyAddedUser (User addingUser) {
        for (User user : users) {
            if (user.equals(addingUser)) return user;
        }
        users.add(addingUser);
        return addingUser;
    }
    
    public Messages addNewMap (GameMap map) {
        if (isTheMapAdded(map)) return Messages.MAP_ALREADY_ADDED;
        maps.add(map);
        return Messages.SUCCESS;
    }
}
