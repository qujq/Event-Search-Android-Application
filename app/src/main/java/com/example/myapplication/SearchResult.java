package com.example.myapplication;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.myapplication.databinding.ActivitySearchResultBinding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
//import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
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
import java.util.Arrays;


public class SearchResult extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivitySearchResultBinding binding;

//    private List<Products> listProducts;
    private RequestQueue requestQueue;
    private List<Event> event_list;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = SearchResult.this;

        binding = ActivitySearchResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_search_result);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        receiveData();

//        binding.fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_search_result);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private boolean receiveData() {
        Intent intent = getIntent();
        String search_url = intent.getStringExtra("SearchURL");
        Log.d("search_url", "url is "+ search_url);
//        listProducts = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(search_url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject page = response.getJSONObject("page");
                            int total_element = page.getInt("totalElements");
                            if(total_element == 0){
                                Log.d("total number", "no results");
                            }
                            else{
                                JSONObject embedded = response.getJSONObject("_embedded");
                                JSONArray events = embedded.getJSONArray("events");
                                JSONObject each_event = null;
                                event_list = new ArrayList<>();
                                for (int j = 0; j < events.length(); j++) {
                                    each_event = events.getJSONObject(j);
                                    String name = each_event.getString("name");
                                    String venue = each_event.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getString("name");
                                    String date = each_event.getJSONObject("dates").getJSONObject("start").getString("localDate");
                                    String category = each_event.getJSONArray("classifications").getJSONObject(0).getJSONObject("segment").getString("name");
                                    JSONArray artists_teams_list = new JSONArray();
                                    try{
                                        artists_teams_list = each_event.getJSONObject("_embedded").getJSONArray("attractions");
                                    }catch (Exception e) {
                                    }

                                    List<String> artists_teams_array = new ArrayList<String>();
                                    for(int k = 0; k < artists_teams_list.length(); ++k){
                                        artists_teams_array.add(artists_teams_list.getJSONObject(k).getString("name"));
                                    }
                                    List<String> category_detail = new ArrayList<String>();
                                    category_detail.add(each_event.getJSONArray("classifications").getJSONObject(0).getJSONObject("segment").getString("name"));
                                    category_detail.add(each_event.getJSONArray("classifications").getJSONObject(0).getJSONObject("genre").getString("name"));
                                    category_detail.add(each_event.getJSONArray("classifications").getJSONObject(0).getJSONObject("subGenre").getString("name"));
                                    try{
                                        category_detail.add(each_event.getJSONArray("classifications").getJSONObject(0).getJSONObject("type").getString("name"));
                                    }catch (Exception e) {
                                    }
                                    try{
                                        category_detail.add(each_event.getJSONArray("classifications").getJSONObject(0).getJSONObject("subType").getString("name"));
                                    }catch (Exception e) {
                                    }
                                    String price_range = each_event.getJSONArray("priceRanges").getJSONObject(0).getString("min")
                                            + " ~ "
                                            + each_event.getJSONArray("priceRanges").getJSONObject(0).getString("max")
                                            + " USD";
                                    String ticket_status = each_event.getJSONObject("dates").getJSONObject("status").getString("code");
                                    String ticketmaster_url = each_event.getString("url");
                                    String seatmap_url = "";
                                    try{
                                        seatmap_url = each_event.getJSONObject("seatmap").getString("staticUrl");
                                    }catch (Exception e) {
                                        seatmap_url = "";
                                    }

                                    Log.d("name", name);
                                    Log.d("venue", venue);
                                    Log.d("date", date);
                                    Log.d("category", category);
                                    for(int k = 0; k < artists_teams_array.size(); ++k) {
                                        Log.d("artists_teams_array", artists_teams_array.get(k));
                                    }
                                    for(int k = 0; k < category_detail.size(); ++k) {
                                        Log.d("category_detail", category_detail.get(k));
                                    }
                                    Log.d("price_range", price_range);
                                    Log.d("ticket_status", ticket_status);
                                    Log.d("ticketmaster_url", ticketmaster_url);
                                    Log.d("seatmap_url", seatmap_url);


                                    Event event = new Event(name, venue, date, category);
                                    event.setCategory(category);
                                    event.setArtistsTeams(artists_teams_array);
                                    event.setCategoryDetail(category_detail);
                                    event.setPriceRange(price_range);
                                    event.setTicketStatus(ticket_status);
                                    event.setTicketmasterUrl(ticketmaster_url);
                                    event.setSeatmapUrl(seatmap_url);
                                    event_list.add(event);

                                }

                                RecyclerView myrv = findViewById(R.id.search_result_recycler_view);
                                RecyclerViewAdapter myAdapter = new RecyclerViewAdapter(context, event_list);
                                myrv.setLayoutManager(new GridLayoutManager(context,1));
                                myrv.setAdapter(myAdapter);
                            }

                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAG", error.getMessage(), error);
                    }
        });
        requestQueue.add(jsonObjectRequest);
        return true;
}}