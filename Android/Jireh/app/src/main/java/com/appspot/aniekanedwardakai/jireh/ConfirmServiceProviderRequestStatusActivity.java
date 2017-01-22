package com.appspot.aniekanedwardakai.jireh;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class ConfirmServiceProviderRequestStatusActivity extends AppCompatActivity {

    ServiceProvider serviceProvider = null;
    TextView spName;
    TextView spEmail;
    TextView spServicesToProvide;
    TextView spAccounNumber;
    TextView spInstitutionNo;
    TextView spTransitNo;
    TextView spBusinessAddress;
    TextView spAvailabilityRadius;

    Button approveButton;
    Button declineButton;

    String servicesList = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_service_provider_request_status);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        spName = (TextView) findViewById(R.id.nameOfPendingServiceProvider);
        spEmail = (TextView) findViewById(R.id.emailOfPendingServiceProvider);;
        spServicesToProvide = (TextView) findViewById(R.id.servicesToProideOfPendingServiceProvider);
        spAccounNumber = (TextView) findViewById(R.id.accountNoOfPendingServiceProvider);
        spInstitutionNo = (TextView) findViewById(R.id.institutionNoOfPendingServiceProvider);
        spTransitNo = (TextView) findViewById(R.id.transitNoOfPendingServiceProvider);
        spBusinessAddress = (TextView) findViewById(R.id.addressOfPendingServiceProvider);
        spAvailabilityRadius = (TextView) findViewById(R.id.radiusOfPendingServiceProvider);

        approveButton = (Button) findViewById(R.id.approveServiceProviderRequestStatus);
        declineButton = (Button) findViewById(R.id.declineServiceProviderRequestStatus);

        serviceProvider = Utility.generateServiceProviderFromJSON(getIntent().getStringExtra("selectedServiceProvider"));

        spName.setText(serviceProvider.getFullname());
        spEmail.setText(serviceProvider.getEmail());
        spAccounNumber.setText(serviceProvider.getBankInfo().getAccountNumber());
        spInstitutionNo.setText(serviceProvider.getBankInfo().getInstitutionNumber());
        spTransitNo.setText(serviceProvider.getBankInfo().getTransitNumber());
        spBusinessAddress.setText(serviceProvider.getBusinessAddress());
        spAvailabilityRadius.setText(Double.toString(serviceProvider.getAvailabilityRadius()));

        for(int i=0; i<serviceProvider.getServicesOffered().size(); i++){
            servicesList += serviceProvider.getServicesOffered().get(i);
            if(i!=serviceProvider.getServicesOffered().size()-1)
                servicesList += ",";
        }
        spServicesToProvide.setText(servicesList);

        approveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                new AlertDialog.Builder(v.getContext())
                        .setTitle(serviceProvider.getFullname()+"'s request status update confirmation")
                        .setMessage("Are you sure you want to approve this service provider's request to provide the following services\n"
                                +servicesList)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                JSONObject spjson = new JSONObject();
                                JSONArray updates = new JSONArray();
                                try {
                                    spjson.put("id",serviceProvider.getID());
                                    updates.put(Utility.generateUpdateJson("requestStatus","Approved"));
                                    spjson.put("updates", updates);
                                    String updateUserLocationJSON = spjson.toString();
                                    TempDB.invokeWSUpdateServiceProvider(updateUserLocationJSON, getApplicationContext());

                                    Log.d("Jireh", "Approved.");
                                    TempDB.invokeWSGetAllServiceProviders(getApplicationContext());

                                    goToMain();
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Toast.makeText(v.getContext(), "Approved.", Toast.LENGTH_SHORT).show();
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });

        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                new AlertDialog.Builder(v.getContext())
                        .setTitle(serviceProvider.getFullname()+"'s request status update confirmation")
                        .setMessage("Are you sure you want to decline this service provider's request to provide the following services\n"
                                +servicesList)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                JSONObject spjson = new JSONObject();
                                JSONArray updates = new JSONArray();
                                try {
                                    spjson.put("id",serviceProvider.getID());
                                    updates.put(Utility.generateUpdateJson("requestStatus","Declined"));
                                    spjson.put("updates", updates);
                                    String updateUserLocationJSON = spjson.toString();
                                    TempDB.invokeWSUpdateServiceProvider(updateUserLocationJSON, getApplicationContext());

                                    Log.d("Jireh", "Declined");
                                    TempDB.invokeWSGetAllServiceProviders(getApplicationContext());

                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Toast.makeText(v.getContext(), "Declined.", Toast.LENGTH_SHORT).show();
                                goToMain();
                            }})
                        .setNegativeButton(android.R.string.no, null).show();

            }
        });
    }

    private void goToMain() {
        TempDB.navigatetoLocateServiceActivity(SignedInUser.getUser(), getApplicationContext(), this);
    }
}
