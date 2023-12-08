package com.nosify.enums;

public enum TokenNames {
    ACCESS_TOKEN("accessToken"),
    REFRESH_TOKEN("refreshToken");
    
    private final String name;
    
    TokenNames(String name) {
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
