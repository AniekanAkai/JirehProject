package com.appspot.aniekanedwardakai.jireh;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.ListIterator;

/**
 * Created by Teddy on 10/10/2015.
 */
public class User{
    private long id=0;

    private boolean isAdmin = false;
    private String fullname;
    private String phoneNumber;
    private Date dateOfBirth;
    private LatLng currentLocation;
    private String email;
    private String password;;
    private double averageRating; // calculated from coalition of all reviews on user.
    private ArrayList<Service> servicesRequested;
    private ArrayList<Review> reviewsOn;
    private double currentRating;


    public User() {
        this.fullname = "";
        this.dateOfBirth = new Date();
        this.phoneNumber = "";
        this.email = "";
        this.password = "";
        servicesRequested = new ArrayList<Service>();
        reviewsOn = new ArrayList<Review>();
        averageRating = 0.0;
        currentLocation = new LatLng(0,0);
    }


    public User(String fullname, Date dateOfBirth, String phoneNumber, String email, String password) {
        this.fullname = fullname;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        servicesRequested = new ArrayList<Service>();
        reviewsOn = new ArrayList<Review>();
        averageRating = 0.0;
        currentLocation = new LatLng(0,0);
    }

    public User(long id, String fullname, Date dateOfBirth, String phoneNumber, String email, String password) {
        this.id = id;
        this.fullname = fullname;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        servicesRequested = new ArrayList<Service>();
        reviewsOn = new ArrayList<Review>();
        averageRating = 0.0;
        currentLocation = new LatLng(0,0);
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public long getID() {
        return id;
    }

    public void setID(long ID) {
        this.id = ID;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /*
       Calculate average on the reviews made on the user,
       i.e sum up rating value and divide by number of reviews made.
     */
    public double getCurrentAverageRating() {
        Review selectedReview = new Review();
        double sumOfRatings = 0.0;
        int numberOfReviews = reviewsOn.size();
        if (numberOfReviews == 0) {
            averageRating = 0.0;
        } else {
            ListIterator<Review> it = reviewsOn.listIterator();
            while (it.hasNext()) {
                selectedReview = it.next();
                sumOfRatings += selectedReview.getRating();
            }
            averageRating = sumOfRatings / numberOfReviews;
        }
        return averageRating;
    }

    public LatLng getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(LatLng currentLocation) {
        this.currentLocation = currentLocation;
    }
    public boolean addServiceRequested(Service serviceRequested){
        return servicesRequested.add(serviceRequested);
    }

    public boolean removeServiceRequested(Service serviceRequested){
        return servicesRequested.remove(serviceRequested);
    }

    public boolean addUserReview(Review userReview){
        return reviewsOn.add(userReview);
    }

    public boolean removeUserReview(Review userReview){
        return reviewsOn.remove(userReview);
    }


    //Methods
    public void requestService(ServiceProvider sp, String type, Long scheduledTime,
                                  double ratePerHour, boolean userProvideTool){

        Service serviceRequest = new Service(this,sp,type,scheduledTime,ratePerHour,userProvideTool);
        serviceRequest.setStatus("Pending Approval");
        TempDB.insertService(serviceRequest);

        //Send email/push notification to the service provider

    }

    public void setCurrentRating(double currentRating) {
        this.currentRating = currentRating;
    }
}
