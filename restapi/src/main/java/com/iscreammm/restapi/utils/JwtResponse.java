package com.iscreammm.restapi.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JwtResponse {
    private final String token;
    private final String type = "Bearer";
    private final String refreshToken;

    public JwtResponse(String token, String refreshToken) {
        this.token = token;
        this.refreshToken = refreshToken;
    }

    @Override
    public String toString() {

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        return gson.toJson(this);
    }
}
