package com.example.funfindr.fragments;

import android.content.SharedPreferences;
import android.os.Build;
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
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.funfindr.R;
import com.example.funfindr.data.Event;
import com.example.funfindr.utilites.DatabaseHandler;
import com.example.funfindr.utilites.FragmentHandler;
import com.example.funfindr.utilites.SharedPreferencesManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class EventsEditFormFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_events_form_edit, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // GET SHARED PREFERENCES
        final SharedPreferences sharedPreferences = SharedPreferencesManager.newPreferences("MyPrefs", getActivity());

        // GET BUNDLE ARGUMENTS
        final String evId = getArguments().getString("_id");
        String evTitle = getArguments().getString("title");
        String evAddress = getArguments().getString("address");
        String evLocation = getArguments().getString("location");
        String evNotes = getArguments().getString("notes");
        String evType = getArguments().getString("type");
        String evDate = getArguments().getString("date");


        // formatting date and time
        final String[] formattedDateTime = evDate.split(" ");
        String[] splitTime = formattedDateTime[1].split(":");


        final FloatingActionButton fab = getActivity().findViewById(R.id.fab); // floating action button
        Button submitButton = getActivity().findViewById(R.id.buttonSubmitEdit); // submission button

        // FIELDS
        final CalendarView eventDate = getActivity().findViewById(R.id.calendarViewEventEdit);
        TimePicker eventTime = getActivity().findViewById(R.id.timePickerEventTimeEdit);
        final EditText eventTitle = getActivity().findViewById(R.id.editTextEventNameEdit);
        final EditText eventAddress = getActivity().findViewById(R.id.editTextEventAddressEdit);
        final EditText eventLocation = getActivity().findViewById(R.id.editTextEventLocationEdit);
        final EditText eventNotes = getActivity().findViewById(R.id.editTextEventNotesEdit);
        Spinner eventType = getActivity().findViewById(R.id.spinnerEventTypeEdit);

        // date to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        try {
            date = sdf.parse(formattedDateTime[0]);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        // PRESET VALUES
        eventDate.setDate(date.getTime(), true, true);
        eventTime.setCurrentHour(Integer.parseInt(splitTime[0]));
        eventTime.setCurrentMinute(Integer.parseInt(splitTime[1]));
        eventTitle.setText(evTitle);
        eventLocation.setText(evLocation);
        eventNotes.setText(evNotes);
        eventAddress.setText(evAddress);
        eventType.setSelection(((ArrayAdapter)eventType.getAdapter()).getPosition(evType));


        String eTimeHour = String.valueOf(eventTime.getCurrentHour());
        String eTimeMinute = String.valueOf(eventTime.getCurrentMinute());


        final String[] eType = new String[1];

        final ArrayList<String> dateOfEvent =  getDateOfEvent(eventDate);
        final ArrayList<String> timeOfEvent =  getTimeOfEvent(eventTime);
        final ArrayList<String> typeOfEvent =  getTypeOfEvent(eventType);

        String time = "";

        if(timeOfEvent == null || timeOfEvent.size() == 0)
        {
            time = " " + eTimeHour + ":"+ eTimeMinute +":"+"00.000";
        }
        else
        {
            time = " " + timeOfEvent.get(0) + ":"+ timeOfEvent.get(1) +":"+"00.000";
        }


        final String finalTime = time;
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Event newEvent = new Event();

                newEvent.setTitle(eventTitle.getText().toString());
                newEvent.setAddress(eventAddress.getText().toString());
                if(dateOfEvent == null || dateOfEvent.size() == 0)
                {
                    newEvent.setDate(formattedDateTime[0] + finalTime);
                }
                else{
                    newEvent.setDate(dateOfEvent.get(0) + finalTime);
                }
                newEvent.setLocation(eventLocation.getText().toString());
                newEvent.setNotes(eventNotes.getText().toString());
                newEvent.setType(typeOfEvent.get(0));


                // data to passed in the update method
                HashMap<String,String> data = new HashMap<>();

                data.put("title", newEvent.getTitle());
                data.put("location", newEvent.getLocation());
                data.put("date", newEvent.getDate());
                data.put("type", newEvent.getType());
                data.put("notes", newEvent.getNotes());
                data.put("address", newEvent.getAddress());

                String userId = DatabaseHandler.getUserId(SharedPreferencesManager.getString(sharedPreferences, "email"));

                if(DatabaseHandler.updateEvent(evId, data))
                {
                    Toast.makeText(getActivity(), "Event has been updated!", Toast.LENGTH_SHORT).show();
                    new FragmentHandler(getActivity().getSupportFragmentManager()).loadFragment(new EventsFragment(), getActivity(), fab);
                }
                else
                {
                    Toast.makeText(getActivity(), "Failed to update event!", Toast.LENGTH_SHORT).show();
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
                edate.add(year + "-" + monthString + "-" + dayString);
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

    public ArrayList<String> getTimeOfEvent(TimePicker timePicker)
    {
        final ArrayList<String> eTime = new ArrayList<String>();
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hour, int minute) {
                String hourString = "";
                String minuteString = "";

                if(hour < 10)
                {
                    hourString = "0" + hour;
                }
                if(minute < 10)
                {
                    minuteString = "0" + minuteString;
                }
                eTime.add(0, hourString);
                eTime.add(1, minuteString);
            }
        });

        return eTime;
    }


}
