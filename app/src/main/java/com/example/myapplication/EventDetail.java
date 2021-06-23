package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;
import android.text.method.LinkMovementMethod;

public class EventDetail extends AppCompatActivity {

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


    }
}