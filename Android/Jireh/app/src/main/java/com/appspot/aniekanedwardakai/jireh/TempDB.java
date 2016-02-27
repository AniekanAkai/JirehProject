package com.appspot.aniekanedwardakai.jireh;

import android.location.GpsStatus;
import android.location.Location;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Teddy on 2/21/2016.
 */
public class TempDB {

    public static ArrayList<User> tempUsers = new ArrayList<User>();
    public static ArrayList<ServiceProvider> tempServiceProviders = new ArrayList<ServiceProvider>();
    public static ArrayList<Service> tempServices = new ArrayList<Service>();
    private static int startCount = 0;

    public static boolean insertUser(User newUser){
        startCount = tempUsers.size();
        tempUsers.add(newUser);
        return (tempUsers.size() == (startCount+1));
    }

    public static boolean insertServiceProvider(ServiceProvider newServiceProvider){
        startCount = tempServiceProviders.size();
        tempServiceProviders.add(newServiceProvider);
        return (tempServiceProviders.size() == (startCount+1));
    }
    public static boolean insertService(Service newService){
        startCount = tempServices.size();
        tempServices.add(newService);
        return (tempServices.size() == (startCount+1));
    }

    public static boolean removeUser(User newUser){
        startCount = tempUsers.size();
        if (startCount==0){
            tempUsers.remove(newUser);
            return (tempUsers.size() == (startCount-1));
        }
        return false;
    }

    public static boolean removeServiceProvider(ServiceProvider newServiceProvider){
        startCount = tempServiceProviders.size();
        if (startCount==0){
            tempServiceProviders.remove(newServiceProvider);
            return (tempServiceProviders.size() == (startCount-1));
        }
        return false;
    }
    public static boolean removeService(Service newService){
        startCount = tempServices.size();
        if (startCount==0){
            tempServices.remove(newService);
            return (tempServices.size() == (startCount-1));
        }
        return false;
    }

    public static boolean updateUser(User user, HashMap<String, Object> updates){
        startCount = tempUsers.size();
        int index= 0;
        Set keys = updates.keySet();
        Iterator it = keys.iterator();
        while(it.hasNext()){
            if(it.next().equals("username")){
                user.setUsername(String.valueOf(updates.get("username")));
            }else if(it.next().equals("name")){
                user.setFullname(String.valueOf(updates.get("name")));
            }else if(it.next().equals("dob")){
                user.setDateOfBirth((Date)updates.get("dob"));
            }else if(it.next().equals("location")){
                user.setCurrentLocation((UserLocation)updates.get("location"));
            }else if(it.next().equals("serviceRequested")){
                user.addServiceRequested((Service) updates.get("serviceRequested"));
            }else if(it.next().equals("reviewsOn")){
                user.addUserReview((Review)updates.get("reviewsOn"));
            }else if(it.next().equals("email")){
                user.setEmail(String.valueOf(updates.get("email")));
            }else if(it.next().equals("phone")){
                user.setPhoneNumber(String.valueOf(updates.get("phone")));
            }
        }
        for(int i=0; i<tempUsers.size();i++){
            if(tempUsers.get(i).getEmail() == user.getEmail()){
                index = i;
                i=tempUsers.size();
            }
        }
        tempUsers.set(index, user);
        return (tempUsers.size()==startCount);
    }

    public static boolean updateServiceProvider(ServiceProvider serviceProvider, HashMap<String, Object> updates){
        int index= 0;
        Set keys = updates.keySet();
        Iterator it = keys.iterator();
        while(it.hasNext()){
            if(it.next().equals("availabiltyRadius")){
                serviceProvider.setAvailabilityRadius((Double) updates.get("availabiltyRadius"));
            }else if(it.next().equals("numberOfCancellations")){
                serviceProvider.setNumberOfCancellations((Integer) updates.get("numberOfCancellations"));
            }else if(it.next().equals("bankInfo")){
                serviceProvider.setBankInfo(String.valueOf(updates.get("bankInfo")));
            }else if(it.next().equals("location")){
                serviceProvider.setLocation((GpsStatus) updates.get("location"));
            }else if(it.next().equals("serviceProvided")){
                serviceProvider.addServiceProvided((Service) updates.get("serviceProvided"));
            }else if(it.next().equals("reviewsOn")){
                serviceProvider.addServiceProviderReview((Review) updates.get("reviewsOn"));
            }else if(it.next().equals("serviceTypeOffering")){
                serviceProvider.addServicesOffered((ServiceType) updates.get("serviceTypeOffering"));
            }
        }

        for(int i=0; i<tempServiceProviders.size();i++){
            if(tempServiceProviders.get(i).getVerificationId() == serviceProvider.getVerificationId()){
                index = i;
                i=tempServiceProviders.size();
            }
        }
        tempServiceProviders.set(index, serviceProvider);
        return (tempServiceProviders.size()==startCount);
    }
    public static boolean updateService(Service service, HashMap<String, Object> updates){
        int index= 0;
        Set keys = updates.keySet();
        Iterator it = keys.iterator();
        while(it.hasNext()){
            if(it.next().equals("scheduledStartTime")){
                service.setScheduledTime((GregorianCalendar) updates.get("scheduledStartTime"));
            }else if(it.next().equals("startTime")){
                service.setServiceStartTime((GregorianCalendar) updates.get("startTime"));
            }else if(it.next().equals("endTime")){
                service.setServiceEndTime((GregorianCalendar) updates.get("endTime"));
            }else if(it.next().equals("serviceLocation")){
                service.setServiceLocation((Location) updates.get("location"));
            }else if(it.next().equals("serviceType")){
                service.setServiceType((ServiceType) updates.get("serviceRequested"));
            }else if(it.next().equals("reviewsOnUser")){
                service.setUserReview((Review) updates.get("reviewsOnUser"));
            }else if(it.next().equals("reviewsOnServiceProvider")){
                service.setServiceProviderReview((Review) updates.get("reviewsOnServiceProvider"));
            }else if(it.next().equals("userProvidesTool")){
                service.setUserProvidesTool((boolean) updates.get("userProvidesTool"));
            }else if(it.next().equals("finalBalance")){
                service.setFinalBalance((double)updates.get("finalBalance"));
            }else if(it.next().equals("specialRequests")){
                service.setSpecialRequests(String.valueOf(updates.get("specialRequests")));
            }else if(it.next().equals("hasServiceStarted")){
                service.setHasStartedService((boolean) updates.get("hasServiceStarted"));
            }else if(it.next().equals("hasServiceCompleted")){
                service.setHasServiceCompleted((boolean) updates.get("hasServiceCompleted"));
            }
        }

        for(int i=0; i<tempServices.size();i++){
            if(tempServices.get(i).getId() == service.getId()){
                index = i;
                i=tempServices.size();
            }
        }
        tempServices.set(index, service);
        return (tempServices.size()==startCount);
    }
}
