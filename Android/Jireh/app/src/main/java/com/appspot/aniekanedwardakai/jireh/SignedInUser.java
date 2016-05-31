package com.appspot.aniekanedwardakai.jireh;

/**
 * Created by Teddy on 5/1/2016.
 */
public class SignedInUser {

    private static User user;
    private static SignedInUser ourInstance = new SignedInUser();

    public static SignedInUser getInstance() {
        return ourInstance;
    }

    private SignedInUser() {
        user = new User();
    }

    public static void setUser(User u){
        user =  u;
    }

    public static User getUser(){
        return user;
    }
}
