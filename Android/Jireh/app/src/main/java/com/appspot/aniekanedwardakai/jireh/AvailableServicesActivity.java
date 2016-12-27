package com.appspot.aniekanedwardakai.jireh;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_CONTACTS;

public class AvailableServicesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback,
        ConnectionCallbacks, OnConnectionFailedListener, LocationListener {


    private static final int ACCESS_FINE_LOCATION_CODE = 1337;
    public static final String TAG = AvailableServicesActivity.class.getSimpleName();
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2 * 1000; /* 2 sec */


    private GoogleMap mMap;
    private User signedInUser;
    private TextView userFullName_header;
    private TextView userEmail_header;
    private GoogleApiClient mGoogleApiClient = null;
    private LatLng currentCoordinates;
    private Location userLocation;
    private LocationRequest mLocationRequest;
    private boolean permissionRequested = false;

    private JSONObject userjson = new JSONObject();
    private JSONArray updates = new JSONArray();

//    private ArrayList<LatLng> availableServiceProviders = new ArrayList<LatLng>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_services);

        if (checkPlayServices()) {
            Log.d(TAG, "Play Services available.");
            while (!checkPermission(ACCESS_FINE_LOCATION) && !permissionRequested) {
                requestPermissions(new String[]{ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION_CODE);
                try{
                    Thread.sleep(1000);
                }catch(Exception e){
                    Log.d(TAG, e.getMessage());
                    e.printStackTrace();
                }
            }
            buildGoogleApiClient();
        }
//        requestPermissions(new String[]{ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION_CODE);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        signedInUser = SignedInUser.getInstance().getUser();

//        if (checkPermission(ACCESS_FINE_LOCATION)) {
//            setCurrentLocation();
//        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Current Location: " + userLocation.toString(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

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
        }else{
            schedule.setVisible(true);
            becomeAServiceProvider.setVisible(false);
        }
        navigationView.setNavigationItemSelectedListener(this);

        userFullName_header = (TextView) findViewById(R.id.userFullName_Header);
        userEmail_header = (TextView) findViewById(R.id.userEmail_Header);


        //Get all the services in the user defined radius, that are within the service provider's availability radius.
        TempDB.invokeWSGetAllServiceProviders(getApplicationContext());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
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
        getMenuInflater().inflate(R.menu.available_services, menu);
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

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//        currentCoordinates = signedInUser.getCurrentLocation();
        if (checkPermission(ACCESS_FINE_LOCATION)) {
            mMap.setMyLocationEnabled(true);
        } else {
            mMap.setMyLocationEnabled(false);
        }

        Log.d(TAG, "Map ready, showing current location, " + signedInUser.getCurrentLocation().toString());
        // Add a marker in Sydney and move the camera
        try {
            mMap.addMarker(new MarkerOptions().position(signedInUser.getCurrentLocation()).title("You're here."));

            for(int i=0; i<TempDB.serviceProvidersNearby.size(); i++){
                ServiceProvider sp = TempDB.serviceProvidersNearby.get(i);
                mMap.addMarker(new MarkerOptions().position(sp.getCurrentLocation()).title(sp.getFullname()+" Services Offered: "+sp.getServicesOffered()));
            }

            mMap.moveCamera(CameraUpdateFactory.newLatLng(signedInUser.getCurrentLocation()));
        }catch(Exception e){
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        }
    }

    private void updateUserMarker()
    {
        Log.d("Jireh", "New Location updated to " + signedInUser.getCurrentLocation().toString());
        mMap.clear();

        mMap.addMarker(new MarkerOptions().position(signedInUser.getCurrentLocation()).title("You're here."));

        for(int i=0; i<TempDB.serviceProvidersNearby.size(); i++){
            ServiceProvider sp = TempDB.serviceProvidersNearby.get(i);
            mMap.addMarker(new MarkerOptions().position(sp.getCurrentLocation()).title(sp.getFullname()+" Services Offered: "+sp.getServicesOffered()));
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLng(signedInUser.getCurrentLocation()));

        // Add a marker for all the service providers in the available radius


    }

    private boolean checkPermission(String permission) {
        return (ContextCompat.checkSelfPermission(getApplicationContext(), permission) == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "Google API Client connnected.");
        startLocationUpdates();
        setCurrentLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (i == CAUSE_SERVICE_DISCONNECTED) {
            Toast.makeText(this, "Disconnected. Will re-connect.", Toast.LENGTH_SHORT).show();
        } else if (i == CAUSE_NETWORK_LOST) {
            Toast.makeText(this, "Network lost. Will re-connect.", Toast.LENGTH_SHORT).show();
        }
        Log.d(TAG, "Google API Client connnection suspended. Will reconnect.");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "Google API Client failed. Error message: " + connectionResult.getErrorMessage());
        Toast.makeText(getApplicationContext(), connectionResult.getErrorMessage(), Toast.LENGTH_LONG);
    }

    @Override
    public void onResume() {
        super.onResume();
//        if (checkPlayServices()) {
//            buildGoogleApiClient();
//        }
    }

    @Override
    protected void onStop() {

        // Disconnecting the client invalidates it.
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

        // only stop if it's connected, otherwise we crash
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        Log.d(TAG, "Location updates removed and google api client disconnected.");
        super.onStop();
    }


    /**
     * Creating google api client object
     * */
    protected synchronized void buildGoogleApiClient() {
        Log.d(TAG, "GoogleAPIClient is being built.");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    /**
     * Method to verify google play services on the device
     * */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(), "This device is not supported.", Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Method to display the location on UI
     * */
    private void setCurrentLocation() {
        userjson = new JSONObject();
        updates = new JSONArray();

        if(checkPermission(ACCESS_FINE_LOCATION)) {
            userLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        }else{
            Log.d(TAG, "Permission not granted. No location retrieved.");
            Toast.makeText(getApplicationContext(),
                    "(Couldn't get the location. Location permission not granted.)",
                    Toast.LENGTH_LONG).show();
        }
        if (userLocation != null) {
            double latitude = userLocation.getLatitude();
            double longitude = userLocation.getLongitude();

            currentCoordinates = new LatLng(latitude, longitude);
        } else {

            Toast.makeText(getApplicationContext(),
                    "(Couldn't get the location. Make sure location is enabled on the device)",
                    Toast.LENGTH_LONG).show();
            currentCoordinates = new LatLng(-1, -1);
        }
        signedInUser.setCurrentLocation(currentCoordinates);
        //Set current location on the database.
        try {
            userjson.put("id", signedInUser.getID());
            updates.put(Utility.generateUpdateJson("currentLocation",currentCoordinates.latitude+","+currentCoordinates.longitude));
            userjson.put("updates", updates);
            String updateUserLocationJSON = userjson.toString();
            TempDB.invokeWSUpdateUser(updateUserLocationJSON, getApplicationContext(), this);

            Log.d("Jireh", "Get all available service providers");
            TempDB.invokeWSGetAllServiceProviders(getApplicationContext());

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ACCESS_FINE_LOCATION_CODE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "Permission Granted to access fine location. ");
                    // permission was granted, yay!
                    setCurrentLocation();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Log.d(TAG, "Permission Denied to access fine location. ");
                    Toast.makeText(getApplicationContext(),
                            "Permission denied, navigate to location manually.",
                            Toast.LENGTH_LONG).show();

                }
                return;
        }
    }

    protected void startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        // Request location updates
        if (checkPermission(ACCESS_FINE_LOCATION)) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        } else{
            Log.d(TAG, "Location permission declined, no updates retrieved.");
            Toast.makeText(this, "Location permission needed to get updates", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        // New location has now been determined
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        // You can now create a LatLng Object for use with maps
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        if(!currentCoordinates.equals(latLng)) {
            Log.d(TAG, "User moved, location changing.");
            userLocation.set(location);
            setCurrentLocation();
            updateUserMarker();
        }
    }
}
