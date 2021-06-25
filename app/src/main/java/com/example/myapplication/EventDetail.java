package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Layout;
import android.util.Log;
import android.widget.TextView;
import android.widget.*;
import android.text.method.LinkMovementMethod;
import android.view.View;

//import android.support.design.widget.TabLayout;
import com.google.android.material.tabs.TabLayout;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private TabLayout detail_tablayout;
    private TextView artistsTeams_textview;

    private String settext_temp = "";

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

        String artist_url = "https://nodejs-9991.wl.r.appspot.com/spotify?artist=";
        for (String each_artist: artistsTeams.split("\\|")){
            Log.d("each_artist", each_artist);
            requestQueue = Volley.newRequestQueue(getApplicationContext());
            String url = artist_url + each_artist;
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Log.d("spotify", "onResponse: ++++++++++++++");
                                String total = response.getJSONObject("artists").getString("total");
                                if(total.equals("0")){
                                    Log.d("artist", "no records");
                                    String artist_hyperlink = "<p>" + each_artist + ": No details" + "</p>";
                                    settext_temp += artist_hyperlink;
                                    TextView artist = findViewById(R.id.artist);
                                    TextView artist_no_records = findViewById(R.id.artist_no_records);
                                    Log.d("settext", settext_temp);
                                    artist_no_records.setVisibility(View.GONE);
                                    artist.setText(Html.fromHtml(settext_temp));
                                    artist.setMovementMethod(LinkMovementMethod.getInstance());
                                }
                                else{
                                    JSONArray artist_detail_list = response.getJSONObject("artists").getJSONArray("items");

                                    for(int i = 0; i < artist_detail_list.length(); ++i){
                                        String name = artist_detail_list.getJSONObject(i).getString("name");
                                        String popularity = "";
                                        String followers = "";
                                        String external_urls = "";
                                        if(name.trim().equals(each_artist.trim())){
                                            Log.d("name===", name);
                                            try {
                                                popularity = artist_detail_list.getJSONObject(i).getString("popularity");
                                            }catch (Exception e){
                                                popularity = "";
                                            }
                                            try{
                                                followers = artist_detail_list.getJSONObject(i).getJSONObject("followers").getString("total");
                                            }catch (Exception e){
                                                followers = "";
                                            }
                                            try{
                                                external_urls = artist_detail_list.getJSONObject(i).getJSONObject("external_urls").getString("spotify");
                                            }catch (Exception e){
                                                external_urls = "";
                                            }
                                            String artist_hyperlink = "";
                                            if(popularity.equals("") && followers.equals("") && external_urls.equals("")){
                                                artist_hyperlink = "<p>" + name + ": No details" + "</p>";
                                            }
                                            else {
                                                artist_hyperlink = "<p>Name &nbsp;&nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp;" + name + "</p>" +
                                                        "<p>Followers &nbsp; &nbsp;&nbsp; " + followers + "</p>" +
                                                        "<p>Popularity &nbsp;&nbsp;&nbsp; " + popularity + "</p>" +
                                                        "<p>CheckAt   &nbsp; &nbsp;&nbsp; &nbsp;&nbsp;    " +
                                                        "<a href=" + external_urls + " >Spotify</a></p>";
                                            }
                                            Log.d("settext", settext_temp);
                                            settext_temp += artist_hyperlink;
                                            TextView artist = findViewById(R.id.artist);
                                            TextView artist_no_records = findViewById(R.id.artist_no_records);
                                            artist_no_records.setVisibility(View.GONE);
                                            Log.d("settext", settext_temp);
                                            artist.setText(Html.fromHtml(settext_temp));
                                            artist.setMovementMethod(LinkMovementMethod.getInstance());

                                            break;
                                        }
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

        TextView venue_textview = findViewById(R.id.detail_venue_content2);
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

        TextView venue_name_content_textview = findViewById(R.id.venue_name_content);
        TextView venue_address_content_textview = findViewById(R.id.venue_address_content);
        TextView venue_city_content_textview = findViewById(R.id.venue_city_content);
        TextView venue_phone_number_content_textview = findViewById(R.id.venue_phone_number_content);
        TextView venue_open_hours_content_textview = findViewById(R.id.venue_open_hours_content);
        TextView venue_general_rule_content_textview = findViewById(R.id.venue_general_rule_content);
        TextView venue_child_rule_content_textview = findViewById(R.id.venue_child_rule_content);

//        // intent obj
//        Intent intent_map = new Intent(EventDetail.this, MapsActivity.class);
//        // pack data
////        intent.putExtra("SearchURL",search_url );
//        // start activity
//        startActivity(intent_map);

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

                                    venue_name_content_textview.setText(event_venue);
                                    Log.d("venue_address+", "000"+venue_address);
                                    venue_address_content_textview.setText(venue_address);
                                    venue_city_content_textview.setText(venue_city);
                                    venue_phone_number_content_textview.setText(venue_phone_number);
                                    venue_open_hours_content_textview.setText(venue_open_hours);
                                    venue_general_rule_content_textview.setText(venue_general_rule);
                                    venue_child_rule_content_textview.setText(venue_child_rule);
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


        venue_name_content_textview.setVisibility(View.GONE);
        venue_address_content_textview.setVisibility(View.GONE);
        venue_city_content_textview.setVisibility(View.GONE);
        venue_phone_number_content_textview.setVisibility(View.GONE);
        venue_open_hours_content_textview.setVisibility(View.GONE);
        venue_general_rule_content_textview.setVisibility(View.GONE);
        venue_child_rule_content_textview.setVisibility(View.GONE);

        TextView venue_venue_name_textview = findViewById(R.id.venue_venue_name);
        TextView venue_address_textview = findViewById(R.id.venue_address);
        TextView venue_city_textview = findViewById(R.id.venue_city);
        TextView venue_phone_number_textview = findViewById(R.id.venue_phone_number);
        TextView venue_open_hours_textview = findViewById(R.id.venue_open_hours);
        TextView venue_general_rule_textview = findViewById(R.id.venue_general_rule);
        TextView venue_child_rule_textview = findViewById(R.id.venue_child_rule);


        venue_venue_name_textview.setVisibility(View.GONE);
        venue_address_textview.setVisibility(View.GONE);
        venue_city_textview.setVisibility(View.GONE);
        venue_phone_number_textview.setVisibility(View.GONE);
        venue_open_hours_textview.setVisibility(View.GONE);
        venue_general_rule_textview.setVisibility(View.GONE);
        venue_child_rule_textview.setVisibility(View.GONE);

        // tab 2
        LinearLayout artist_tab = findViewById(R.id.artist_tab);
        artist_tab.setVisibility(View.GONE);


        detail_tablayout = findViewById(R.id.tabLayout);
        artistsTeams_textview = findViewById(R.id.detail_artists_teams);
        detail_tablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                TextView detail_artists_teams_textview = findViewById(R.id.detail_artists_teams);
                TextView detail_artists_teams_content_textview = findViewById(R.id.detail_artists_teams_content);
                TextView detail_venue_textview = findViewById(R.id.detail_venue);
                TextView detail_venue_content_textview = findViewById(R.id.detail_venue_content2);
                TextView detail_date_textview = findViewById(R.id.detail_date);
                TextView detail_date_content_textview = findViewById(R.id.detail_date_content);
                TextView detail_category_textview = findViewById(R.id.detail_category);
                TextView detail_category_content_textview = findViewById(R.id.detail_category_content);
                TextView detail_price_range_textview = findViewById(R.id.detail_price_range);
                TextView detail_price_range_content_textview = findViewById(R.id.detail_price_range_content);
                TextView detail_ticket_status_textview = findViewById(R.id.detail_ticket_status);
                TextView detail_ticket_status_content_textview = findViewById(R.id.detail_ticket_status_content);
                TextView detail_buy_ticket_at_textview = findViewById(R.id.detail_buy_ticket_at);
                TextView detail_buy_ticket_at_content_textview = findViewById(R.id.detail_buy_ticket_at_content);
                TextView detail_seat_map_textview = findViewById(R.id.detail_seat_map);
                TextView detail_seat_map_content_textview = findViewById(R.id.detail_seat_map_content);

                TextView venue_name_content_textview = findViewById(R.id.venue_name_content);
                TextView venue_address_content_textview = findViewById(R.id.venue_address_content);
                TextView venue_city_content_textview = findViewById(R.id.venue_city_content);
                TextView venue_phone_number_content_textview = findViewById(R.id.venue_phone_number_content);
                TextView venue_open_hours_content_textview = findViewById(R.id.venue_open_hours_content);
                TextView venue_general_rule_content_textview = findViewById(R.id.venue_general_rule_content);
                TextView venue_child_rule_content_textview = findViewById(R.id.venue_child_rule_content);

                if (tab.getPosition() == 0){
                    Log.d("tab", "onTabSelected: 0");
                    // tab 0 events
                    detail_artists_teams_textview.setVisibility(View.VISIBLE);
                    detail_artists_teams_content_textview.setVisibility(View.VISIBLE);
                    detail_venue_textview.setVisibility(View.VISIBLE);
                    detail_venue_content_textview.setVisibility(View.VISIBLE);
                    detail_date_textview.setVisibility(View.VISIBLE);
                    detail_date_content_textview.setVisibility(View.VISIBLE);
                    detail_category_textview.setVisibility(View.VISIBLE);
                    detail_category_content_textview.setVisibility(View.VISIBLE);
                    detail_price_range_textview.setVisibility(View.VISIBLE);
                    detail_price_range_content_textview.setVisibility(View.VISIBLE);
                    detail_ticket_status_textview.setVisibility(View.VISIBLE);
                    detail_ticket_status_content_textview.setVisibility(View.VISIBLE);
                    detail_buy_ticket_at_textview.setVisibility(View.VISIBLE);
                    detail_buy_ticket_at_content_textview.setVisibility(View.VISIBLE);
                    detail_seat_map_textview.setVisibility(View.VISIBLE);
                    detail_seat_map_content_textview.setVisibility(View.VISIBLE);

                    // tab 1
                    artist_tab.setVisibility(View.GONE);

                    // tab 2
                    venue_venue_name_textview.setVisibility(View.GONE);
                    venue_address_textview.setVisibility(View.GONE);
                    venue_city_textview.setVisibility(View.GONE);
                    venue_phone_number_textview.setVisibility(View.GONE);
                    venue_open_hours_textview.setVisibility(View.GONE);
                    venue_general_rule_textview.setVisibility(View.GONE);
                    venue_child_rule_textview.setVisibility(View.GONE);

                    venue_name_content_textview.setVisibility(View.GONE);
                    venue_address_content_textview.setVisibility(View.GONE);
                    venue_city_content_textview.setVisibility(View.GONE);
                    venue_phone_number_content_textview.setVisibility(View.GONE);
                    venue_open_hours_content_textview.setVisibility(View.GONE);
                    venue_general_rule_content_textview.setVisibility(View.GONE);
                    venue_child_rule_content_textview.setVisibility(View.GONE);

                }
                if (tab.getPosition() == 1){
                    Log.d("tab", "onTabSelected: 1");

                    // tab 0 events
                    detail_artists_teams_textview.setVisibility(View.GONE);
                    detail_artists_teams_content_textview.setVisibility(View.GONE);
                    detail_venue_textview.setVisibility(View.GONE);
                    detail_venue_content_textview.setVisibility(View.GONE);
                    detail_date_textview.setVisibility(View.GONE);
                    detail_date_content_textview.setVisibility(View.GONE);
                    detail_category_textview.setVisibility(View.GONE);
                    detail_category_content_textview.setVisibility(View.GONE);
                    detail_price_range_textview.setVisibility(View.GONE);
                    detail_price_range_content_textview.setVisibility(View.GONE);
                    detail_ticket_status_textview.setVisibility(View.GONE);
                    detail_ticket_status_content_textview.setVisibility(View.GONE);
                    detail_buy_ticket_at_textview.setVisibility(View.GONE);
                    detail_buy_ticket_at_content_textview.setVisibility(View.GONE);
                    detail_seat_map_textview.setVisibility(View.GONE);
                    detail_seat_map_content_textview.setVisibility(View.GONE);

                    // tab 1
                    artist_tab.setVisibility(View.VISIBLE);

                    // tab 2
                    venue_venue_name_textview.setVisibility(View.GONE);
                    venue_address_textview.setVisibility(View.GONE);
                    venue_city_textview.setVisibility(View.GONE);
                    venue_phone_number_textview.setVisibility(View.GONE);
                    venue_open_hours_textview.setVisibility(View.GONE);
                    venue_general_rule_textview.setVisibility(View.GONE);
                    venue_child_rule_textview.setVisibility(View.GONE);

                    venue_name_content_textview.setVisibility(View.GONE);
                    venue_address_content_textview.setVisibility(View.GONE);
                    venue_city_content_textview.setVisibility(View.GONE);
                    venue_phone_number_content_textview.setVisibility(View.GONE);
                    venue_open_hours_content_textview.setVisibility(View.GONE);
                    venue_general_rule_content_textview.setVisibility(View.GONE);
                    venue_child_rule_content_textview.setVisibility(View.GONE);
                }
                if (tab.getPosition() == 2){
                    Log.d("tab", "onTabSelected: 2");
                    // tab 0 events
                    detail_artists_teams_textview.setVisibility(View.GONE);
                    detail_artists_teams_content_textview.setVisibility(View.GONE);
                    detail_venue_textview.setVisibility(View.GONE);
                    detail_venue_content_textview.setVisibility(View.GONE);
                    detail_date_textview.setVisibility(View.GONE);
                    detail_date_content_textview.setVisibility(View.GONE);
                    detail_category_textview.setVisibility(View.GONE);
                    detail_category_content_textview.setVisibility(View.GONE);
                    detail_price_range_textview.setVisibility(View.GONE);
                    detail_price_range_content_textview.setVisibility(View.GONE);
                    detail_ticket_status_textview.setVisibility(View.GONE);
                    detail_ticket_status_content_textview.setVisibility(View.GONE);
                    detail_buy_ticket_at_textview.setVisibility(View.GONE);
                    detail_buy_ticket_at_content_textview.setVisibility(View.GONE);
                    detail_seat_map_textview.setVisibility(View.GONE);
                    detail_seat_map_content_textview.setVisibility(View.GONE);

                    // tab 1
                    artist_tab.setVisibility(View.GONE);

                    // tab 2
                    venue_venue_name_textview.setVisibility(View.VISIBLE);
                    venue_address_textview.setVisibility(View.VISIBLE);
                    venue_city_textview.setVisibility(View.VISIBLE);
                    venue_phone_number_textview.setVisibility(View.VISIBLE);
                    venue_open_hours_textview.setVisibility(View.VISIBLE);
                    venue_general_rule_textview.setVisibility(View.VISIBLE);
                    venue_child_rule_textview.setVisibility(View.VISIBLE);

                    venue_name_content_textview.setVisibility(View.VISIBLE);
                    venue_address_content_textview.setVisibility(View.VISIBLE);
                    venue_city_content_textview.setVisibility(View.VISIBLE);
                    venue_phone_number_content_textview.setVisibility(View.VISIBLE);
                    venue_open_hours_content_textview.setVisibility(View.VISIBLE);
                    venue_general_rule_content_textview.setVisibility(View.VISIBLE);
                    venue_child_rule_content_textview.setVisibility(View.VISIBLE);

                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }
}