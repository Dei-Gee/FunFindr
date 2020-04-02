package com.example.funfindr.fragments;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.funfindr.R;
import com.example.funfindr.utilites.DatabaseHandler;
import com.example.funfindr.utilites.FragmentHandler;
import com.example.funfindr.utilites.SharedPreferencesManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class EventsFragment extends Fragment {


    @Nullable
    @Override
    public android.view.View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_events, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // Shared Preferences
        SharedPreferences sharedPreferences = SharedPreferencesManager.newPreferences("MyPrefs", getContext());
        String email = SharedPreferencesManager.getString(sharedPreferences, "email");
        String userId = DatabaseHandler.getUserId(email);

        ArrayList<Map<String,String>> allEvents = DatabaseHandler.selectAllEvents (userId);

        LinearLayout cardsContainer = (LinearLayout) getActivity().findViewById(R.id.cardsContainer);

        Collections.reverse(allEvents);

        // Floating action button
        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        Drawable drawable = getContext().getResources().getDrawable(R.drawable.ic_add_blue_24dp);
        fab.setImageDrawable(drawable);

        // Fragment Handler
        FragmentManager frag = getActivity().getSupportFragmentManager(); // initializes new Support Fragment Manager
        final FragmentHandler fragHandler = new FragmentHandler(frag); // handles the fragment manager
        fragHandler.setFloatingActionButtonDrawable(fab, fragHandler.getCurrentFragment(), getContext());

        // FLOATING ACTION BUTTON CLICK LISTENER
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragHandler.getCurrentFragment(); // gets the current fragment active in the container

                fragHandler.floatingActionButtonHandler(view, fragHandler.getCurrentFragment().getClass(), new EventsFormFragment());

            }
        });

        for(Map<String,String> event : allEvents)
        {
            CardView card;
            card = (CardView) View.inflate(getActivity(), R.layout.cards_layout_events, null);
            CardView.LayoutParams params = new CardView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0,16,0,16);
            card.setLayoutParams(params);

            // CARD VIEWS
            TextView eventName = card.findViewById(R.id.textViewEventName);
            TextView eventDateTime = card.findViewById(R.id.textViewEventTime);
            TextView eventType = card.findViewById(R.id.textTypeOfEvent);
            TextView eventPlaceName = card.findViewById(R.id.textViewPlaceName);
            Button showEventOnMapButton = card.findViewById(R.id.buttonEventShowOnMap);
            ImageView imageViewDeleteEvent = card.findViewById(R.id.imageViewDeleteButton);
            ImageView imageViewEditEvent = card.findViewById(R.id.imageViewEditButton);

            // formatting date and time
            String[] formattedDateTime = event.get("date").split(" ");
            String[] splitTime = formattedDateTime[1].split(":");

            // Button Methods
            refreshEvents(imageViewDeleteEvent, event.get("_id"));
            editEvent(imageViewEditEvent, event);

            eventName.setText(event.get("title"));
            eventPlaceName.setText(event.get("location"));
            eventDateTime.setText(splitTime[0] + ":"  + splitTime[1] + " (" + formattedDateTime[0] +")");
            eventType.setText(event.get("type"));
            card.invalidate();
            cardsContainer.addView(card);
        }
        super.onViewCreated(view, savedInstanceState);
    }

    private void editEvent(ImageView imageView, final Map<String,String> map) {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle args = new Bundle();
                args.putString("_id", map.get("_id"));
                args.putString("title", map.get("title"));
                args.putString("location", map.get("location"));
                args.putString("date", map.get("date"));
                args.putString("type", map.get("type"));
                args.putString("notes", map.get("notes"));
                args.putString("address", map.get("address"));

                FragmentManager frag = getActivity().getSupportFragmentManager();
                FragmentHandler fragHandler = new FragmentHandler(frag);
                FloatingActionButton fab = getActivity().findViewById(R.id.fab);
                Fragment editFragment = new EventsEditFormFragment();
                editFragment.setArguments(args);
                fragHandler.loadFragment(editFragment, getActivity(), fab);
            }
        });
    }

    /**
     * Deletes and event and refreshes the fragment
     * @param imageView The icon that serves as the delete button
     * @param id The event id
     */
    public void refreshEvents(ImageView imageView, final String id)
    {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FloatingActionButton fab = getActivity().findViewById(R.id.fab);
                if(DatabaseHandler.deleteEvent(id))
                {
                    Toast.makeText(getActivity(), "Event deleted!", Toast.LENGTH_SHORT).show();
                    new FragmentHandler(getActivity().getSupportFragmentManager()).loadFragment(new EventsFragment(), getActivity(), fab);
                }

            }
        });
    }

}
