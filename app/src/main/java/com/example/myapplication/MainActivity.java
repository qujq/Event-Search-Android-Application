package com.example.myapplication;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.myapplication.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
//import android.widget.RadioGroup;
//import android.widget.RadioButton;
//import android.widget.EditText;
//import android.widget.Button;
//import android.widget.Spinner;
//import android.widget.AdapterView.*;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.*;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class MainActivity extends AppCompatActivity {
    static final String[] category_spinner_item = {"All","Music","Sport","Art \\u0026 Theatre","Film", "Miscellaneous"};
    static final String[] distance_unit_item = {"Miles", "Kilometers"};

    private static String TAG = "LOGGERINFO";
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
    private RadioGroup location_choice;
    private RadioButton here_location;
    private RadioButton other_location;

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

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Log.d(TAG,"click button");
            }
        });
///////////////////////

        location_choice = findViewById(R.id.location_choice);
        here_location = findViewById(R.id.here_location);
        other_location = findViewById(R.id.other_location);
        here_location.setChecked(true);  // set default location here

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
                    location_chosen = here_location.getText().toString();
                }else{
                    location_chosen = other_location.getText().toString();
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
                String locationValue = location_input.getText().toString();
                Log.d(TAG, "keyWordValue is : " + keywordValue +
                        " category_chosen: " + category_chosen +
                        " distanceValue: " + distanceValue +
                        " distance_chosen: " + distance_chosen +
                        " location_chosen: " + location_chosen +
                        " locationValue: " + locationValue
                );

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