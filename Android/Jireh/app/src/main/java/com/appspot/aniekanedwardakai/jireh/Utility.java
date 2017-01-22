package com.appspot.aniekanedwardakai.jireh;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Teddy on 3/12/2016.
 */
public class Utility {

    public static final String TAG = "Jireh";
    private static Pattern pattern;
    private static Matcher matcher;
    protected static int TIMEOUT = 60;

    //Email Pattern
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"+"[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    /**
     * Validate Email with regular expression
     *
     * @param email
     * @return true for Valid Email and false for Invalid Email
     */
    public static boolean validate(String email) {
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();

    }

    public static void sleepInSeconds(int seconds){
        try {
            Thread.sleep(seconds*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    /**
     * Checks for Null String object
     *
     * @param txt
     * @return true for not null and false for null String object
     */
    public static boolean isNotNull(String txt){
        return txt!=null && txt.trim().length()>0 ? true: false;
    }

    public static String constructJSONFromHashMap(HashMap<String, Object> hashMap)
    {
        return "";
    }

    public static void displayToastMessage(Context context, String msg){
        Toast.makeText(context,msg, Toast.LENGTH_LONG).show();
    }

    public static void defaultSidebarHandler(MenuItem item, User signedInUser, Activity activity){
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            //Open User details page
            Intent locateIntent = new Intent(activity, UserProfileActivity.class);
            SignedInUser.getInstance().setUser(signedInUser);
            // Clears History of Activity
            //locateIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            activity.startActivity(locateIntent);

        } else if (id == R.id.nav_payment) {

        } else if (id == R.id.nav_history) {

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_signout) {
            Utility.signOut(activity);
        } else if(id == R.id.nav_become_a_service_provider) {
            //Navigate to BecomeAServiceProviderActivity
            Intent intent = new Intent(activity, BecomeAServiceProviderActivity.class);
            SignedInUser.getInstance().setUser(signedInUser);
            activity.startActivity(intent);
        }  else if (id == R.id.nav_serviceproviderrequests) {
            //Navigate to BecomeAServiceProviderActivity
            Intent intent = new Intent(activity, ServiceProviderRequestListActivity.class);
            SignedInUser.getInstance().setUser(signedInUser);
            activity.startActivity(intent);

        }  else if (id == R.id.nav_schedule) {

        }

        DrawerLayout drawer = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    public static JSONObject constructUserJSON(User u)
    {
        JSONObject obj = null;
        try {
            obj = new JSONObject();
            obj.put("id", u.getID());
            obj.put("fullname", u.getFullname());
            obj.put("password", u.getPassword());
            obj.put("phoneNumber", u.getPhoneNumber());
            obj.put("email", u.getEmail());
            obj.put("dob", u.getDateOfBirth().getTime());
            obj.put("currentLocation", u.getCurrentLocationString());
            obj.put("averageRating", u.getCurrentAverageRating());
            obj.put("isAdmin", u.isAdmin());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static JSONObject constructServiceProviderJSON(ServiceProvider sp)
    {
        JSONObject obj = null;
        try {
            obj = constructUserJSON(sp);
            obj.put("availabilityRadius",sp.getAvailabilityRadius());
            obj.put("bankInfo",sp.getBankInfo().toString());
            obj.put("servicesOffered", new JSONArray(sp.getServicesOffered()));
            obj.put("businessAddress", sp.getBusinessAddress());
            obj.put("profilePictureURL", sp.getPhoto());
//            obj.put("numberOfCancellation", sp.getNumberOfCancellations());
//            obj.put("verificationId", sp.getVerificationId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static JSONObject constructServiceJSON(Service s)
    {
        JSONObject obj = null;
        try {
            obj = new JSONObject("");
            obj.put("id", s.getId());
            obj.put("user_id", s.getUser().getID());
            obj.put("serviceprovider_id", s.getServiceProvider().getID());
            obj.put("ratePerHour", s.getRatePerHour());
            obj.put("status", s.getStatus());
            obj.put("scheduledTime", s.getScheduledTime());
            obj.put("startTime", s.getServiceStartTime());
            obj.put("endTime", s.getServiceEndTime());
            obj.put("finalBalance", s.getFinalBalance());
            obj.put("serviceType", s.getServiceType().toString());
            obj.put("userReview", constructUserReviewJSON(s.getUserReview()));
            obj.put("serviceProviderReview", constructServiceProviderReviewJSON(s.getServiceProviderReview()));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    /*
        Used for user's review of the service provider.
     */
    public static JSONObject constructServiceProviderReviewJSON(Review r)
    {
        JSONObject obj = null;
        try {
            obj = new JSONObject("");
            obj.put("service_id", r.getService().getId());
            obj.put("user_id", r.getReviewer().getID());
            obj.put("serviceprovider_id", r.getReviewee().getID());
            obj.put("rating", 0);
            obj.put("comment", "");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    /*
        Used for service provider's review of the user
     */
    public static JSONObject constructUserReviewJSON(Review r)
    {
        JSONObject obj = null;
        try {
            obj = new JSONObject("");
            obj.put("service_id", r.getService().getId());
            obj.put("user_id", r.getReviewee().getID());
            obj.put("serviceprovider_id", r.getReviewer().getID());
            obj.put("rating", 0);
            obj.put("comment", "");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static JSONObject generateUpdateJson(String key, Object value) {
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

    /*
 * Method to generate user from JSON
 *
 */
    public static User generateUserFromJSON(String json) throws JSONException {
        Log.d("Jireh", "Generating user from "+json);
        User u = new User();
        try {
            JSONObject o = new JSONObject(json);
            u = new User(o.getLong("id"),
                    o.getString("fullname"),
                    new SimpleDateFormat("yyyy-MM-dd").parse(o.getString("dob")),
                    o.getString("phoneNumber"),
                    o.getString("email"), o.getString("password"));
            u.setCurrentLocation(toLatLng(o.getString("currentLocation")));
            u.setCurrentRating(o.getDouble("averageRating"));
            u.setAdmin(o.getBoolean("isAdmin"));
        } catch (JSONException e) {
            if(u==null){
                throw e;
            }else {
                e.printStackTrace();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return u;
    }

    /*
     * Method to generate service provider from JSON
     *
     */
    public static ServiceProvider generateServiceProviderFromJSON(String json){
        ServiceProvider sp = new ServiceProvider();
        try {
            JSONObject o = new JSONObject(json);
            sp = new ServiceProvider();
            sp.setID(o.getLong("id"));
            sp.setAvailabilityRadius(o.getDouble("availabilityRadius"));
            sp.setCurrentLocation(toLatLng(o.getString("currentLocation")));
            sp.setBankInfo(new BankInformation(new JSONObject(o.getString("bankInfo"))));
            sp.setBusinessAddress(o.getString("businessAddress"));
            sp.setPhoto(o.getString("profilePhotoURL"));
            JSONArray servicesOffered = o.getJSONArray("servicesOffered");
            Log.d("Jireh","Expected size: "+servicesOffered.length());
            for(int i=0; i<servicesOffered.length(); i++){
                Log.d("Jireh","Service to be offered: "+servicesOffered.getString(i));
                sp.addServicesOffered(servicesOffered.getString(i));
            }
            sp.setEmail(o.getString("email"));
            sp.setPassword(o.getString("password"));
            try {
                sp.setDateOfBirth(new SimpleDateFormat("yyyy-MM-dd").parse(o.getString("dob")));
            } catch (ParseException e) {
                Log.d("Jireh", "Parsing error in generate service provider from JSON.");
                e.printStackTrace();
            }
            sp.setFullname(o.getString("fullname"));//o.put("fullname", sp.getFullname());
            sp.setPhoneNumber(o.getString("phoneNumber"));//o.put("phoneNumber", sp.getPhoneNumber());
            sp.setCurrentRating(o.getDouble("rating"));//o.put("rating", sp.getCurrentAverageRating());
            sp.setAdmin(o.getBoolean("isAdmin"));//o.put("isAdmin", sp.isAdmin());
            sp.setCurrentLocation(toLatLng(o.getString("currentLocation")));//o.put("currentLocation", sp.getCurrentLocation());
    } catch (JSONException e) {
            e.printStackTrace();
        }
        return sp;
    }

    /*
     * Method to generate service from JSON
     *
     */
    public static Service generateServiceFromJSON(String json){
        Service s = new Service();
        try {
            JSONObject o = new JSONObject(json);
            s = new Service(new User(),
                    new ServiceProvider(),
                    o.getString("serviceType"),
                    o.getLong("scheduledTime"),
                    o.getDouble("ratePerHour"),
                    o.getBoolean("userProvidesTool"));
            s.getUser().setID(o.getLong("userid"));
            s.getServiceProvider().setID(o.getLong("serviceproviderid"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return s;
    }


    public static String getDisplayDate(Date date){

        String myFormat = "yyyy/MM/dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        return sdf.format(date);
    }

    public static Date toDate(String dateString){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        try {
            date = sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static LatLng toLatLng(String locationString){

        LatLng location = new LatLng(0,0);

        Log.d(TAG, "Converting the string to LatLng: " + locationString);

        double lon = Double.parseDouble(locationString.split(",")[0]);
        double lat = Double.parseDouble(locationString.split(",")[1]);

        location = new LatLng(lon,lat);
        return location;
    }

    public static void signOut(Context context)
    {
        Intent signOutIntent = new Intent(context, LoginActivity.class);
        SignedInUser.setUser(null);
        signOutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(signOutIntent);
    }
}
