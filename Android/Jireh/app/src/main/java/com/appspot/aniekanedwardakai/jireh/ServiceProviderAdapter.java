package com.appspot.aniekanedwardakai.jireh;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by Teddy on 12/29/2016.
 */

public class ServiceProviderAdapter extends ArrayAdapter<ServiceProvider> implements AdapterView.OnItemSelectedListener {

    public ServiceProviderAdapter(Context context, ArrayList<ServiceProvider> serviceProviders){
        super(context, 0, serviceProviders);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        ServiceProvider sp = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.service_provider_request_list_item, parent, false);
        }
        // Lookup view for data population
        TextView spFullName = (TextView) convertView.findViewById(R.id.serviceProviderName);
        TextView spServices = (TextView) convertView.findViewById(R.id.servicesRequestedToOffer);
        TextView spEmail = (TextView) convertView.findViewById(R.id.serviceProvidersEmail);
//        Spinner spStatus = (Spinner) convertView.findViewById(R.id.serviceProviderRequestStatus);
        // Populate the data into the template view using the data object
        spFullName.setText(sp.getFullname());
        spEmail.setText(sp.getEmail());
        spServices.setText(sp.getServicesOffered().toString());

//        ServiceProviderRequestStatus[] spOptions = {new ServiceProviderRequestStatus(sp.getID(), "Pending"),
//                new ServiceProviderRequestStatus(sp.getID(), "Approved"),
//                new ServiceProviderRequestStatus(sp.getID(), "Declined")};

        // Create an ArrayAdapter using the string array and a default spinner layout
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
//              R.array.status_values, android.R.layout.simple_spinner_item);
//        ServiceProviderRequestStatusSpinnerAdapter adapter =
//                new ServiceProviderRequestStatusSpinnerAdapter(getContext(),
//                        android.R.layout.simple_spinner_item, spOptions);
        // Specify the layout to use when the list of choices appears
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
//        spStatus.setAdapter(adapter);
//        spStatus.setOnItemSelectedListener(this);
        // Return the completed view to render on screen
        return convertView;
    }

    /**
     *
     * @param parent
     * @param view
     * @param position refers to
     * @param id
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        JSONObject spjson = new JSONObject();
        JSONArray updates = new JSONArray();

        // An item was selected. You can retrieve the selected item using
        ServiceProviderRequestStatus selectionValue = (ServiceProviderRequestStatus)(parent.getItemAtPosition(position));
//        String selection = (String) parent.getItemAtPosition(position);
        try {
            spjson.put("id",selectionValue.getServiceProviderID());
            updates.put(Utility.generateUpdateJson("requestStatus",selectionValue.getRequestStatusValue()));
            spjson.put("updates", updates);
            String updateUserLocationJSON = spjson.toString();
            TempDB.invokeWSUpdateServiceProvider(updateUserLocationJSON, getContext());

            Log.d("Jireh", "Get all service providers nearby.");
            TempDB.invokeWSGetAllServiceProviders(getContext());

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

