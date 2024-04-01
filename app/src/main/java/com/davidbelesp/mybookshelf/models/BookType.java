package com.davidbelesp.mybookshelf.models;

import java.util.HashMap;
import java.util.Map;

public enum BookType {

    Novel("Novel"),
    Light_Novel("Light Novel"),
    Manga("Manga"),
    Manhwa("Manhwa"),
    Manhua("Manhua");

    private static final Map<String, BookType> lookupType = new HashMap<>();
    public final String label;

    public String getLabel(){
        return this.label;
    }

    static {
        for (BookType t : values()){
            lookupType.put(t.label, t);
        }
    }
    private BookType(String label) {this.label = label;}


    public static BookType get(String search){
        return lookupType.get(search);
    }
}
