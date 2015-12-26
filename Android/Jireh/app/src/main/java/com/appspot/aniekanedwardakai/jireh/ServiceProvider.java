package com.appspot.aniekanedwardakai.jireh;

import android.location.GpsStatus;
import android.location.Location;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Teddy on 10/10/2015.
 */
public class ServiceProvider extends User {

    private GpsStatus location;
    private int availabiltyRadius=0;
    private String verificationId;
    private int numberOfCancellations;
    private int bankInfo;         //acc number, swift no, bank id etc
    private ServiceType[] servicesOffered;
    private ArrayList<Service> servicesProvided; //get from service ClassID
    private ArrayList<Review> reviewsOnUser;      // get from service class userReview


    public ServiceProvider(String username, String firstname, String lastname, Date dateOfBirth,
                           String phoneNumber, String email, GpsStatus location, int availabiltyRadius,
                           String verificationId,int bankInfo,ServiceType[] servicesOffered){
        super(username, firstname, lastname, dateOfBirth, phoneNumber, email);
        this.location = location;
        this.availabiltyRadius = availabiltyRadius;
        this.verificationId = verificationId;
        this.bankInfo = bankInfo;
        this.servicesOffered = servicesOffered;
    }
    //GPS location
    public GpsStatus getLocation() {
        return location;
    }

    public void setLocation(GpsStatus location) {
        this.location = location;
    }

    //verification

    public String getVerificationId() {
        return verificationId;
    }

    public void setVerificationId(String verificationId) {
        this.verificationId = verificationId;
    }


    // number of Cancellations

    public int getNumberOfCancellations() {
        return numberOfCancellations;
    }

    public void setNumberOfCancellations(int numberOfCancellations) {
        this.numberOfCancellations = numberOfCancellations;
    }

    //bankInfo
    public int getBankInfo() {
        return bankInfo;
    }

    public void setBankInfo(int bankInfo) {
        this.bankInfo = bankInfo;
    }

    //Service being provided
    public ServiceType[] getServicesOffered() {
        return servicesOffered;
    }

    public void setServicesOffered(ServiceType[] servicesOffered) {
        this.servicesOffered = servicesOffered;
    }

    public boolean addServiceProvided(Service serviceProvided){
        return servicesProvided.add(serviceProvided);
    }

    public boolean removeServiceProvided(Service serviceProvided){
        return servicesProvided.remove(serviceProvided);
    }

    public boolean addUserReview(Review userReview){
        return reviewsOnUser.add(userReview);
    }

    public boolean removeUserReview(Review userReview){
        return reviewsOnUser.remove(userReview);
    }

    /* methods*/
    public void createService(ServiceType serviceType, Location service, Schedule schedule){}  //serviceType, serviceLocation, schedule)
    public void enterAvailability(Schedule spSchedule){} //spSchedule
    public GpsStatus locateUser(){return null;}
    public void createProfile(){}
    public void editProfile(){}

    public void specifyAvailabilityRadius(int radius){
        availabiltyRadius = radius;
    }

    public int getAvailabilityRadius(){return availabiltyRadius;}

    public void makeCommentOnUser(){}
    public void specifyPreferenceToUser(){}
}
