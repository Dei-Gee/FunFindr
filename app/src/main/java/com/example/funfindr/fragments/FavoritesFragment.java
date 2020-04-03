package com.example.funfindr.fragments;

import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.funfindr.R;
import com.example.funfindr.database.models.Favorite;
import com.example.funfindr.utilites.adapters.CustomFavoritesAdapter;
import com.example.funfindr.utilites.handlers.DatabaseHandler;
import com.example.funfindr.utilites.handlers.FragmentHandler;
import com.example.funfindr.utilites.handlers.SharedPreferencesManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FavoritesFragment extends Fragment {
    private final String MYPREFERENCES= "MyPrefs";
    private final SQLiteDatabase database = DatabaseHandler.getWritable(getActivity());
    @Nullable
    @Override
    public android.view.View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences sharedPreferences = SharedPreferencesManager.newPreferences(MYPREFERENCES, getContext());
        String userId = SharedPreferencesManager.getString(sharedPreferences, "_id");

        // views
        ListView listViewFavorites = getActivity().findViewById(R.id.listViewFavorites);


        Bundle favArgs = getArguments();
        List<Favorite> favoriteList = new ArrayList<Favorite>();
        ArrayList<Map<String,String>> favData;
        favData = DatabaseHandler.selectAllFavorites(database, userId);
        Favorite newFavorite;

        for(Map<String,String> favorite : favData)
        {
            newFavorite = new Favorite();
            newFavorite.setId(favorite.get("_id"));
            newFavorite.setUserId(userId);
            newFavorite.setAddress(favorite.get("address"));
            newFavorite.setLocality(favorite.get("locality"));
            newFavorite.setPostalCode(favorite.get("postal_code"));
            newFavorite.setAdmin(favorite.get("admin"));
            newFavorite.setSubAdmin(favorite.get("sub_admin"));
            newFavorite.setCountryName(favorite.get("country_name"));
            favoriteList.add(newFavorite);
        }

        CustomFavoritesAdapter customFavoritesAdapter = new CustomFavoritesAdapter(getActivity(), favoriteList);
        listViewFavorites.setAdapter(customFavoritesAdapter);
    }

    /**
     * Deletes favorite and refreshes the fragment
     * @param imageView The icon that serves as the delete button
     * @param id The event id
     */
    public void refreshFavorites(ImageView imageView, final String id)
    {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FloatingActionButton fab = getActivity().findViewById(R.id.fab);
                if(DatabaseHandler.deleteFavorite(database, id))
                {
                    Toast.makeText(getActivity(), "Favorite deleted!", Toast.LENGTH_SHORT).show();
                    new FragmentHandler(getActivity().getSupportFragmentManager()).loadFragment(new FavoritesFragment(), getActivity(), fab);
                }

            }
        });
    }
}
