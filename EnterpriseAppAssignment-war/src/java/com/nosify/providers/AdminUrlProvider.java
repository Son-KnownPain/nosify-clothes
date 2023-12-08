package com.nosify.providers;

import java.util.ArrayList;
import java.util.List;

public class AdminUrlProvider {
    public static final String ADMIN_PREFIX = "/admin";
    
    public class Admin {
        public static final String LOGIN = "/login";
        public static final String LOGIN_CHECK = "/login-check";
        public static final String LOGOUT = "/logout";
    }
    
    public class Category {
        public static final String PREFIX = "/category";
        
        public static final String INDEX1 = "/index";
        public static final String INDEX2 = "/";
        public static final String INDEX3 = "";
        
        public static final String ADD = "/add";
        public static final String STORE = "/store";
        public static final String EDIT = "/edit/{id}";
        public static final String UPDATE = "/update";
        public static final String DELETE = "/delete/{id}";
        public static final String DELETE_CONFIRMATION = "/delete";
    }
    
    public class Product {
        public static final String PREFIX = "/product";
        
        public static final String INDEX1 = "/index";
        public static final String INDEX2 = "/";
        public static final String INDEX3 = "";
        
        public static final String ADD = "/add";
        public static final String STORE = "/store";
        public static final String EDIT = "/edit/{id}";
        public static final String UPDATE = "/update";
        public static final String DELETE = "/delete/{id}";
        public static final String DELETE_CONFIRMATION = "/delete";
    }
    
    public class User {
        public static final String PREFIX = "/user";
        
        public static final String INDEX1 = "/index";
        public static final String INDEX2 = "/";
        public static final String INDEX3 = "";
        
        public static final String ADD = "/add";
        public static final String STORE = "/store";
        public static final String EDIT = "/edit/{id}";
        public static final String UPDATE = "/update";
        public static final String DELETE = "/delete/{id}";
        public static final String DELETE_CONFIRMATION = "/delete";
    }
    
    public class Order {
        public static final String PREFIX = "/order";
        
        public static final String INDEX1 = "/index";
        public static final String INDEX2 = "/";
        public static final String INDEX3 = "";
        
        public static final String DETAIL = "/detail/{id}";
        public static final String ADD = "/add";
        public static final String STORE = "/store";
        public static final String EDIT = "/edit/{id}";
        public static final String UPDATE = "/update";
        public static final String DELETE = "/delete/{id}";
        public static final String DELETE_CONFIRMATION = "/delete";
        public static final String UPDATE_STATUS = "/update-status";
    }
    
    public static List<String> getIgnoreAdminUrls() {
        ArrayList<String> result = new ArrayList<>();
        result.add(adminPrefix(Admin.LOGIN));
        result.add(adminPrefix(Admin.LOGIN_CHECK));
        return result;
    }
    
    public static String getLoginPageUrl() {
        return ADMIN_PREFIX + Admin.LOGIN;
    }
    
    private static String adminPrefix(String s) {
        return ADMIN_PREFIX + s;
    }
}
