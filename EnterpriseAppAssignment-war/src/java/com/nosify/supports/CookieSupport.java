package com.nosify.supports;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class CookieSupport {
    public static Cookie getSpecificCookie(HttpServletRequest request, String specificName) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(specificName)) return cookie;
        }
        return null;
    }
}
