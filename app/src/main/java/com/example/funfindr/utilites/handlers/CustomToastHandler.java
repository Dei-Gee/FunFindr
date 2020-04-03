package com.example.funfindr.utilites.handlers;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class CustomToastHandler {
    private static int DEFAULT_DURATION = Toast.LENGTH_SHORT;
    private Context context;
    private String message;

    public CustomToastHandler(Context _context, String _message)
    {
        this.context = _context;
        this.message = _message;
    }

    public void generateToast(int backgroundColor, int textColor)
    {
        Toast toast = Toast.makeText(context, message, DEFAULT_DURATION);
        View view = toast.getView();
        //Gets the actual oval background of the Toast then sets the colour filter
        view.setBackgroundColor(backgroundColor);

        //Gets the TextView from the Toast so it can be editted
        TextView text = view.findViewById(android.R.id.message);
        text.setTextColor(textColor);

        toast.show();
    }



}
