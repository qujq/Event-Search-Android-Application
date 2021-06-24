package com.example.myapplication;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
//import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<Event> mData;
    public RecyclerViewAdapter(Context mContext, List<Event> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.each_event, parent, false);
        return new NormalHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        NormalHolder normalHolder = (NormalHolder) holder;
        normalHolder.title.setText(mData.get(position).getName());
        normalHolder.venue.setText(mData.get(position).getVenue());
        normalHolder.date.setText(mData.get(position).getDate());
        normalHolder.category.setText(mData.get(position).getCategory());
        normalHolder.favorite.setText(mData.get(position).getFavoriteString());

        String category = mData.get(position).getCategory();

        if(category.equals("Arts & Theatre")){
            normalHolder.category_icon.setImageResource(R.drawable.art_icon);
        }
        else if(category.equals("Film")){
            normalHolder.category_icon.setImageResource(R.drawable.film_icon);
        }
        else if(category.equals("Music")){
            normalHolder.category_icon.setImageResource(R.drawable.music_icon);
        }
        else if(category.equals("Sports")){
            normalHolder.category_icon.setImageResource(R.drawable.ic_sport_icon);
        }
        else{
            normalHolder.category_icon.setImageResource(R.drawable.miscellaneous_icon);
        }

        normalHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("card view", "onClick: card view");
                // intent obj
                Intent intent = new Intent(mContext, EventDetail.class);
                // pack data
                intent.putExtra("title", mData.get(position).getName());
                intent.putExtra("venue", mData.get(position).getVenue());
                intent.putExtra("date", mData.get(position).getDate());
                intent.putExtra("artistsTeams", mData.get(position).getArtistsTeams());
                intent.putExtra("category", mData.get(position).getCategoryString());
                intent.putExtra("priceRange", mData.get(position).getPriceRange());
                intent.putExtra("ticketStatus", mData.get(position).getTicketStatus());
                intent.putExtra("ticketmaster", mData.get(position).getTicketmasterUrl());
                intent.putExtra("seatmap", mData.get(position).getSeatmapUrl());
                // start activity
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class NormalHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView venue;
        public TextView date;
        public TextView category;
        public TextView favorite;
        public ImageView favorite_icon;
        public ImageView category_icon;
        public CardView cardView;


        public NormalHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card);
            title = itemView.findViewById(R.id.event_title);
            venue = itemView.findViewById(R.id.event_venue);
            date = itemView.findViewById(R.id.event_date);
            category = itemView.findViewById(R.id.event_category);
            favorite = itemView.findViewById(R.id.event_favorite);
            favorite_icon = itemView.findViewById(R.id.favorite_icon);
            category_icon = itemView.findViewById(R.id.category_icon);
            if (category == null){
                Log.d("category", "null category");
            }

            favorite_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        favorite_icon.setImageResource(R.drawable.heart_fill_red);
                }
            });

        }

    }

}