package com.nosify.providers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UrlProvider {
    public static final String API_PREFIX = "/api";
    
    private String addApiPrefix(final String PATH) {
        return API_PREFIX + PATH;
    }
    
    public class User implements AuthUrl {
        public static final String PREFIX = "/user";
        
        // NONE SIGN IN
        public static final String SIGN_UP = "/sign-up";
        public static final String SIGN_IN = "/sign-in";
        public static final String FORGOT_PASSWORD = "/forgot-password";
        
        // NEED TO SIGN IN
        public static final String GET_USER_INFO = "/info";
        public static final String CHANGE_USER_INFO = "/change-info";
        public static final String SIGN_OUT = "/sign-out";
        
        @Override
        public List<String> signInUrls() {
            ArrayList<String> signInUrls = new ArrayList<>();
            signInUrls.add(addApiPrefix(addUserPrefix(GET_USER_INFO)));
            signInUrls.add(addApiPrefix(addUserPrefix(CHANGE_USER_INFO)));
            signInUrls.add(addApiPrefix(addUserPrefix(SIGN_OUT)));
            
            return signInUrls;
        }
        
        private String addUserPrefix(final String PATH) {
            return PREFIX + PATH;
        }

        @Override
        public HashMap<String, List<String>> roleUrls() {
            return null;
        }
    
        
    }
    
    public class Product implements AuthUrl {
        public static final String PREFIX = "/product";
        
        // NONE SIGN IN
        public static final String BY_NAME = "/by-name";
        public static final String DETAIL = "/detail/{id}";
        public static final String BY_LIST_ID = "/by-list-id";
        public static final String RELATED_PRODUCTS = "/related-products/{id}";

        
        // NEED TO SIGN IN
        
        @Override
        public List<String> signInUrls() {
            ArrayList<String> signInUrls = new ArrayList<>();
            
            return signInUrls;
        }
        
        private String addProductPrefix(final String PATH) {
            return PREFIX + PATH;
        }

        @Override
        public HashMap<String, List<String>> roleUrls() {
            return null;
        }
    }
    
    public class Category implements AuthUrl {
        public static final String PREFIX = "/category";
        
        // NONE SIGN IN
        public static final String FOR_HOME_PAGE = "/for-home-page";
        public static final String WITHOUT_THUMBNAIL = "/without-thumbnail";
        public static final String GET_ALL = "/all";
        public static final String BY_ID = "/by-id/{id}";
        
        // NEED TO SIGN IN
        
        @Override
        public List<String> signInUrls() {
            ArrayList<String> signInUrls = new ArrayList<>();
            
            return signInUrls;
        }
        
        private String addCategoryPrefix(final String PATH) {
            return PREFIX + PATH;
        }

        @Override
        public HashMap<String, List<String>> roleUrls() {
            return null;
        }
    }
    
    public class Order implements AuthUrl {
        public static final String PREFIX = "/order";
        
        // NONE SIGN IN
        
        // NEED TO SIGN IN
        public static final String CHECKOUT = "/checkout";
        public static final String ALL_ORDERS = "/all";
        public static final String USER_CANCEL = "/cancel/{id}";
        
        @Override
        public List<String> signInUrls() {
            ArrayList<String> signInUrls = new ArrayList<>();
            
            signInUrls.add(addApiPrefix(addOrderPrefix(CHECKOUT)));
            signInUrls.add(addApiPrefix(addOrderPrefix(ALL_ORDERS)));
            signInUrls.add(addApiPrefix(addOrderPrefix(USER_CANCEL)));
            
            return signInUrls;
        }
        
        private String addOrderPrefix(final String PATH) {
            return PREFIX + PATH;
        }

        @Override
        public HashMap<String, List<String>> roleUrls() {
            return null;
        }
    }
    
    public List<AuthUrl> getAllAuthUrl() {
        ArrayList<AuthUrl> result = new ArrayList<>();
        result.add(new User());
        result.add(new Product());
        result.add(new Category());
        result.add(new Order());
        return result;
    }
}


