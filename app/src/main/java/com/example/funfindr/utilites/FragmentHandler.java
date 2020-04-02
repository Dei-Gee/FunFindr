package com.example.funfindr.utilites;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.funfindr.R;
import com.example.funfindr.fragments.EventsFormFragment;
import com.example.funfindr.fragments.EventsFragment;
import com.example.funfindr.fragments.FavoritesFragment;
import com.example.funfindr.fragments.GoogleMapFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

/**
 * Handles the fragments in an activity
 */
public class FragmentHandler {
    private FragmentManager fragmentManager;
    private Fragment currentFragment;
    private List<Fragment> fragments;

    /**
     * Instantiates a new FragmentHandler class
     * @param fragMan This is the fragment manager passed into this class from the activity
     */
    public FragmentHandler(FragmentManager fragMan)
    {
        this.fragmentManager = fragMan;
    }

    /**
     * Gets the current fragment in the fragment container
     * @return the current fragment
     */
    public Fragment getCurrentFragment()
    {
        this.currentFragment = this.fragmentManager.findFragmentById(R.id.content_frame);
        return this.currentFragment;
    }

    /**
     * Gets all fragments using the fragment manager
     * @return A list of all fragments
     */
    public List<Fragment> getAllFragments()
    {
        this.fragments = this.fragmentManager.getFragments();
        return this.fragments;
    }

    /**
     * Loads the default fragment
     * @param fragment The fragment to be loaded
     * @param context The context of the activity that will contain the fragment
     * @param fbtn The FloatingActionButton of the activity
     */
    public void loadFragment(Fragment fragment, Context context, FloatingActionButton fbtn) {
        setFloatingActionButtonDrawable(fbtn, fragment, context);
        if(this.getCurrentFragment() == null){
            this.fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        }
        else{
            this.fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack("tag").commit();
        }
    }


    /**
     * Sets the icon for the Floating Action Button
     * @param f The FloatingActionButton whose eventhandler will be bound to this method
     * @param currFrag The current fragment active in the fragment container
     * @param ctx Th context of the activity
     */
    public void setFloatingActionButtonDrawable(FloatingActionButton f, Fragment currFrag, Context ctx)
    {

        if(GoogleMapFragment.class.isInstance(currFrag)) {
            f.setVisibility(View.VISIBLE);
            Drawable drawable = ctx.getResources().getDrawable(R.drawable.ic_gps_fixed_blue_24dp);
            f.setImageDrawable(drawable);
        }
        else if(EventsFragment.class.isInstance(currFrag) || FavoritesFragment.class.isInstance(currFrag)) {
            f.setVisibility(View.VISIBLE);
            Drawable drawable = ctx.getResources().getDrawable(R.drawable.ic_add_blue_24dp);
            f.setImageDrawable(drawable);
        }
        else {
            f.setVisibility(View.INVISIBLE);
        }

    }

    /**
     * Sets the action to be performed by the floating action button (if it exists) depending on the current fragment
     * @param view The view of the activity
     * @param fragClass The current fragment will be checked to see if it is an instance of this class
     * @param newFrag The new fragment to be loaded into the fragment container
     */
    public void floatingActionButtonHandler(View view, Class fragClass, Fragment newFrag) {

        if(EventsFragment.class.isInstance(this.currentFragment))
        {
            this.fragmentManager.beginTransaction().replace(R.id.content_frame, newFrag).addToBackStack("tag").commit();;
        }


    }



}
