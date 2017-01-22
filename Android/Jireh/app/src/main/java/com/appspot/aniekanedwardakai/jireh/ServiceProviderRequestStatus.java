package com.appspot.aniekanedwardakai.jireh;

/**
 * Created by Teddy on 1/9/2017.
 */

public class ServiceProviderRequestStatus {

    long ServiceProviderID = 0;
    String requestStatusValue = "Pending";

    public ServiceProviderRequestStatus(long serviceProviderID, String requestStatusValue) {
        ServiceProviderID = serviceProviderID;
        this.requestStatusValue = requestStatusValue;
    }

    public void setServiceProviderID(long serviceProviderID) {
        ServiceProviderID = serviceProviderID;
    }

    public void setRequestStatusValue(String requestStatusValue) {
        this.requestStatusValue = requestStatusValue;
    }


    public long getServiceProviderID() {

        return ServiceProviderID;
    }

    public String getRequestStatusValue() {
        return requestStatusValue;
    }
}
