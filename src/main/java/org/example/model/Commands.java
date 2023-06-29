package org.example.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Commands {
    NEW_MAP ("new map (?<map>.*)"),
    SHARE_MAP ("share (?<map>.*)");
    private final String regex;
    Commands (String regex) {
        this.regex = regex;
    }
    
    public Matcher getMatcher (String string) {
        Matcher matcher = Pattern.compile(regex).matcher(string);
        if (matcher.matches()) return matcher;
        return null;
    }
}