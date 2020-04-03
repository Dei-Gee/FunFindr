package com.example.funfindr.utilites.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.funfindr.R;
import com.example.funfindr.database.models.Favorite;

import java.util.List;

public class CustomFavoritesAdapter extends ArrayAdapter {
    private Context context;
    private List<Favorite> favoritesList;
    private int layout = R.layout.cards_layout_favorites;

    private TextView postalCode;
    private TextView fullAddress;
    private TextView adminSubAdmin;
    private Button buttonShowOnMap;

    public CustomFavoritesAdapter(Context _context, List<Favorite> favorites)
    {
        super(_context, R.layout.cards_layout_favorites, favorites);
        this.context = _context;
        this.favoritesList = favorites;
    }

    @Override
    public int getCount() {
        return favoritesList.size();
    }

    @Override
    public Object getItem(int i) {
        return favoritesList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
//.findViewById(R.id.imageViewDeleteButtonFavorites)
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v =  View.inflate(this.context, this.layout, null);

        postalCode = v.findViewById(R.id.textViewPlaceName);
        fullAddress = v.findViewById(R.id.textViewPlaceAddress);
        adminSubAdmin = v.findViewById(R.id.textTypeOfPlace);

        // set the values
        postalCode.setText(favoritesList.get(i).getPostalCode());
        fullAddress.setText(favoritesList.get(i).getAddress());
        adminSubAdmin.setText(favoritesList.get(i).getAdmin() + ", " + favoritesList.get(i).getSubAdmin());

        // set the tag to the id
        v.setTag(favoritesList.get(i).getId());
        return v;
    }
}
