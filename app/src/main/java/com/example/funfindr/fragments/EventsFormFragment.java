package com.example.funfindr.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.funfindr.R;
import com.example.funfindr.data.Event;
import com.example.funfindr.utilites.DatabaseHandler;
import com.example.funfindr.utilites.FragmentHandler;
import com.example.funfindr.utilites.SharedPreferencesManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class EventsFormFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_events_form, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // CUSTOM ADAPTER
        String[] eventTypes = getResources().getStringArray(R.array.event_types);
        ArrayAdapter<String> customAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, eventTypes);

        Spinner eventType = getActivity().findViewById(R.id.spinnerEventType);

        eventType.setAdapter(customAdapter);


        final SharedPreferences sharedPreferences = SharedPreferencesManager.newPreferences("MyPrefs", getActivity());

        final FloatingActionButton fab = getActivity().findViewById(R.id.fab); // floating action button
        fab.setVisibility(View.INVISIBLE);
        Button submitButton = getActivity().findViewById(R.id.buttonSubmit);

        // FIELDS
        final CalendarView eventDate = getActivity().findViewById(R.id.calendarView);
        TimePicker eventTime = getActivity().findViewById(R.id.timePickerEventTime);
        final EditText eventTitle = getActivity().findViewById(R.id.editTextEventName);
        final EditText eventAddress = getActivity().findViewById(R.id.editTextEventAddress);
        final EditText eventLocation = getActivity().findViewById(R.id.editTextEventLocation);
        final EditText eventNotes = getActivity().findViewById(R.id.editTextEventNotes);

        final String eTimeHour = String.valueOf(eventTime.getCurrentHour());
        final String eTimeMinute = String.valueOf(eventTime.getCurrentMinute());
        final String[] eType = new String[1];

        final ArrayList<String> dateOfEvent =  getDateOfEvent(eventDate);
        final ArrayList<String> typeOfEvent =  getTypeOfEvent(eventType);




        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(dateOfEvent.get(0) +" => ", String.valueOf(dateOfEvent.get(0)));
                Log.d(eTimeHour.toUpperCase()+" => ", eTimeHour);
                Log.d(eTimeMinute.toUpperCase()+" => ", eTimeMinute);
                Log.d(typeOfEvent.get(0) +" => ", String.valueOf(typeOfEvent.get(0)));

                Event newEvent = new Event();

                newEvent.setTitle(eventTitle.getText().toString());
                newEvent.setAddress(eventAddress.getText().toString());
                newEvent.setDate(dateOfEvent.get(0) + " " + eTimeHour + ":"+eTimeMinute+":"+"00.000");
                newEvent.setLocation(eventLocation.getText().toString());
                newEvent.setNotes(eventNotes.getText().toString());
                newEvent.setType(typeOfEvent.get(0));

                Log.d("EMAIL => ", SharedPreferencesManager.getString(sharedPreferences, "email"));

                if(DatabaseHandler.createEvent(newEvent, SharedPreferencesManager.getString(sharedPreferences, "email")))
                {
                    Toast.makeText(getActivity(), "New Event Created!", Toast.LENGTH_SHORT).show();
                    new FragmentHandler(getActivity().getSupportFragmentManager()).loadFragment(new EventsFragment(), getActivity(), fab);
                }
                else
                {
                    Toast.makeText(getActivity(), "Failed to create event!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public ArrayList<String> getDateOfEvent(CalendarView calendarView)
    {
        final ArrayList<String> edate = new ArrayList<String>();
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                String monthString = String.valueOf(month);
                String dayString = String.valueOf(dayOfMonth);
                if(month < 10)
                {
                    monthString = "0"+monthString;
                }
                if(dayOfMonth < 10)
                {
                    dayString = "0"+dayString;
                }
                edate.add(String.valueOf(year) + "-" + monthString + "-" + String.valueOf(dayString));
            }
        });
        return edate;
    }

    public ArrayList<String> getTypeOfEvent(Spinner spinner)
    {
        final ArrayList<String> eType = new ArrayList<String>();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                eType.add(0, (String) adapterView.getItemAtPosition(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                eType.add(0, adapterView.getSelectedItem().toString());
            }
        });

        return eType;
    }
}
