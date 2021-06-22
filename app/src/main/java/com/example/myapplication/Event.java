package com.example.myapplication;

import org.json.JSONArray;
import org.json.JSONObject;

public class Event {
    private String name;
    private String venue;
    private String date;
    private String category;
    private Boolean isFavoirte;
    public Event(String name, String venue,String date) {
        this.name = name;
        this.venue = venue;
        this.date = date;
    }
    public String getName() {
        return name;
    }

    public String getVenue() {
        return venue;
    }

    public String getDate() {
        return date;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    public void setFavorite(Boolean isFavoirte) {
        this.isFavoirte = isFavoirte;
    }
}
