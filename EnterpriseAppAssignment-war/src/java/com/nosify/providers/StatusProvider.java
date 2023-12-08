package com.nosify.providers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class StatusProvider {
    private static final HashMap<Integer, String> status = statusInitalization();
    
    public static final int PENDING = 0;
    public static final int PROCESSING = 1;
    public static final int SHIPPED = 2;
    public static final int DELIVERIED = 3;
    
    public static final int CANCEL_BY_USER = 10;
    public static final int CANCEL_DUE_TO_INVALID_USER_INFORMATION = 11;
    public static final int CANCEL_DUE_TO_DELIVERY_PROBLLEM = 12;
    public static final int CANCEL_DUE_TO_PROBLEM_OF_STORE = 13;
    
    private static HashMap<Integer, String> statusInitalization() {
        HashMap<Integer, String> result = new HashMap<>();
        
        result.put(PENDING, "Pending Confirmation");
        result.put(PROCESSING, "Processing");
        result.put(SHIPPED, "Shipped");
        result.put(DELIVERIED, "Deliveried");
        result.put(CANCEL_BY_USER, "Cancelled by user");
        result.put(CANCEL_DUE_TO_INVALID_USER_INFORMATION, "Cancelled due to invalid user information");
        result.put(CANCEL_DUE_TO_DELIVERY_PROBLLEM, "Cancelled due to delivery problem");
        result.put(CANCEL_DUE_TO_PROBLEM_OF_STORE, "Cancelled due to problem of store");
        
        return result;
    }
    
    public static String getStatus(int statusNumber) {
        return status.get(statusNumber) != null
                ? 
                (String) status.get(statusNumber)
                :
                "Unknown status";
    }
    
    public static List<Integer> ableToCancelStatus() {
        List<Integer> result = new ArrayList<>();
        
        for (Integer statusNumber : status.keySet()) {
            if (statusNumber < 10 && statusNumber != DELIVERIED) result.add(statusNumber);
        }
        
        return result;
    }
    
    public static List<Integer> canceledStatus() {
        List<Integer> result = new ArrayList<>();
        
        for (Integer statusNumber : status.keySet()) {
            if (statusNumber >= 10 && statusNumber < 100) result.add(statusNumber);
        }
        
        return result;
    }

    public static HashMap<Integer, String> getStatus() {
        return status;
    }
}
