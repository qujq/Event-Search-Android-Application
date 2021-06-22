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

                                    Log.d("name", name);
                                    Log.d("venue", venue);
                                    Log.d("date", date);
                                    Log.d("category", category);

                                    Event event = new Event(name, venue, date, category);
                                    event.setCategory(category);
                                    event_list.add(event);

                                }

                                RecyclerView myrv = findViewById(R.id.search_result_recycler_view);
                                RecyclerViewAdapter myAdapter = new RecyclerViewAdapter(context,event_list);
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