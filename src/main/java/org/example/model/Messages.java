package org.example.model;

public enum Messages {
    MAP_ALREADY_ADDED ("The map you are trying to add is already added!"),
    SUCCESS ("Success!");
    public final String text;
    Messages (String text) {
        this.text = text;
    }
}