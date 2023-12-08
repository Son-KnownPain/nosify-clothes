package com.nosify.enums;

public enum AppConstants {
    DEFAULT_AVATAR("default-avatar.jpg");
    
    private final String appConst;
    
    AppConstants(String appConst) {
        this.appConst = appConst;
    }
    
    public String getRole() {
        return this.appConst;
    }

    @Override
    public String toString() {
        return appConst;
    }
}
