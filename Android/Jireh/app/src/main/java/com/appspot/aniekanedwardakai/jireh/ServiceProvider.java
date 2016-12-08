package com.appspot.aniekanedwardakai.jireh;

import android.location.GpsStatus;
import android.location.Location;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Teddy on 10/10/2015.
 */
public class ServiceProvider extends User {

    private String photo;
    private String businessAddress;
    private double availabiltyRadius=0;
    private long verificationId=0; //ServiceProvider id(from DB) would be used here.
    private int numberOfCancellations;
    private String bankInfo;         //acc number, swift no, bank id etc
    private List<String> servicesOffered;
    private ArrayList<Service> servicesProvided; //get from service ClassID
    private ArrayList<Review> reviewsOnUser;      // get from service class userReview


    public ServiceProvider(long id, String fullname, Date dateOfBirth,
                           String phoneNumber, String email, String password, String businessAddress, double availabiltyRadius,
                           long verificationId, String bankInfo, List<String> servicesOffered){
        super(id, fullname, dateOfBirth, phoneNumber, email, password);
        this.businessAddress = businessAddress;
        this.availabiltyRadius = availabiltyRadius;
        this.verificationId = verificationId;
        this.bankInfo = bankInfo;
        this.servicesOffered = servicesOffered;
    }

    public ServiceProvider(User u, double availabiltyRadius, long verificationId,String bankInfo,List<String> servicesOffered){
        super(u.getID(), u.getFullname(), u.getDateOfBirth(), u.getPhoneNumber(), u.getEmail(), u.getPassword());
        this.availabiltyRadius = availabiltyRadius;
        this.verificationId = verificationId;
        this.bankInfo = bankInfo;
        this.servicesOffered = servicesOffered;
    }

    public ServiceProvider() {

    }

    //GPS businessAddress
    public String getBusinessAddress() {
        return businessAddress;
    }

    public void setBusinessAddress(String businessAddress) {
        this.businessAddress = businessAddress;
    }

    //verification

    public long getVerificationId() {
        return verificationId;
    }

    public void setVerificationId(long verificationId) {
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

    public boolean removeServicesOffered(String servicetype) {
        return servicesOffered.remove(servicetype);
    }

    public boolean addServicesOffered(String servicetype) {
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
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

    public List<String> getServicesOffered() {
        return servicesOffered;
    }
}
