package com.appspot.aniekanedwardakai.jireh;

import android.content.Intent;
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
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Date;

public class DeleteAccountActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Button confirmDeleteButton;
    private Button cancelDeleteButton;
    private MultiAutoCompleteTextView deleteReasonTextArea;
    private User signedInUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        signedInUser = SignedInUser.getUser();

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

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        confirmDeleteButton = (Button) findViewById(R.id.confirmDeleteButton);
        cancelDeleteButton = (Button) findViewById(R.id.cancelDeleteActionButton);
        deleteReasonTextArea = (MultiAutoCompleteTextView) findViewById(R.id.deleteReasonTextArea);

        confirmDeleteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                deleteUser();

            }
        });

        cancelDeleteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent profileIntent = new Intent(getApplicationContext(), UserProfileActivity.class);
                SignedInUser.getInstance().setUser(signedInUser);
                startActivity(profileIntent);
            }
        });

    }

    private synchronized void deleteUser() {

        JSONObject jsonObject = new JSONObject();
        String reason = deleteReasonTextArea.getText().toString();
        try {
            jsonObject.put("id", signedInUser.getID());
            jsonObject.put("email", signedInUser.getEmail());
            jsonObject.put("reason", reason);
            jsonObject.put("time", Utility.getDisplayDate(new Date()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Invoke web service to delete user

        try {
            int time=0;
            boolean result = TempDB.removeUser(jsonObject.toString(), getApplicationContext(), this);
            if(result){
                Toast.makeText(getApplicationContext(),"Account deleted successfully", Toast.LENGTH_LONG).show();
                //sign out
                Utility.signOut(this);
            }else{
                Toast.makeText(getApplicationContext(),"Account deleted failed.", Toast.LENGTH_LONG).show();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
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
        getMenuInflater().inflate(R.menu.delete_account, menu);
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
