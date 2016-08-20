package com.appspot.aniekanedwardakai.jireh;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

public class ServiceProviderRequestCompleteActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private User signedInUser;
    MenuItem becomeAServiceProvider;
    MenuItem schedule;
    MenuItem serviceProviderRequests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_provider_request_complete);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        signedInUser = SignedInUser.getUser();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        MenuItem becomeAServiceProvider = menu.findItem(R.id.nav_become_a_service_provider);
        MenuItem schedule = menu.findItem(R.id.nav_schedule);
        MenuItem serviceProviderRequests = menu.findItem(R.id.nav_serviceproviderrequests);


        //Set menu items to hide when signed in as admin.
        if(!signedInUser.isAdmin()){
            serviceProviderRequests.setVisible(false);
        }

        //Set menu items to hide when not signed in as service provider
        if(!SignedInUser.isSignedInAsServiceProvider()){
            schedule.setVisible(false);
            becomeAServiceProvider.setVisible(true);
        }
        navigationView.setNavigationItemSelectedListener(this);

        Button continueAsUser = (Button) findViewById(R.id.continueAsUser);
        continueAsUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMain();
            }
        });

        Button updateServiceProvider = (Button) findViewById(R.id.serviceProviderInformation);
        updateServiceProvider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Jireh", "Service providers to be updated.");
            }
        });
    }

    private void goToMain() {
        TempDB.navigatetoLocateServiceActivity(SignedInUser.getUser(), getApplicationContext(), this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.service_provider_request_complete, menu);

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
        // Handle navigation view item clicks here.
        Utility.defaultSidebarHandler(item, signedInUser, this);
        return true;
    }
}