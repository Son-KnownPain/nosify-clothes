package com.nosify.enums;

public enum UserRoles {
    USER("User"),
    ADMIN("Admin");
    
    private final String role;
    
    UserRoles(String role) {
        this.role = role;
    }
    
    public String getRole() {
        return this.role;
    }

    @Override
    public String toString() {
        return role;
    }
}
