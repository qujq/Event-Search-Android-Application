package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;
import android.text.method.LinkMovementMethod;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EventDetail extends AppCompatActivity {
    private RequestQueue requestQueue;
    private String venue_address = "";
    private String venue_city = "";
    private String venue_phone_number = "";
    private String venue_open_hours = "";
    private String venue_general_rule = "";
    private String venue_child_rule = "";
    private String venue_location_latitude = "";
    private String venue_location_longitude = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        Intent intent = getIntent();
        String event_name = intent.getStringExtra("title");
        String event_venue = intent.getStringExtra("venue");
        String event_date = intent.getStringExtra("date");
        String artistsTeams = intent.getStringExtra("artistsTeams");
        String category = intent.getStringExtra("category");
        String priceRange = intent.getStringExtra("priceRange");
        String ticketStatus = intent.getStringExtra("ticketStatus");
        String ticketmaster = intent.getStringExtra("ticketmaster");
        String seatmap = intent.getStringExtra("seatmap");

        Log.d("event_details", "*****************");
        Log.d("event_name", event_name);
        Log.d("event_venue", event_venue);
        Log.d("event_date", event_date);
        Log.d("artistsTeams", artistsTeams);
        Log.d("category", category);
        Log.d("priceRange", priceRange);
        Log.d("ticketStatus", ticketStatus);
        Log.d("ticketmaster", ticketmaster);
        Log.d("seatmap", seatmap);

        TextView artistsTeams_textview = findViewById(R.id.detail_artists_teams_content);
        artistsTeams_textview.setText(artistsTeams);

        TextView venue_textview = findViewById(R.id.detail_venue_content);
        venue_textview.setText(event_venue);

        TextView date_textview = findViewById(R.id.detail_date_content);
        date_textview.setText(event_date);

        TextView category_textview = findViewById(R.id.detail_category_content);
        category_textview.setText(category);

        TextView priceRange_textview = findViewById(R.id.detail_price_range_content);
        priceRange_textview.setText(priceRange);

        TextView ticketStatus_textview = findViewById(R.id.detail_ticket_status_content);
        ticketStatus_textview.setText(ticketStatus);

        TextView buyTicketAt_textview = findViewById(R.id.detail_buy_ticket_at_content);
        String buyTicketAt_hyperlink = "<a href=" + ticketmaster + " >Ticketmaster</a>";
        buyTicketAt_textview.setText(Html.fromHtml(buyTicketAt_hyperlink));
        buyTicketAt_textview.setMovementMethod(LinkMovementMethod.getInstance());

        TextView seatmap_textview = findViewById(R.id.detail_seat_map_content);
        String seatmap_hyperlink = "<a href=" + seatmap + " >View Seat Map Here</a>";
        seatmap_textview.setText(Html.fromHtml(seatmap_hyperlink));
        seatmap_textview.setMovementMethod(LinkMovementMethod.getInstance());

        String venueDetailUrl = "https://nodejs-9991.wl.r.appspot.com/venueDetail?keyword=";
        venueDetailUrl += event_venue;

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(venueDetailUrl,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray venues = response.getJSONArray("venues");
                            for(int i = 0; i < venues.length(); ++i){
                                JSONObject each_venue = venues.getJSONObject(i);
                                String venue_name = each_venue.getString("name");
                                if(venue_name.equals(event_venue)){
                                    venue_city += each_venue.getJSONObject("city").getString("name");
                                    venue_city += " , ";
                                    venue_city += each_venue.getJSONObject("state").getString("name");

                                    venue_address += each_venue.getJSONObject("address").getString("line1");

                                    try{
                                        venue_phone_number += each_venue.getJSONObject("boxOfficeInfo").getString("phoneNumberDetail");
                                    }catch (Exception e) {
                                        venue_phone_number = "";
                                    }

                                    try{
                                        venue_open_hours += each_venue.getJSONObject("boxOfficeInfo").getString("openHoursDetail");
                                    }catch (Exception e) {
                                        venue_open_hours = "";
                                    }

                                    try{
                                        venue_general_rule += each_venue.getJSONObject("generalInfo").getString("generalRule");
                                    }catch (Exception e) {
                                        venue_general_rule = "";
                                    }

                                    try{
                                        venue_child_rule += each_venue.getJSONObject("generalInfo").getString("childRule");
                                    }catch (Exception e) {
                                        venue_child_rule = "";
                                    }
                                    venue_location_latitude += each_venue.getJSONObject("location").getString("latitude");
                                    venue_location_longitude += each_venue.getJSONObject("location").getString("longitude");

                                    Log.d("venue_address", venue_address);
                                    Log.d("venue_city", venue_city);
                                    Log.d("venue_phone_number", venue_phone_number);
                                    Log.d("venue_open_hours", venue_open_hours);
                                    Log.d("venue_general_rule", venue_general_rule);
                                    Log.d("venue_child_rule", venue_child_rule);
                                    Log.d("venue_location_latitude", venue_location_latitude);
                                    Log.d("venue_location_longitude", venue_location_longitude);
                                    break;
                                }
                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAG", error.getMessage(), error);
                    }
        });
        requestQueue.add(jsonObjectRequest);


    }
}