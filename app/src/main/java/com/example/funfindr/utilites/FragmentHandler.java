package com.example.funfindr.utilites;

import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.funfindr.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class FragmentHandler {
    private FragmentManager fragmentManager;
    private Fragment currentFragment;
    private List<Fragment> fragments;

    /**
     * Handles the fragments in an activity
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
     * Sets the action to be performed by the floating action button (if it exists) depending on the current fragment
     */
    public void floatingActionButtonHandler(View view, Class fragClass) {
        if(fragClass.isInstance(this.currentFragment))
        {
//            Snackbar.make(view, "This is the events fragment", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show();
        }
        else
        {
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }


}
