package com.appspot.aniekanedwardakai.jireh;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.util.Log;

import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.common.net.MediaType;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mysql.jdbc.StringUtils;

import org.apache.commons.mail.EmailException;
import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Teddy on 2/21/2016.
 */
public class TempDB {

    public static ArrayList<User> adminUsers = new ArrayList<User>();
    public static ArrayList<ServiceProvider> serviceProvidersNearby = new ArrayList<ServiceProvider>();
    public static ArrayList<Service> tempServices = new ArrayList<Service>();
    private static int startCount = 0;
    private static User user = null;
    protected static ModifiableBooleanValue success = new ModifiableBooleanValue(false);
    protected static JSONObject wsResponse = new JSONObject();

    public static boolean insertServiceProvider(ServiceProvider newServiceProvider, final Context context, final Activity activity) throws UnsupportedEncodingException {
        JSONObject json = Utility.constructServiceProviderJSON(newServiceProvider);
        Log.d("Jireh", "Service Provider JSON: "+ json);
        StringEntity se = new StringEntity(json.toString());
        AsyncHttpClient client = new AsyncHttpClient();
        client.put(context, "http://104.196.60.217:8080/restfulTest/serviceProvider/create", se, MediaType.JSON_UTF_8.toString(),
//        client.put(context, "http://192.168.0.112:8080/restfulTest/serviceProvider/create", se, MediaType.JSON_UTF_8.toString(),
                new AsyncHttpResponseHandler(){
                    // When the response returned by REST has Http response code '200'
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        // Hide Progress Dialog
                        try {
                            String response = StringUtils.toAsciiString(responseBody);
                            // JSON Object
                            Log.d("Jireh", response);
                            wsResponse = new JSONObject(response);

                            // When the JSON response has status boolean value assigned with true
                            if(wsResponse.getBoolean("status")){
                                // Display successfully registered message using Toast
                                Toast.makeText(context, "Service Provider request is successfully created!", Toast.LENGTH_LONG).show();

                                success = new ModifiableBooleanValue(true);
                                Intent completed = new Intent(context, ServiceProviderRequestCompleteActivity.class);
                                completed.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(completed);



                            }
                            // Else display error message
                            else{
                                //errorMsg.setText(wsResponse.getString("error_msg"));
                                Toast.makeText(context, "Response status = false.\n"+wsResponse.getString("error_msg"), Toast.LENGTH_LONG).show();
                                success = new ModifiableBooleanValue(false);
                            }
                        } catch (JSONException e) {
                            Toast.makeText(context, "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                            success = new ModifiableBooleanValue(false);
                        }
                    }
                    // When the response returned by REST has Http response code other than '200'
                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] response, Throwable error) {
                        // Hide Progress Dialog
                        // prgDialog.hide();
                        // When Http response code is '404'
                        if(statusCode == 404){
                            Toast.makeText(context, "Requested resource not found", Toast.LENGTH_LONG).show();
                            success = new ModifiableBooleanValue(false);
                        }
                        // When Http response code is '500'
                        else if(statusCode == 500){
                            Toast.makeText(context, "Something went wrong at server end", Toast.LENGTH_LONG).show();
                            success = new ModifiableBooleanValue(false);
                        }
                        // When Http response code other than 404, 500
                        else{
                            Toast.makeText(context, "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                            success = new ModifiableBooleanValue(false);
                        }
                    }
                });
        return success.getValue();
    }
    public static boolean insertService(Service newService){
        //TODO Update to use webservice call
        startCount = tempServices.size();
        tempServices.add(newService);
        return (tempServices.size() == (startCount+1));
    }

    public static boolean removeUser(String json, final Context context, final Activity activity) throws UnsupportedEncodingException {
        // Make RESTful webservice call using AsyncHttpClient object
        Log.d("Jireh", "Deleting User JSON: "+ json);
        StringEntity se = new StringEntity(json);
        AsyncHttpClient client = new AsyncHttpClient();
        client.put(context, "http://104.196.60.217:8080/restfulTest/user/delete", se, MediaType.JSON_UTF_8.toString(),
                new AsyncHttpResponseHandler(){
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                // Hide Progress Dialog
                try {
                    String response = StringUtils.toAsciiString(responseBody);
                    // JSON Object
                    wsResponse = new JSONObject(response);
                    Log.d("Jireh", response);
                    // When the JSON response has status boolean value assigned with true
                    if(wsResponse.getBoolean("status")){
                        // Display successfully registered message using Toast
                        Toast.makeText(context, "User is successfully deleted!", Toast.LENGTH_LONG).show();
                        success = new ModifiableBooleanValue(true);
                        //sign out
                        Utility.signOut(context);
                    }
                    // Else display error message
                    else{
                        Toast.makeText(context, "Response status = false.\n"+wsResponse.getString("error_msg"), Toast.LENGTH_LONG).show();
                        success = new ModifiableBooleanValue(false);
                    }
                } catch (JSONException e) {
                    Toast.makeText(context, "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                    success = new ModifiableBooleanValue(false);
                }
            }
            // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                // Hide Progress Dialog
                // prgDialog.hide();
                // When Http response code is '404'
                if(statusCode == 404){
                    Toast.makeText(context, "Requested resource not found", Toast.LENGTH_LONG).show();
                    success = new ModifiableBooleanValue(false);
                }
                // When Http response code is '500'
                else if(statusCode == 500){
                    Toast.makeText(context, "Something went wrong at server end", Toast.LENGTH_LONG).show();
                    success = new ModifiableBooleanValue(false);
                }
                // When Http response code other than 404, 500
                else{
                    Toast.makeText(context, "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                    success = new ModifiableBooleanValue(false);
                }
            }
        });
        return success.getValue();
    }

    public static boolean removeServiceProvider(ServiceProvider newServiceProvider){
        //TODO Update to use webservice call
        startCount = serviceProvidersNearby.size();
        if (startCount==0){
            serviceProvidersNearby.remove(newServiceProvider);
            return (serviceProvidersNearby.size() == (startCount-1));
        }
        return false;
    }
    public static boolean removeService(Service newService){
        //TODO Update to use webservice call
        startCount = tempServices.size();
        if (startCount==0){
            tempServices.remove(newService);
            return (tempServices.size() == (startCount-1));
        }
        return false;
    }

    public static boolean updateServiceProvider(ServiceProvider serviceProvider, HashMap<String, Object> updates){
        //TODO Update to use webservice call
        return false;
//        int index= 0;
//        Set keys = updates.keySet();
//        Iterator it = keys.iterator();
//        while(it.hasNext()){
//            if(it.next().equals("availabiltyRadius")){
//                serviceProvider.setAvailabilityRadius((Double) updates.get("availabiltyRadius"));
//            }else if(it.next().equals("numberOfCancellations")){
//                serviceProvider.setNumberOfCancellations((Integer) updates.get("numberOfCancellations"));
//            }else if(it.next().equals("bankInfo")){
//                serviceProvider.setBankInfo(String.valueOf(updates.get("bankInfo")));
//            }else if(it.next().equals("location")){
//                serviceProvider.setBusinessAddress(updates.get("location").toString());
//            }else if(it.next().equals("serviceProvided")){
//                serviceProvider.addServiceProvided((Service) updates.get("serviceProvided"));
//            }else if(it.next().equals("reviewsOn")){
//                serviceProvider.addServiceProviderReview((Review) updates.get("reviewsOn"));
//            }else if(it.next().equals("serviceTypeOffering")){
//                serviceProvider.addServicesOffered(updates.get("serviceTypeOffering").toString());
//            }
//        }
//
//        for(int i=0; i<tempServiceProviders.size();i++){
//            if(tempServiceProviders.get(i).getVerificationId() == serviceProvider.getVerificationId()){
//                index = i;
//                i=tempServiceProviders.size();
//            }
//        }
//        tempServiceProviders.set(index, serviceProvider);
//        return (tempServiceProviders.size()==startCount);
    }
