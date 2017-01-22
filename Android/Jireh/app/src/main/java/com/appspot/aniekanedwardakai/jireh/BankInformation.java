package com.appspot.aniekanedwardakai.jireh;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Teddy on 1/15/2017.
 */

public class BankInformation {

    private String accountNumber;
    private  String transitNumber;
    private  String institutionNumber;


    public BankInformation(String accountNumber, String transitNumber, String institutionNumber) {
        this.accountNumber = accountNumber;
        this.transitNumber = transitNumber;
        this.institutionNumber = institutionNumber;
    }

    public BankInformation(JSONObject o) {
        try {
            this.accountNumber = o.getString("accountNumber");
            this.transitNumber = o.getString("transitNumber");
            this.institutionNumber = o.getString("institutionNumber");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getTransitNumber() {
        return transitNumber;
    }

    public void setTransitNumber(String transitNumber) {
        this.transitNumber = transitNumber;
    }

    public String getInstitutionNumber() {
        return institutionNumber;
    }

    public void setInstitutionNumber(String institutionNumber) {
        this.institutionNumber = institutionNumber;
    }

    @Override
    public String toString() {
        return toJSON().toString();
    }

    public JSONObject toJSON(){
        JSONObject bankInfo = new JSONObject();
        try {
            bankInfo.put("accountNumber",accountNumber);
            bankInfo.put("transitNumber", transitNumber);
            bankInfo.put("institutionNumber", institutionNumber);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bankInfo;
    }
}
