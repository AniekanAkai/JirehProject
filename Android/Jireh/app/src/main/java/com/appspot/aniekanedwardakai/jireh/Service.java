package com.appspot.aniekanedwardakai.jireh;

import android.location.Location;
//import com.google.android.gms.location;
import com.google.android.gms.maps.model.LatLng;

import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Teddy on 10/10/2015.
 */
class Service{
    private String id;
    private User user; //User that requests the service.
    private ServiceProvider serviceProvider; //Service provider that would be supporting this.
    private String serviceType; //the type of service being created


    private Long scheduledTime; //Time user and service provider have settled on.
    private Long serviceStartTime;
    private Long serviceEndTime;

    private Review userReview;
    private Review serviceProviderReview;

    private String specialRequests;
    private LatLng serviceLocation; //Location where user

    private double finalBalance; //Calculated from the ratePerHpur
    private double ratePerHour; //This is the price that would be shown.

    private boolean userProvidesTool = false; //Specifies if the user or the service provider would be providing tools.
    private boolean hasStartedService = false;
    private boolean hasServiceCompleted = false;

    private String status = "";//Pending Approval, Approved, Started, In Progress, Complete


    public Service() {
        this.user = new User();
        this.serviceProvider = new ServiceProvider();
        this.serviceType = "";
        this.scheduledTime = new Date().getTime();
        this.ratePerHour = 0.0;

        this.userProvidesTool = true;
    }
    public Service(User user, ServiceProvider serviceProvider, String serviceType,
                   Long scheduledTime, double ratePerHour, boolean userProvidesTool) {
        this.user = user;
        this.serviceProvider = serviceProvider;
        this.serviceType = serviceType;
        this.scheduledTime = scheduledTime;
        this.ratePerHour = ratePerHour;

        this.userProvidesTool = userProvidesTool;
    }

    //Getters and Setters
    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public Long getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(Long scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public Long getServiceStartTime() {
        return serviceStartTime;
    }

    public void setServiceStartTime(Long serviceStartTime) {
        this.serviceStartTime = serviceStartTime;
    }

    public Long getServiceEndTime() {
        return serviceEndTime;
    }

    public void setServiceEndTime(Long serviceEndTime) {
        this.serviceEndTime = serviceEndTime;
    }

    public Review getUserReview() {
        return userReview;
    }

    public void setUserReview(Review userReview) {
        this.userReview = userReview;
    }

    public Review getServiceProviderReview() {
        return serviceProviderReview;
    }

    public void setServiceProviderReview(Review serviceProviderReview) {
        this.serviceProviderReview = serviceProviderReview;
    }

    public String getSpecialRequests() {
        return specialRequests;
    }

    public void setSpecialRequests(String specialRequests) {
        this.specialRequests = specialRequests;
    }

    public LatLng getServiceLocation() {
        return serviceLocation;
    }

    public void setServiceLocation(LatLng serviceLocation) {
        this.serviceLocation = serviceLocation;
    }

    public double getFinalBalance() {
        return finalBalance;
    }

    public void setFinalBalance(double finalBalance) {
        this.finalBalance = finalBalance;
    }

    public double getRatePerHour() {
        return ratePerHour;
    }

    public void setRatePerHour(double ratePerHour) {
        this.ratePerHour = ratePerHour;
    }

    public boolean isUserProvidesTool() {
        return userProvidesTool;
    }

    public void setUserProvidesTool(boolean userProvidesTool) {
        this.userProvidesTool = userProvidesTool;
    }

    public boolean isHasStartedService() {
        return hasStartedService;
    }

    public void setHasStartedService(boolean hasStartedService) {
        this.hasStartedService = hasStartedService;
    }

    public boolean isHasServiceCompleted() {
        return hasServiceCompleted;
    }

    public void setHasServiceCompleted(boolean hasServiceCompleted) {
        this.hasServiceCompleted = hasServiceCompleted;
    }

    //Pending Approval, Approved, Started, In Progress, Complete
    public String getStatus() {
        return status;
    }

    //Pending Approval, Approved, Started, In Progress, Complete
    public void setStatus(String status) {
        this.status = status;
    }

}
