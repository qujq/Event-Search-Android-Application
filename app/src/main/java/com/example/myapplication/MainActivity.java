package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.databinding.ActivityMainBinding;
import com.google.android.material.tabs.TabLayout;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.*;
import android.widget.AdapterView.*;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.content.Intent;


import java.util.ArrayList;
import java.util.List;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;


public class MainActivity extends AppCompatActivity {
    static final String[] category_spinner_item = {"All","Music","Sport","Art \\u0026 Theatre","Film", "Miscellaneous"};
    static final String[] distance_unit_item = {"miles", "km"};
    SharedPreferences sharedpreferences;

    private static final String event_search_url = "https://nodejs-9991.wl.r.appspot.com/?";

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    private EditText keyword_input;
    private Spinner category_choice;
    private String category_chosen;
    private EditText distance_input;
    private Spinner distance_unit_choice;
    private String distance_chosen;
    private String location_chosen;
    private EditText location_input;
    private Button search_button;
    private Button clear_button;
    private RadioGroup location_choice;
    private RadioButton here_location;
    private RadioButton other_location;

    private TabLayout detail_tablayout;
    private Context context;
    private List<Event> event_list;
    public static final String mypreference = "mypref";
    public JSONArray favorite_json_list;
    private EmptyRecyclerView myrv;
    private View mEmptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
///////////////
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

///////////////////////
        context = MainActivity.this;

        location_choice = findViewById(R.id.location_choice);
        here_location = findViewById(R.id.here_location);
        other_location = findViewById(R.id.other_location);
        here_location.setChecked(true);  // set default location here
        location_chosen = "Here";

        keyword_input = findViewById(R.id.keyword_input);
        category_choice = findViewById(R.id.category_choice);
        category_choice.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("spinner:", "category chosen：" + category_spinner_item[position]);
                category_chosen = category_spinner_item[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        distance_input = findViewById(R.id.distance_input);
        distance_unit_choice = findViewById(R.id.distance_unit_choice);
        distance_unit_choice.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("spinner:", "distance_chosen：" + distance_unit_item[position]);
                distance_chosen = distance_unit_item[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        location_choice.setOnCheckedChangeListener(new OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup rg, int checkedId) {
                // TODO Auto-generated method stub
                if(checkedId == here_location.getId()){
                    location_chosen = "Here";
                }else{
                    location_chosen = "Other";
                }
                Log.d("location_chosen", location_chosen);
            }
        });

        location_input = findViewById(R.id.location_input);



        search_button = findViewById(R.id.search_button);
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keywordValue = keyword_input.getText().toString();
                String distanceValue = distance_input.getText().toString();
                int distanceValue_int = 10;
                if(!distanceValue.equals("")){
                    distanceValue_int = Integer.parseInt(distanceValue);
                }
                String locationValue = location_input.getText().toString();
                Log.d("search", "keyWordValue is : " + keywordValue +
                        " category_chosen: " + category_chosen +
                        " distanceValue_int: " + distanceValue_int +
                        " distance_chosen: " + distance_chosen +
                        " location_chosen: " + location_chosen +
                        " locationValue: " + locationValue
                );
                Boolean formHasError = false;
                if (keywordValue.trim().equals("")) {
                    keyword_input.setError("Please enter mandatory field");
                    formHasError = true;
                }
                if(location_chosen.equals("Other") && locationValue.trim().equals("")){
                    location_input.setError("Please enter mandatory field");
                    formHasError = true;
                }
                if(formHasError){
                    return;
                }
                Log.d("form check","success form check");
//                generate url
                String search_url = "";
                search_url += event_search_url;
                search_url += "keyword=" + keywordValue;
                search_url += "&category=" + category_chosen;
                search_url += "&distance=" + distanceValue_int;
                search_url += "&distanceUnit=" + distance_chosen;
                search_url += "&from=" + location_chosen;
                search_url += "&fromLocation=" + locationValue;
                search_url += "&latitude=" + 34.0522;
                search_url += "&longitude=" + -118.2437;
                Log.d("search_url", search_url);

