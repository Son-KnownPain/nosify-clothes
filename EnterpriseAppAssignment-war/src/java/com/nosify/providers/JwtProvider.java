package com.nosify.providers;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.util.Date;
import java.util.HashMap;

public class JwtProvider {
    public static final String VERIFY_RESULT_KEY = "result";
    public static final String VERIFY_ERROR_KEY = "error";
    public static final String INVALID_TOKEN = "Invalid token";
    public static final String TOKEN_WAS_EXPIRED = "Token was expired";
    public static final String UNSUPPORT_TOKEN = "Unsupport token";
    public static final String JWT_CLAIMS_STRING_IS_EMPTY = "Jwt claims string is empty";
    
    public static String generateToken(String data, long expireTime, final String SECRET_KEY) {
        Date dateNow = new Date();
        Date expireDate = new Date(dateNow.getTime() + expireTime);
        
        return Jwts.builder()
                .setSubject(data)
                .setIssuedAt(dateNow)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }
    
    public static String getDataFromJwt(String token, final String SECRET_KEY) {
        try {
            Claims claims = Jwts.parser()
                            .setSigningKey(SECRET_KEY)
                            .parseClaimsJws(token)
                            .getBody();
            return claims.getSubject();
        } catch (ExpiredJwtException | MalformedJwtException | SignatureException | UnsupportedJwtException | IllegalArgumentException e) {
            return null;
        }
    }
    
    public static HashMap<String, Object> verifyToken(String token, final String SECRET_KEY) {
        HashMap<String, Object> result = new HashMap<>();
        try {
            Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token);
            result.put(VERIFY_RESULT_KEY, true);
            return result;
        } catch (MalformedJwtException e) {
            result.put(VERIFY_ERROR_KEY, INVALID_TOKEN);
        } catch (ExpiredJwtException e) {
            result.put(VERIFY_ERROR_KEY, TOKEN_WAS_EXPIRED);
        } catch (UnsupportedJwtException e) {
            result.put(VERIFY_ERROR_KEY, UNSUPPORT_TOKEN);
        } catch (IllegalArgumentException e) {
            result.put(VERIFY_ERROR_KEY, JWT_CLAIMS_STRING_IS_EMPTY);
        }
        
        result.put(VERIFY_RESULT_KEY, false);
        
        return result;
    }
}
