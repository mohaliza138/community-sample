package org.example.controller;

import org.example.model.GameMap;
import org.example.model.Messages;

import java.util.ArrayList;

public class ServerHandler {
    private final ArrayList<GameMap> maps = new ArrayList<>();
    private boolean isTheMapAdded (GameMap map) {
        for (GameMap gameMap : maps) {
            if (gameMap.equals(map)) return true;
        }
        return false;
    }
    
    private Messages addNewMap (GameMap map) {
        if (isTheMapAdded(map)) return Messages.MAP_ALREADY_ADDED;
        maps.add(map);
        //todo: graphical changes!
        return Messages.SUCCESS;
    }
}
