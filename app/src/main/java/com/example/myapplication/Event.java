package com.example.myapplication;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class Event {
    private String name;
    private String venue;
    private String date;
    private String category;
    private Boolean isFavorite;
    private List<String> artists_teams_array;
    private String artists_teams_string = "";
    private String category_detail = "";
    private String price_range;
    private String ticket_status;
    private String ticketmaster_url;
    private String seatmap_url;
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

    public String getArtistsTeams() {
        return artists_teams_string;
    }

    public String getCategoryString() {
        return category_detail;
    }

    public String getPriceRange() {
        return price_range;
    }

    public String getTicketStatus() {
        return ticket_status;
    }

    public String getTicketmasterUrl() {
        return ticketmaster_url;
    }

    public String getSeatmapUrl() {
        return seatmap_url;
    }


    public void setCategory(String category) {
        this.category = category;
    }

    public void setIsFavorite(Boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public void setArtistsTeams(List<String> artists_teams_array) {
        this.artists_teams_array = artists_teams_array;
        for(int k = 0; k < artists_teams_array.size(); ++k){
            if(artists_teams_array.get(k) != null) {
                artists_teams_string += artists_teams_array.get(k);
                if(k < artists_teams_array.size()-1) {
                    artists_teams_string += " | ";
                }
            }
        }
        Log.d("artists_teams_string", "setArtistsTeams: " + artists_teams_string);
    }

    public void setCategoryDetail(List<String> category_detail) {
        for(int k = 0; k < category_detail.size(); ++k){
            if(category_detail.get(k) != null  && !category_detail.get(k).equals("Undefined")) {
                this.category_detail += category_detail.get(k);
                if(k < category_detail.size()-1) {
                    this.category_detail += " | ";
                }
            }
        }
        Log.d("category_detail", "category_detail: " + category_detail);
    }

    public void setPriceRange(String price_range) {
        this.price_range = price_range;
    }


    public void setTicketStatus(String ticket_status) {
        this.ticket_status = ticket_status;
    }

    public void setTicketmasterUrl(String ticketmaster_url) {
        this.ticketmaster_url = ticketmaster_url;
    }

    public void setSeatmapUrl(String seatmap_url) {
        this.seatmap_url = seatmap_url;
    }

}
