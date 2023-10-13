package com.iscreammm.restapi.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties("app")
public class YAMLConfig {

    private String jwtAccessSecret;
    private String jwtRefreshSecret;
    private int jwtExpirationMs;
    private int jwtRefreshExpirationMs;

    public String getJwtAccessSecret() {
        return jwtAccessSecret;
    }

    public void setJwtAccessSecret(String jwtAccessSecret) {
        this.jwtAccessSecret = jwtAccessSecret;
    }

    public String getJwtRefreshSecret() {
        return jwtRefreshSecret;
    }

    public void setJwtRefreshSecret(String jwtRefreshSecret) {
        this.jwtRefreshSecret = jwtRefreshSecret;
    }

    public int getJwtExpirationMs() {
        return jwtExpirationMs;
    }

    public void setJwtExpirationMs(int jwtExpirationMs) {
        this.jwtExpirationMs = jwtExpirationMs;
    }

    public int getJwtRefreshExpirationMs() {
        return jwtRefreshExpirationMs;
    }

    public void setJwtRefreshExpirationMs(int jwtRefreshExpirationMs) {
        this.jwtRefreshExpirationMs = jwtRefreshExpirationMs;
    }
}
