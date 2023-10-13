package com.iscreammm.restapi.security.jwt;
import java.util.Date;

import com.iscreammm.restapi.model.User;
import com.iscreammm.restapi.security.config.YAMLConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;

@Component
public class JwtUtils {

    private final YAMLConfig jwtConfig;

    @Autowired
    public JwtUtils(YAMLConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    public String generateJwtAccessToken(User user) {
        return generateTokenFromUsername(user.getUsername(),
                jwtConfig.getJwtAccessSecret(), jwtConfig.getJwtExpirationMs());
    }

    public String generateJwtRefreshToken(User user) {
        return generateTokenFromUsername(user.getUsername(),
                jwtConfig.getJwtRefreshSecret(), jwtConfig.getJwtRefreshExpirationMs());
    }

    public String generateTokenFromUsername(String username, String jwtSecret, int jwtExpirationMs) {
        return Jwts.builder().setSubject(username).setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

    public String getUsernameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtConfig.getJwtAccessSecret()).parseClaimsJws(token).getBody().getSubject();
    }

    public void validateJwtAccessToken(String authToken) {
        Jwts.parser().setSigningKey(jwtConfig.getJwtAccessSecret()).parseClaimsJws(authToken);
    }

    public void validateJwtRefreshToken(String authToken) {
        Jwts.parser().setSigningKey(jwtConfig.getJwtRefreshSecret()).parseClaimsJws(authToken);
    }
}
