package com.appspot.aniekanedwardakai.jireh;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class UserProfileActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private User signedInUser;
    private JSONObject userjson;
    JSONArray updates;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private AutoCompleteTextView mFullNameView;
    private EditText mDOBView;
    private EditText mPasswordView;
    private EditText mConfirmPasswordView;
    private View mProgressView;
    private View mUpdateView;
    private AutoCompleteTextView mPhoneView;
    private String myFormat = "MM/dd/yy"; //In which you need put here
    private SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
    private TextView userFullName_header;
    private TextView userEmail_header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        
        signedInUser = SignedInUser.getInstance().getUser();
        userjson = new JSONObject();
        updates = new JSONArray();

        try {
            userjson.put("id", signedInUser.getID());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mEmailView.setText(signedInUser.getEmail());
        mFullNameView = (AutoCompleteTextView) findViewById(R.id.full_name);
        mFullNameView.setText(signedInUser.getFullname());
        mDOBView = (EditText) findViewById(R.id.dateOfBirth);
        mDOBView.setText(Utility.getDisplayDate(signedInUser.getDateOfBirth()));
        mPhoneView = (AutoCompleteTextView) findViewById(R.id.phoneNumber);
        mPhoneView.setText(signedInUser.getPhoneNumber());

        mPasswordView = (EditText) findViewById(R.id.password);
        mConfirmPasswordView = (EditText) findViewById(R.id.confirmpassword);

        Button mUpdateButton = (Button) findViewById(R.id.user_update_button);
        mUpdateButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(!mEmailView.getText().toString().equals(signedInUser.getEmail())){
                        updates.put(generateUpdateJson("email", mEmailView.getText().toString()));
                        signedInUser.setEmail(mEmailView.getText().toString());
                    }
                    if(!mFullNameView.getText().toString().equals(signedInUser.getFullname())){
                        updates.put(generateUpdateJson("fullname", mFullNameView.getText()));
                        signedInUser.setFullname(mFullNameView.getText().toString());
                    }
                    if(!Utility.toDate(mDOBView.getText().toString()).equals(signedInUser.getDateOfBirth())){
                        updates.put(generateUpdateJson("dateOfBirth", mDOBView.getText().toString()));
                        signedInUser.setDateOfBirth(Utility.toDate(mDOBView.getText().toString()));
                    }
                    if(!mPhoneView.getText().toString().equals(signedInUser.getPhoneNumber())){
                        updates.put(generateUpdateJson("phonenumber", mPhoneView.getText()));
                        signedInUser.setPhoneNumber(mPhoneView.getText().toString());
                    }
                    if(!mPasswordView.getText().toString().equals(signedInUser.getPassword())){
                        updates.put(generateUpdateJson("password", mPasswordView.getText()));
                        signedInUser.setPassword(mPasswordView.getText().toString());
                    }
                    userjson.put("updates", updates);
                    updateUser(userjson);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        Button mDeleteButton = (Button) findViewById(R.id.user_delete_button);
        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent deleteIntent = new Intent(getApplicationContext(), DeleteAccountActivity.class);
                startActivity(deleteIntent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        userFullName_header = (TextView) findViewById(R.id.userFullName_Header);
//        userFullName_header.setText(signedInUser.getFullname());
        userEmail_header = (TextView) findViewById(R.id.userEmail_Header);
//        userEmail_header.setText(signedInUser.getEmail());

    }

    private void updateUser(JSONObject o) {
        try {
            signedInUser = TempDB.invokeWSUpdateUser(o.toString(), getApplicationContext(), this);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private JSONObject generateUpdateJson(String key, Object value) {
        try {
            JSONObject o = new JSONObject();
            o.put("key", key);
            o.put("value", value);
            return o;
        } catch ( JSONException e){
            e.printStackTrace();
        }
        return null;
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
        getMenuInflater().inflate(R.menu.user_profile, menu);
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