//    public static boolean updateService(Service service, HashMap<String, Object> updates){
        //TODO Update to use webservice call
//        int index= 0;
//        Set keys = updates.keySet();
//        Iterator it = keys.iterator();
//        while(it.hasNext()){
//            if(it.next().equals("scheduledStartTime")){
//                service.setScheduledTime((Long) updates.get("scheduledStartTime"));
//            }else if(it.next().equals("startTime")){
//                service.setServiceStartTime((GregorianCalendar) updates.get("startTime"));
//            }else if(it.next().equals("endTime")){
//                service.setServiceEndTime((GregorianCalendar) updates.get("endTime"));
//            }else if(it.next().equals("serviceLocation")){
//                service.setServiceLocation((LatLng) updates.get("location"));
//            }else if(it.next().equals("serviceType")){
//                service.setServiceType((ServiceType) updates.get("serviceRequested"));
//            }else if(it.next().equals("reviewsOnUser")){
//                service.setUserReview((Review) updates.get("reviewsOnUser"));
//            }else if(it.next().equals("reviewsOnServiceProvider")){
//                service.setServiceProviderReview((Review) updates.get("reviewsOnServiceProvider"));
//            }else if(it.next().equals("userProvidesTool")){
//                service.setUserProvidesTool((boolean) updates.get("userProvidesTool"));
//            }else if(it.next().equals("finalBalance")){
//                service.setFinalBalance((double)updates.get("finalBalance"));
//            }else if(it.next().equals("specialRequests")){
//                service.setSpecialRequests(String.valueOf(updates.get("specialRequests")));
//            }else if(it.next().equals("hasServiceStarted")){
//                service.setHasStartedService((boolean) updates.get("hasServiceStarted"));
//            }else if(it.next().equals("hasServiceCompleted")){
//                service.setHasServiceCompleted((boolean) updates.get("hasServiceCompleted"));
//            }
//        }
//
//        for(int i=0; i<tempServices.size();i++){
//            if(tempServices.get(i).getId() == service.getId()){
//                index = i;
//                i=tempServices.size();
//            }
//        }
//        tempServices.set(index, service);
//        return (tempServices.size()==startCount);
//    }

    /**
     * Method that performs RESTful webservice invocations
     * @param params
     */
    protected static User invokeWSLogin(RequestParams params, final Context context, final Activity activity){
        // Show Progress Dialog
        //prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object

        AsyncHttpClient client = new AsyncHttpClient();

        client.get("http://104.196.60.217:8080/restfulTest/login/dologin", params, new AsyncHttpResponseHandler() {
//       client.get("http://192.168.0.112:8080/restfulTest/login/dologin", params, new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                // Hide Progress Dialog
                // prgDialog.hide();
                try {
                    String response = StringUtils.toAsciiString(responseBody);
                    // JSON Object
                    wsResponse = new JSONObject(response);

                    Log.d("Jireh", response);
                    // When the JSON response has status boolean value assigned with true
                    if (wsResponse.getBoolean("status")) {
                        JSONArray adminsJSON = wsResponse.getJSONArray("admins");
                        for(int i=0; i<adminsJSON.length(); i++){
                            User adminUser = Utility.generateUserFromJSON(adminsJSON.getString(i));
                            adminUsers.add(adminUser);
                        }

                        user = new User();
                        user.setID(wsResponse.getLong("id"));
                        user.setFullname(wsResponse.getString("fullname"));
                        user.setPassword(wsResponse.getString("password"));
                        user.setEmail(wsResponse.getString("email"));
                        user.setPhoneNumber(wsResponse.getString("phoneNumber"));
                        user.setDateOfBirth(new SimpleDateFormat("yyyy-MM-dd").parse(wsResponse.getString("dob")));
                        user.setAdmin(wsResponse.getBoolean("isAdmin"));

                        if(wsResponse.getBoolean("asServiceProvider")) {
                            SignedInUser.setSignedInAsServiceProvider(true);
                            ArrayList<String> servicesOffered = new ArrayList<>();
                            JSONArray jsonArray = wsResponse.getJSONArray("servicesOffered");
                            for(int i=0; i<jsonArray.length(); i++){
                                servicesOffered.add(jsonArray.getString(i));
                            }

                            ServiceProvider sp = new ServiceProvider(user,
                                    wsResponse.getDouble("availabilityRadius"),
                                    wsResponse.getLong("verificationId"),
                                    wsResponse.getString("bankInfo"),
                                    servicesOffered);
                            sp.setPhoto(wsResponse.getString("profilePhotoURL"));
                            sp.setNumberOfCancellations(wsResponse.getInt("numberOfCancellations"));
                            sp.setBusinessAddress(wsResponse.getString("businessAddress"));
                        }else{
                            SignedInUser.setSignedInAsServiceProvider(false);
                        }

                        // Display successfully registered message using Toast
                        Toast.makeText(context, "You are successfully logged in!", Toast.LENGTH_LONG).show();
                        navigatetoLocateServiceActivity(user, context, activity);
                    }
                    // Else display error message
                    else {
                        Toast.makeText(context, "Response status = false.\n" + wsResponse.getString("error_msg"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(context, "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                } catch (ParseException e) {
                    Toast.makeText(context, "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }

            // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] response, Throwable error) {
                // Hide Progress Dialog
                // prgDialog.hide();
                // When Http response code is '404'
                if (statusCode == 404) {
                    Toast.makeText(context, "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if (statusCode == 500) {
                    Toast.makeText(context, "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else {
                    Toast.makeText(context, "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }
            }
        });
        return user;
    }
    /**
     * Method that performs RESTful webservice invocations
     * @param params
     */
    protected static boolean invokeWSRegister(final RequestParams params, final Context context, final Activity activity){
        // Show Progress Dialog
        //prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object
        Log.d("Jireh", "Getting admins first.");
        TempDB.retrieveAllAdmins(new RequestParams(), context, activity);
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://104.196.60.217:8080/restfulTest/register/doregister",params ,new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                // Hide Progress Dialog
                // prgDialog.hide();
                try {
                    String response = StringUtils.toAsciiString(responseBody);
                    // JSON Object
                    wsResponse = new JSONObject(response);
                    Log.d("Jireh", response);
                    // When the JSON response has status boolean value assigned with true
                    if(wsResponse.getBoolean("status")){
                        // Display successfully registered message using Toast
                        Toast.makeText(context, "You are successfully registered!", Toast.LENGTH_LONG).show();

                        String msgContent = "The following user has been registered successfully.\nNew User details: " + params.toString();
                        params.remove("dob");
                        params.remove("phone");
                        params.remove("name");


                        NotificationUtility.sendToAllAdmins("New user registered successfully.", msgContent);

                        TempDB.invokeWSLogin(params, context, activity);
                    }
                    // Else display error message
                    else{
                        Toast.makeText(context, "Response status = false.\n"+wsResponse.getString("error_msg"), Toast.LENGTH_LONG).show();
                        success = new ModifiableBooleanValue(false);
                    }
                } catch (JSONException e) {
                    Toast.makeText(context, "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                } catch (EmailException e) {
                    e.printStackTrace();
                }
            }
            // When the response returned by REST has Http response code other than '200'
            @Override
            //public void onFailure(int statusCode, Header[] headers, byte[] response, Throwable error) {
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                // Hide Progress Dialog
                // prgDialog.hide();
                // When Http response code is '404'
                String response = StringUtils.toAsciiString(responseBody);
                if(statusCode == 404){
                    Toast.makeText(context, "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if(statusCode == 500){
                    Toast.makeText(context, "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else{
                    Toast.makeText(context, "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }
                success = new ModifiableBooleanValue(false);
            }
        });

        return success.getValue();
    }


    protected static void invokeWSGetAllServiceProviders(final Context context){

        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        params.put("user_id", SignedInUser.getUser().getID());
        params.put("currentLocation", SignedInUser.getUser().getCurrentLocationString());

      client.get("http://104.196.60.217:8080/restfulTest/serviceProvider/getServiceProviders", params, new AsyncHttpResponseHandler() {
//        client.get("http://192.168.0.112:8080/restfulTest/serviceProvider/getServiceProviders", params, new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                // Hide Progress Dialog
                // prgDialog.hide();
                try {
                    String response = StringUtils.toAsciiString(responseBody);
                    // JSON Object
                    wsResponse = new JSONObject(response);

                    Log.d("Jireh", response);
                    // When the JSON response has status boolean value assigned with true
                    if (wsResponse.getBoolean("status")) {
                        serviceProvidersNearby.clear();
                        JSONArray serviceProvidersListJSON = wsResponse.getJSONArray("serviceProvidersList");
                        for(int i=0; i<serviceProvidersListJSON.length(); i++){

                            ServiceProvider serviceProviderNearby = Utility.generateServiceProviderFromJSON(serviceProvidersListJSON.getString(i));
                            serviceProviderNearby.setFullname(new JSONObject(serviceProvidersListJSON.getString(i)).getString("fullname"));
                            serviceProvidersNearby.add(serviceProviderNearby);
                        }
                        // Display successfully registered message using Toast
                        Toast.makeText(context, "All Service Providers available to the user gotten.", Toast.LENGTH_LONG).show();
                    }
                    // Else display error message
                    else {
                        Toast.makeText(context, "Response status = false.\n" + wsResponse.getString("error_msg"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(context, "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }

            // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] response, Throwable error) {
                // Hide Progress Dialog
                // prgDialog.hide();
                // When Http response code is '404'
                if (statusCode == 404) {
                    Toast.makeText(context, "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if (statusCode == 500) {
                    Toast.makeText(context, "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else {
                    Toast.makeText(context, "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    protected static User invokeWSUpdateUser(String jsonParams, final Context context, final Activity activity) throws UnsupportedEncodingException {
        Log.d("Jireh", "User JSON: "+ jsonParams);
        StringEntity se = new StringEntity(jsonParams); //"application/json");
        AsyncHttpClient client = new AsyncHttpClient();
        client.put(context, "http://104.196.60.217:8080/restfulTest/user/update", se, MediaType.JSON_UTF_8.toString(),
                new AsyncHttpResponseHandler(){
                    //              When the response returned by REST has Http response code '200'
                    @Override
                    public void onSuccess(String response) {
                        try {
                            // JSON Object
                            wsResponse = new JSONObject(response);

                            Log.d("Jireh", response);

                            // When the JSON response has status boolean value assigned with true
                            if (wsResponse.getBoolean("status")) {
                                System.out.println(wsResponse.toString());
                                // Display successfully registered message using Toast
                                Toast.makeText(context, "Signed in user successfully updated.", Toast.LENGTH_LONG).show();
                            }
                            // Else display error message
                            else {
                                Toast.makeText(context, "Response status = false.\n" + wsResponse.getString("error_msg"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(context, "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }

                    // When the response returned by REST has Http response code other than '200'
                    @Override
                    public void onFailure(int statusCode, Throwable error, String content) {
                        // Hide Progress Dialog
                        // prgDialog.hide();
                        // When Http response code is '404'
                        if (statusCode == 404) {
                            Toast.makeText(context, "Requested resource not found", Toast.LENGTH_LONG).show();
                        }
                        // When Http response code is '500'
                        else if (statusCode == 500) {
                            Toast.makeText(context, "Something went wrong at server end", Toast.LENGTH_LONG).show();
                        }
                        // When Http response code other than 404, 500
                        else {
                            Toast.makeText(context, "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                        }
                    }
                });
        return user;
    }


    public static void navigatetoLocateServiceActivity(User u, Context context, Activity activity){
        Intent locateIntent = new Intent(context, AvailableServicesActivity.class);
        SignedInUser.getInstance().setUser(u);
        //locateIntent.putExtra("signedInUser",u);
        // Clears History of Activity
        //locateIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(locateIntent);
    }


    public static boolean retrieveAllAdmins(final RequestParams params, final Context context, final Activity activity){
        // Show Progress Dialog
        //prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object

        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://104.196.60.217:8080/restfulTest/user/getAllAdmins",params ,new AsyncHttpResponseHandler() {
//        client.get("http://192.168.0.112:8080/restfulTest/user/getAllAdmins", params ,new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                // Hide Progress Dialog
                // prgDialog.hide();
                try {
                    String response = StringUtils.toAsciiString(responseBody);
                    // JSON Object
                    wsResponse = new JSONObject(response);
                    Log.d("Jireh", response);
                    // When the JSON response has status boolean value assigned with true
                    if(wsResponse.getBoolean("status")){
                        // Display successfully registered message using Toast
                        Toast.makeText(context, "All admins received!", Toast.LENGTH_LONG).show();
                        JSONArray adminsJSON = wsResponse.getJSONArray("admins");
                        for(int i=0; i<adminsJSON.length(); i++){
                            User adminUser = Utility.generateUserFromJSON(adminsJSON.getString(i));
                            adminUsers.add(adminUser);
                        }
                    }
                    // Else display error message
                    else{
                        Toast.makeText(context, "Response status = false.\n"+wsResponse.getString("error_msg"), Toast.LENGTH_LONG).show();
                        success = new ModifiableBooleanValue(false);
                    }
                } catch (JSONException e) {
                    Toast.makeText(context, "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
            // When the response returned by REST has Http response code other than '200'
            @Override
            //public void onFailure(int statusCode, Header[] headers, byte[] response, Throwable error) {
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                // Hide Progress Dialog
                // prgDialog.hide();
                // When Http response code is '404'
//                String response = StringUtils.toAsciiString(responseBody);
                if(statusCode == 404){
                    Toast.makeText(context, "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if(statusCode == 500){
                    Toast.makeText(context, "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else{
                    Toast.makeText(context, "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }
                success = new ModifiableBooleanValue(false);
            }
        });

        return success.getValue();
    }
}