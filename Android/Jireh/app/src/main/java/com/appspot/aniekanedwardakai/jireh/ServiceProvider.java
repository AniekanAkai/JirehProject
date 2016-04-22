package com.appspot.aniekanedwardakai.jireh;

import android.location.GpsStatus;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Teddy on 10/10/2015.
 */
public class ServiceProvider extends User {


    private LatLng location;
    private double availabiltyRadius=0;
    private String verificationId; //ServiceProvider id(from DB) would be used here.
    private int numberOfCancellations;
    private String bankInfo;         //acc number, swift no, bank id etc
    private ArrayList<ServiceType> servicesOffered;
    private ArrayList<Service> servicesProvided; //get from service ClassID
    private ArrayList<Review> reviewsOnUser;      // get from service class userReview


    public ServiceProvider(long id, String fullname, Date dateOfBirth,
                           String phoneNumber, String email, String password, LatLng location, double availabiltyRadius,
                           String verificationId,String bankInfo,ArrayList<ServiceType> servicesOffered){
        super(id, fullname, dateOfBirth, phoneNumber, email, password);
        this.location = location;
        this.availabiltyRadius = availabiltyRadius;
        this.verificationId = verificationId;
        this.bankInfo = bankInfo;
        this.servicesOffered = servicesOffered;
    }
    //GPS location
    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    //verification

    public String getVerificationId() {
        return verificationId;
    }

//    public void setVerificationId(String verificationId) {
//        this.verificationId = verificationId;
//    }


    // number of Cancellations
    public int getNumberOfCancellations() {
        return numberOfCancellations;
    }

    public void setNumberOfCancellations(int numberOfCancellations) {
        this.numberOfCancellations = numberOfCancellations;
    }

    //bankInfo
    public String getBankInfo() {
        return bankInfo;
    }

    public void setBankInfo(String bankInfo) {
        this.bankInfo = bankInfo;
    }

    //Service being provided
    public void requestNewServiceTypetoOffer(ServiceType servicetype){
        if(!servicesOffered.contains(servicetype)){
            //Send request to Admin
        }
    }

    public boolean removeServicesOffered(ServiceType servicetype) {
        return servicesOffered.remove(servicetype);
    }

    public boolean addServicesOffered(ServiceType servicetype) {
        if(!servicesOffered.contains(servicetype)){
            servicesOffered.add(servicetype);
        }
        return false;
    }

    public boolean addServiceProvided(Service serviceProvided){
        return servicesProvided.add(serviceProvided);
    }

    public boolean removeServiceProvided(Service serviceProvided){
        return servicesProvided.remove(serviceProvided);
    }

    public boolean addServiceProviderReview(Review review){
        return reviewsOnUser.add(review);
    }

    public boolean removeServiceProviderReview(Review review){
        return reviewsOnUser.remove(review);
    }

    /* methods*/
    public void createService(ServiceType serviceType, Location service, Schedule schedule){}
    public void enterAvailability(Schedule spSchedule){} //spSchedule
    public GpsStatus locateUser(){return null;}
    public void createProfile(){}
    public void editProfile(){}

    public void setAvailabilityRadius(double radius){
        availabiltyRadius = radius;
    }

    public double getAvailabilityRadius(){return availabiltyRadius;}

    public void makeCommentOnUser(){}
    public void specifyPreferenceToUser(){}

}