                // intent obj
                Intent intent = new Intent(MainActivity.this, SearchResult.class);
                // pack data
                intent.putExtra("SearchURL",search_url );
                intent.putExtra("isFavorite", "false");
                // start activity
                startActivity(intent);


            }
        });

        Intent intent = getIntent();
        Boolean showFavorite = intent.getBooleanExtra("favorite", false);
        if(showFavorite){
            Log.d("TAG", "onTabSelected: 1");
            LinearLayout search_form_content = findViewById(R.id.form_content);
            Button search_button = findViewById(R.id.search_button);
            Button clear_button = findViewById(R.id.clear_button);
            search_form_content.setVisibility(View.GONE);
            search_button.setVisibility(View.GONE);
            clear_button.setVisibility(View.GONE);
            LinearLayout favorite_layout = findViewById(R.id.favorite_layout);
            favorite_layout.setVisibility(View.VISIBLE);

            ////////////////////// favorite tab
            // get favorite
            sharedpreferences = context.getSharedPreferences(mypreference,
                    Context.MODE_PRIVATE);
            if (sharedpreferences.contains("favorite")) {
                Log.d("first page shared pref", (sharedpreferences.getString("favorite", "[]")));
                try {
                    favorite_json_list = new JSONArray(sharedpreferences.getString("favorite", "[]"));
                }catch (JSONException ex) {
                    throw new RuntimeException(ex);
                }
                for (int i = 0; i < favorite_json_list.length(); i++) {
                    try{
                        Log.d("first page split", favorite_json_list.getJSONObject(i).toString());
                    }catch (JSONException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
            else{
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("favorite", "[]");
                editor.commit();
            }

            event_list = new ArrayList<>();
            for(int k = 0; k < favorite_json_list.length(); ++k){
                try{
                    String name = favorite_json_list.getJSONObject(k).getString("name");
                    String venue = favorite_json_list.getJSONObject(k).getString("venue");
                    String date = favorite_json_list.getJSONObject(k).getString("date");
                    String category = favorite_json_list.getJSONObject(k).getString("category");
                    String price_range = favorite_json_list.getJSONObject(k).getString("price_range");
                    String ArtistsTeams = favorite_json_list.getJSONObject(k).getString("ArtistsTeams");
                    String category_detail = favorite_json_list.getJSONObject(k).getString("category_detail");
                    String ticket_status = favorite_json_list.getJSONObject(k).getString("ticket_status");
                    String ticketmaster_url = favorite_json_list.getJSONObject(k).getString("ticketmaster_url");
                    String seatmap_url = favorite_json_list.getJSONObject(k).getString("seatmap_url");

                    List<String> artists_teams_array = new ArrayList<>();
                    for (String each_artist: ArtistsTeams.split("\\|")){
                        artists_teams_array.add(each_artist);
                    }

                    List<String> category_detail_array = new ArrayList<>();
                    for (String each_category_detail: category_detail.split("\\|")){
                        category_detail_array.add(each_category_detail);
                    }

                    Event event = new Event(name, venue, date, category);
                    event.setIsFavorite(true);
                    event.setPriceRange(price_range);
                    event.setTicketStatus(ticket_status);
                    event.setTicketmasterUrl(ticketmaster_url);
                    event.setSeatmapUrl(seatmap_url);
                    event.setArtistsTeams(artists_teams_array);
                    event.setCategoryDetail(category_detail_array);
                    event_list.add(event);
                }catch (JSONException ex) {
                    throw new RuntimeException(ex);
                }
            }

            //////////////////test/////////////
            mEmptyView = findViewById(R.id.id_empty_view);
            myrv = (EmptyRecyclerView) findViewById(R.id.favorite_recycler_view);
            RecyclerViewAdapterFavorite myAdapterFavorite = new RecyclerViewAdapterFavorite(context, event_list);
            myrv.setLayoutManager(new GridLayoutManager(context,1));
            myrv.setAdapter(myAdapterFavorite);
            myrv.setEmptyView(mEmptyView);
        }

        clear_button = findViewById(R.id.clear_button);
        clear_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyword_input.setText("");
                distance_input.setText("10");
                location_input.setText("");
                here_location.setChecked(true);  // set default location here
                category_choice.setSelection(0);
                distance_unit_choice.setSelection(0);
                keyword_input.setError(null);
                location_input.setError(null);

            }
        });

        detail_tablayout = findViewById(R.id.search_favorite_toolbar);
        detail_tablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            LinearLayout search_form_content = findViewById(R.id.form_content);
            Button search_button = findViewById(R.id.search_button);
            Button clear_button = findViewById(R.id.clear_button);
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0){
                    Log.d("TAG", "onTabSelected: 0");
                    search_form_content.setVisibility(View.VISIBLE);
                    search_button.setVisibility(View.VISIBLE);
                    clear_button.setVisibility(View.VISIBLE);
                    LinearLayout favorite_layout = findViewById(R.id.favorite_layout);
                    favorite_layout.setVisibility(View.GONE);
                }
                if (tab.getPosition() == 1){

                    Log.d("TAG", "onTabSelected: 1");
                    search_form_content.setVisibility(View.GONE);
                    search_button.setVisibility(View.GONE);
                    clear_button.setVisibility(View.GONE);
                    LinearLayout favorite_layout = findViewById(R.id.favorite_layout);
                    favorite_layout.setVisibility(View.VISIBLE);

                    ////////////////////// favorite tab
                    // get favorite
                    sharedpreferences = context.getSharedPreferences(mypreference,
                            Context.MODE_PRIVATE);
                    if (sharedpreferences.contains("favorite")) {
                        Log.d("first page shared pref", (sharedpreferences.getString("favorite", "[]")));
                        try {
                            favorite_json_list = new JSONArray(sharedpreferences.getString("favorite", "[]"));
                        }catch (JSONException ex) {
                            throw new RuntimeException(ex);
                        }
                        for (int i = 0; i < favorite_json_list.length(); i++) {
                            try{
                                Log.d("first page split", favorite_json_list.getJSONObject(i).toString());
                            }catch (JSONException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    }
                    else{
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("favorite", "[]");
                        editor.commit();
                    }

                    event_list = new ArrayList<>();
                    for(int k = 0; k < favorite_json_list.length(); ++k){
                        try{
                            String name = favorite_json_list.getJSONObject(k).getString("name");
                            String venue = favorite_json_list.getJSONObject(k).getString("venue");
                            String date = favorite_json_list.getJSONObject(k).getString("date");
                            String category = favorite_json_list.getJSONObject(k).getString("category");
                            String price_range = favorite_json_list.getJSONObject(k).getString("price_range");
                            String ArtistsTeams = favorite_json_list.getJSONObject(k).getString("ArtistsTeams");
                            String category_detail = favorite_json_list.getJSONObject(k).getString("category_detail");
                            String ticket_status = favorite_json_list.getJSONObject(k).getString("ticket_status");
                            String ticketmaster_url = favorite_json_list.getJSONObject(k).getString("ticketmaster_url");
                            String seatmap_url = favorite_json_list.getJSONObject(k).getString("seatmap_url");

                            List<String> artists_teams_array = new ArrayList<>();
                            for (String each_artist: ArtistsTeams.split("\\|")){
                                artists_teams_array.add(each_artist);
                            }

                            List<String> category_detail_array = new ArrayList<>();
                            for (String each_category_detail: category_detail.split("\\|")){
                                category_detail_array.add(each_category_detail);
                            }

                            Event event = new Event(name, venue, date, category);
                            event.setIsFavorite(true);
                            event.setPriceRange(price_range);
                            event.setTicketStatus(ticket_status);
                            event.setTicketmasterUrl(ticketmaster_url);
                            event.setSeatmapUrl(seatmap_url);
                            event.setArtistsTeams(artists_teams_array);
                            event.setCategoryDetail(category_detail_array);
                            event_list.add(event);
                        }catch (JSONException ex) {
                            throw new RuntimeException(ex);
                        }
                    }

                    //////////////////test/////////////

//                    RecyclerView myrv = findViewById(R.id.favorite_recycler_view);
//                    RecyclerViewAdapterFavorite myAdapterFavorite = new RecyclerViewAdapterFavorite(context, event_list);
//                    myrv.setLayoutManager(new GridLayoutManager(context,1));
//                    myrv.setAdapter(myAdapterFavorite);

                    mEmptyView = findViewById(R.id.id_empty_view);
                    myrv = (EmptyRecyclerView) findViewById(R.id.favorite_recycler_view);
                    RecyclerViewAdapterFavorite myAdapterFavorite = new RecyclerViewAdapterFavorite(context, event_list);
                    myrv.setLayoutManager(new GridLayoutManager(context,1));
                    myrv.setAdapter(myAdapterFavorite);
                    myrv.setEmptyView(mEmptyView);

//                    // intent obj
//                    Intent intent = new Intent(MainActivity.this, SearchResult.class);
//                    // pack data
//                    intent.putExtra("isFavorite", "true");
//                    // start activity
//                    startActivity(intent);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}