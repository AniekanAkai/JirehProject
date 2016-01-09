package com.appspot.aniekanedwardakai.jireh;

import java.util.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

/**
 * Created by Teddy on 10/10/2015.
 */
public class User {
    private String username;
    private String firstname;
    private String lastname;
    private Date dateOfBirth;
    private UserLocation currentLocation;
    private String phoneNumber;
    private String email;
    private double averageRating; // calculated from coalition of all reviews on user.
    private ArrayList<Service> servicesRequested;
    private ArrayList<Review> reviewsOn;

    public User(String username, String firstname, String lastname, Date dateOfBirth, String phoneNumber, String email) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.email = email;
        servicesRequested = new ArrayList<Service>();
        reviewsOn = new ArrayList<Review>();
        averageRating = 0.0;
        currentLocation = new UserLocation();
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
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

    /*
       Calculate average on the reviews made on the user,
       i.e sum up rating value and divide by number of reviews made.
     */
    public double getCurrentAverageRating() {

        Review selectedReview = new Review();
        double sumOfRatings = 0.0;
        int numberOfReviews = reviewsOn.size();
        if(numberOfReviews==0){
            averageRating = 0.0;
        }else{
            ListIterator<Review> it = reviewsOn.listIterator();
            while(it.hasNext()){
                selectedReview = it.next();
                sumOfRatings += selectedReview.getRating();
            }
            averageRating = sumOfRatings/numberOfReviews;
        }

        return averageRating;
    }

}
