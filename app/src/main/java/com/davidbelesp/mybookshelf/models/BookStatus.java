package com.davidbelesp.mybookshelf.models;

import java.util.HashMap;
import java.util.Map;

public enum BookStatus {

    Plan_to_read("Plan to read"),
    Reading("Reading"),
    Completed("Completed"),
    On_hold("On Hold"),

    Dropped("Dropped");


    private static final Map<String, BookStatus> lookupStatus = new HashMap<String, BookStatus>();

    public final String label;

    static {
        for (BookStatus b : values()){
            lookupStatus.put(b.label, b);
        }
    }

    public String getLabel(){
        return this.label;
    }

    private BookStatus(String label) {
        this.label = label;
    }

    public static BookStatus get(String search){

        return lookupStatus.get(search);
    }

}
