package com.example.funfindr.utilites.adapters;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.funfindr.MainUIActivity;
import com.example.funfindr.R;
import com.example.funfindr.database.models.Favorite;
import com.example.funfindr.fragments.FavoritesFragment;
import com.example.funfindr.fragments.GoogleMapFragment;
import com.example.funfindr.utilites.handlers.DatabaseHandler;
import com.example.funfindr.utilites.handlers.FragmentHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class CustomFavoritesAdapter extends ArrayAdapter<Favorite> {
    private Context context;
    private List<Favorite> favoritesList;
    private int layout = R.layout.cards_layout_favorites;
    // Bundle
    private Bundle favoritesBundle = new Bundle();

    public CustomFavoritesAdapter(Context _context, List<Favorite> favorites) {
        super(_context, R.layout.cards_layout_favorites, favorites);

        this.context = _context;
        this.favoritesList = favorites;
    }

    @Override
    public int getCount() {
        return favoritesList.size();
    }

    @Override
    public Favorite getItem(int i) {
        return favoritesList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final MainUIActivity conAct = (MainUIActivity) context;
        final FloatingActionButton fab = conAct.findViewById(R.id.fab);

        CardView v = (CardView) LayoutInflater.from(this.context).inflate(this.layout, null, false);

        TextView postalCode;
        TextView fullAddress;
        TextView adminSubAdmin;
        Button buttonShowOnMap;
        ImageView imageView;

        postalCode = v.findViewById(R.id.textViewPlaceName);
        fullAddress = v.findViewById(R.id.textViewPlaceAddress);
        adminSubAdmin = v.findViewById(R.id.textTypeOfPlace);

        // set the values
        postalCode.setText(favoritesList.get(i).getPostalCode());
        fullAddress.setText(favoritesList.get(i).getAddress());
        adminSubAdmin.setText(favoritesList.get(i).getAdmin() + ", " + favoritesList.get(i).getSubAdmin());

        imageView = v.findViewById(R.id.imageViewDeleteButtonFavorites);
        final String id = favoritesList.get(i).getId();

        /**
         * Deletes favorite and refreshes the fragment
         * @param imageView The icon that serves as the delete button
         * @param id The favorite's id
         */
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("CLICK => ", ""+id);

                SQLiteDatabase database = DatabaseHandler.getWritable(context);
                if(DatabaseHandler.deleteFavorite(database, id))
                {
                    Toast.makeText(context, "Favorite deleted!", Toast.LENGTH_SHORT).show();
                    new FragmentHandler(conAct.getSupportFragmentManager()).loadFragment(new FavoritesFragment(), context, fab);
                }

            }
        });



        buttonShowOnMap = v.findViewById(R.id.buttonShowOnMapFavorites);
        final String favoritesAddress = favoritesList.get(i).getAddress();
        /**
         * Shows the event on the map
         * @param button The show event button
         * @param fHandler The fragment handler
         * @param fbtn the floating action button
         * @param eventAddress The event address
         */
        buttonShowOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                favoritesBundle.putString("favorites_full_address", favoritesAddress);

                // new map fragment
                GoogleMapFragment gmap = new GoogleMapFragment();
                gmap.setArguments(favoritesBundle);
                new FragmentHandler(conAct.getSupportFragmentManager()).loadFragment(gmap, context, fab);
            }
        });

        // set the tag to the id
        v.setTag(favoritesList.get(i).getId());
        v.invalidate();
        return v;
    }




}
