package com.appspot.aniekanedwardakai.jireh;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Teddy on 3/12/2016.
 */
public class Utility {

    private static Pattern pattern;
    private static Matcher matcher;
    protected static int TIMEOUT = 60;

    //Email Pattern
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"+"[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
//    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\+]+(\.[_A-Za-z0-9-]+)*@"
//                    + "[A-Za-z0-9-]+(\.[A-Za-z0-9]+)*(\.[A-Za-z]{2,})$";

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
    /**
     * Checks for Null String object
     *
     * @param txt
     * @return true for not null and false for null String object
     */
    public static boolean isNotNull(String txt){
        return txt!=null && txt.trim().length()>0 ? true: false;
    }

    public static Date toDate(String dateString){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
        Date date = new Date();
        try {
            date = sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String constructJSONFromHashMap(HashMap<String, Object> hashMap)
    {

        return "";
    }
}
