package com.example.myapplication;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;

public class RecyclerViewAdapterFavorite extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    public List<Event> mData;
    public RecyclerViewAdapterFavorite(Context mContext, List<Event> mData) {
        super();
        this.mContext = mContext;
        this.mData = mData;
    }
    public static final String mypreference = "mypref";
    public SharedPreferences sharedpreferences;
    public JSONArray favorite_json_list;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.each_event, parent, false);
        // get
        sharedpreferences = mContext.getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        if (sharedpreferences.contains("favorite")) {
            Log.d("shared pref", (sharedpreferences.getString("favorite", "")));
            try {
                favorite_json_list = new JSONArray(sharedpreferences.getString("favorite", "[]"));
            }catch (JSONException ex) {
                throw new RuntimeException(ex);
            }
            for (int i = 0; i < favorite_json_list.length(); i++) {
                try{
                    Log.d("split", favorite_json_list.getJSONObject(i).toString());
                }catch (JSONException ex) {
                    throw new RuntimeException(ex);
                }
            }
            // clear
//            SharedPreferences.Editor editor = sharedpreferences.edit();
//            editor.putString("Name", "");
//            editor.commit();
        }
        else{
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("favorite", "[]");
            editor.commit();

        }
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

        for(int i = 0; i < favorite_json_list.length(); ++i){
            try{
                if(favorite_json_list.getJSONObject(i).getString("name").equals(mData.get(position).getName())
                        && favorite_json_list.getJSONObject(i).getString("date").equals(mData.get(position).getDate())){
                    mData.get(position).setIsFavorite(true);
                    normalHolder.favorite_icon.setImageResource(R.drawable.heart_fill_red);
                }
            }catch (JSONException ex) {
                throw new RuntimeException(ex);
            }
        }

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

        normalHolder.favorite_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    normalHolder.favorite_icon.setImageResource(R.drawable.heart_outline_black);
                    mData.get(position).setIsFavorite(false);

                    // get
                    sharedpreferences = mContext.getSharedPreferences(mypreference,
                            Context.MODE_PRIVATE);
                    try {
                        favorite_json_list = new JSONArray(sharedpreferences.getString("favorite", "[]"));
                    }catch (JSONException ex) {
                        throw new RuntimeException(ex);
                    }
                    for(int j = 0; j < favorite_json_list.length(); ++j){
                        JSONObject each_event;
                        String event_name_in_favorite;
                        try{
                            each_event = favorite_json_list.getJSONObject(j);
                            event_name_in_favorite = each_event.getString("name");
                        }catch (JSONException ex) {
                            throw new RuntimeException(ex);
                        }

                        if(event_name_in_favorite.equals(mData.get(position).getName())){
                            favorite_json_list.remove(j);
                            Log.d("remove favorite", mData.get(position).getName());
                            Log.d("favorite_json_list", favorite_json_list.toString());
                            break;
                        }
                    }

                    // save
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("favorite", favorite_json_list.toString());
                    editor.commit();
                    mData.remove(position);
                    notifyItemRemoved(position);
                    notifyDataSetChanged();

                }
        });

        normalHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("card view", "onClick: card view");
                // intent obj
                Intent intent = new Intent(mContext, MapsActivity.class);
                // pack data
                intent.putExtra("fromFavorite", true);
                intent.putExtra("title", mData.get(position).getName());
                intent.putExtra("venue", mData.get(position).getVenue());
                intent.putExtra("date", mData.get(position).getDate());
                intent.putExtra("artistsTeams", mData.get(position).getArtistsTeams());
                intent.putExtra("category", mData.get(position).getCategory());
                intent.putExtra("categoryDetail", mData.get(position).getCategoryString());
                intent.putExtra("priceRange", mData.get(position).getPriceRange());
                intent.putExtra("ticketStatus", mData.get(position).getTicketStatus());
                intent.putExtra("ticketmaster", mData.get(position).getTicketmasterUrl());
                intent.putExtra("seatmap", mData.get(position).getSeatmapUrl());
                intent.putExtra("favorite", mData.get(position).getIsFavorite().toString());
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
        }

    }

}