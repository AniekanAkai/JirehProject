package com.appspot.aniekanedwardakai.jireh;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.TypedArrayUtils;
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
import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class BecomeAServiceProviderActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_become_aservice_provider_temp);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        final User signedInUser = SignedInUser.getInstance().getUser();

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


        Button okBtn = (Button) findViewById(R.id.becomeServiceProviderButton);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String servicesToProvide = ((EditText)findViewById(R.id.servicesToProide)).getText().toString();
                double availabilityRadius = Double.parseDouble(((EditText)findViewById(R.id.radius)).getText().toString());
                String accountNo = ((EditText)findViewById(R.id.accountNo)).getText().toString();
                String transitNo = ((EditText)findViewById(R.id.transitNo)).getText().toString();
                String institutionNo = ((EditText)findViewById(R.id.institutionNo)).getText().toString();
                String profilePhoto = ((EditText)findViewById(R.id.profilePhotoLink)).getText().toString();
                String address = ((EditText) findViewById(R.id.address)).getText().toString();

//                JSONObject bankInfo = new JSONObject();
//                try {
//                    bankInfo.put("accountNumber",accountNo);
//                    bankInfo.put("transitNumber", transitNo);
//                    bankInfo.put("institutionNumber", institutionNo);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }

                ServiceProvider sp = new ServiceProvider(signedInUser.getID(),
                        signedInUser.getFullname(), signedInUser.getDateOfBirth(),
                        signedInUser.getPhoneNumber(), signedInUser.getEmail(),
                        signedInUser.getPassword(), address,
                        availabilityRadius, 0, new BankInformation(accountNo, transitNo, institutionNo),
                        Arrays.asList(servicesToProvide.split(",")));
                sp.setPhoto(profilePhoto);

                Log.d(Utility.TAG, "ServiceProvider created: " + sp.toString());

                try {
                    TempDB.insertServiceProvider(sp, getApplicationContext(), getParent());

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
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
        getMenuInflater().inflate(R.menu.become_aservice, menu);
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
       Utility.defaultSidebarHandler(item, SignedInUser.getUser(), this);
        return true;
    }
}
