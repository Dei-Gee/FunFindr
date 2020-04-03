package com.example.funfindr.fragments;

import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
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

import com.example.funfindr.R;
import com.example.funfindr.SignupActivity;
import com.example.funfindr.database.models.Event;
import com.example.funfindr.utilites.handlers.CustomToastHandler;
import com.example.funfindr.utilites.handlers.DatabaseHandler;
import com.example.funfindr.utilites.handlers.FragmentHandler;
import com.example.funfindr.utilites.handlers.SharedPreferencesManager;
import com.example.funfindr.utilites.handlers.SharedPreferencesManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EventsFormFragment extends Fragment {

    private final SQLiteDatabase database = DatabaseHandler.getWritable(getActivity());

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


        String eTimeHour = String.valueOf(eventTime.getCurrentHour());
        String eTimeMinute = String.valueOf(eventTime.getCurrentMinute());


        final String[] eType = new String[1];

        final ArrayList<String> dateOfEvent =  getDateOfEvent(eventDate);
        final ArrayList<String> timeOfEvent =  getTimeOfEvent(eventTime);
        final ArrayList<String> typeOfEvent =  getTypeOfEvent(eventType);

        String time = "";

        // Chekcs the value of the time of event
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

                // Checks the value of the event's date
                if(dateOfEvent == null || dateOfEvent.size() == 0)
                {
                    new CustomToastHandler(getContext(),
                            "Please select a date and time!").
                            generateToast(R.color.design_default_color_error, R.color.colorWhite);
                }
                else
                {
                    newEvent.setDate(dateOfEvent.get(0) + finalTime);
                }

                newEvent.setLocation(eventLocation.getText().toString());
                newEvent.setNotes(eventNotes.getText().toString());
                newEvent.setType(typeOfEvent.get(0));

                // make sure input is not empty
                if(newEvent.getAddress().length() == 0 || newEvent.getDate().length() == 0 || newEvent.getLocation().length() == 0 ||
                newEvent.getTitle().length() == 0 || newEvent.getType().length() == 0)
                {
                    new CustomToastHandler(getContext(),
                            "Please complete the form!").generateToast(getResources().getColor(R.color.design_default_color_error), getResources().getColor(R.color.colorWhite));

                }
                else if(newEvent.getAddress() == null || newEvent.getDate() == null || newEvent.getLocation() == null ||
                        newEvent.getTitle() == null || newEvent.getType() == null)
                {
                    new CustomToastHandler(getContext(),
                            "Please complete the form!").
                            generateToast(getResources().getColor(R.color.design_default_color_error), getResources().getColor(R.color.colorWhite));
                }
                else
                {
                    if(DatabaseHandler.createEvent(database, newEvent, SharedPreferencesManager.getString(sharedPreferences, "email")))
                    {
                        new CustomToastHandler(getContext(),
                                "New Event Created!").generateToast(getResources().getColor(R.color.quantum_googgreenA700), getResources().getColor(R.color.colorWhite));
                        new FragmentHandler(getActivity().getSupportFragmentManager()).loadFragment(new EventsFragment(), getActivity(), fab);
                    }
                    else
                    {
                        new CustomToastHandler(getContext(),
                                "Failed to create event!").generateToast(getResources().getColor(R.color.quantum_googgreenA700), getResources().getColor(R.color.colorWhite));

                    }
                }


            }
        });
    }

    /**
     * Gets the date of the event through the CalendarView's OnDateChangeLIstener() method
     * @param calendarView The CalendarView
     * @return returns and ArrayList with the values of the date
     */
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

    /**
     * Gets the type of event through the Spinner's OnItemSelectedListener() method
     * @param spinner The Spinner
     * @return returns and ArrayList with the values of the type
     */
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

    /**
     * Gets the time of the event through the TimePicker's OnTimeChangedListener() method
     * @param timePicker The TimePicker
     * @return returns and ArrayList with the values of the time
     */
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
