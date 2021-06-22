package com.example.myapplication;

import org.json.JSONArray;
import org.json.JSONObject;

public class Event {
    private String name;
    private String venue;
    private String date;
    private String category;
    private Boolean isFavorite;
    public Event(String name, String venue,String date, String category) {
        this.name = name;
        this.venue = venue;
        this.date = date;
        this.category = category;
        isFavorite = false;
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

    public String getCategory() {
        return category;
    }

    public String getFavoriteString() {
        if(isFavorite){
            return "true";
        }
        else{
            return "false";
        }
    }

    public Boolean getIsFavorite() {
        return isFavorite;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    public void setFavorite(Boolean isFavorite) {
        this.isFavorite = isFavorite;
    }
}
