package com.nosify.services;

import com.nosify.providers.JwtProvider;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class UserTokenService {
    private final String ACCESS_TOKEN_SECRET_KEY = "nvadthnthnhs-access";
    private final String REFRESH_TOKEN_SECRET_KEY = "rf-689326";
    
    private final long ACCESS_TOKEN_EXPIRE_TIME = 30000L; // 30s (30k mili seconds)
    private final long REFRESH_TOKEN_EXPIRE_TIME = 604800000L; // 7 days (604m 800k mili seconds)
    
    public String createAccessToken(String data) {
        return JwtProvider.generateToken(data, ACCESS_TOKEN_EXPIRE_TIME, ACCESS_TOKEN_SECRET_KEY);
    }
    
    public String createRefreshToken(String data) {
        return JwtProvider.generateToken(data, REFRESH_TOKEN_EXPIRE_TIME, REFRESH_TOKEN_SECRET_KEY);
    }
    
    public Map<String, Object> verifyAccessToken(String token) {
        return JwtProvider.verifyToken(token, ACCESS_TOKEN_SECRET_KEY);
    }
    
    public Map<String, Object> verifyRefreshToken(String token) {
        return JwtProvider.verifyToken(token, REFRESH_TOKEN_SECRET_KEY);
    }
    
    public String getDataOfAT(String token) {
        return JwtProvider.getDataFromJwt(token, ACCESS_TOKEN_SECRET_KEY);
    }
    
    public String getDataOfRT(String token) {
        return JwtProvider.getDataFromJwt(token, REFRESH_TOKEN_SECRET_KEY);
    }
}
