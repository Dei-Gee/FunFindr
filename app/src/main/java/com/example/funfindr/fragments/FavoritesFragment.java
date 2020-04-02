package com.example.funfindr.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.funfindr.R;
import com.example.funfindr.utilites.adapters.CustomFavoritesAdapter;

public class FavoritesFragment extends Fragment {

    @Nullable
    @Override
    public android.view.View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle favData = getArguments();

//        CustomFavoritesAdapter customFavoritesAdapter = new CustomFavoritesAdapter(getContext(), )

    }
}
