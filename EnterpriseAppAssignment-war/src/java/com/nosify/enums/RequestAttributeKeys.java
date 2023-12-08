package com.nosify.enums;

public enum RequestAttributeKeys {
    USER_ID("UserID");
    
    private final String name;
    
    RequestAttributeKeys(String name) {
        this.name = name;
    }
    
    public String getRole() {
        return this.name;
    }

    @Override
    public String toString() {
        return name;
    }
}
