package com.example.funfindr;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.example.funfindr.fragments.EventsFormFragment;
import com.example.funfindr.fragments.FavoritesFragment;
import com.example.funfindr.fragments.GoogleMapFragment;
import com.example.funfindr.fragments.EventsFragment;
import com.example.funfindr.utilites.FragmentHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.Menu;

public class MainUIActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_ui);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final FloatingActionButton fab = findViewById(R.id.fab); // floating action button

        // Fragment Handler
        FragmentManager frag = getSupportFragmentManager(); // initializes new Support Fragment Manager
        final FragmentHandler fragHandler = new FragmentHandler(frag); // handles the fragment manager


        // default fragment
        fragHandler.loadFragment(new GoogleMapFragment(), MainUIActivity.this, fab);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragHandler.getCurrentFragment(); // gets the current fragment active in the container

                // determines what this floating action button will do
                if(fragHandler.getCurrentFragment() instanceof EventsFragment)
                {
                    fragHandler.floatingActionButtonHandler(view, fragHandler.getCurrentFragment().getClass(), new EventsFormFragment());
                }
                else if(fragHandler.getCurrentFragment() instanceof FavoritesFragment) {

                }


            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        Fragment curr = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } if(curr != null && curr.getChildFragmentManager().getBackStackEntryCount() > 0) {
            curr.getChildFragmentManager().popBackStack();
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_ui, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentHandler f = new FragmentHandler(fragmentManager);
        FloatingActionButton fb = findViewById(R.id.fab);

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            f.loadFragment(new GoogleMapFragment(), MainUIActivity.this, fb);
        } else if (id == R.id.nav_favorites) {
            f.loadFragment(new FavoritesFragment(), MainUIActivity.this, fb);
        } else if (id == R.id.nav_events) {
            f.loadFragment(new EventsFragment(), MainUIActivity.this, fb);
        } else if (id == R.id.nav_information) {

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_logout) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
