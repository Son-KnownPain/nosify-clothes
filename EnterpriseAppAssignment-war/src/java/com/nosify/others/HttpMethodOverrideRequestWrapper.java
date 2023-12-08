package com.nosify.others;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class HttpMethodOverrideRequestWrapper extends HttpServletRequestWrapper {
    private final String method;
    
    public HttpMethodOverrideRequestWrapper(HttpServletRequest request, String method) {
        super(request);
        this.method = method;
    }
    
    @Override
    public String getMethod() {
        return method;
    }
}
