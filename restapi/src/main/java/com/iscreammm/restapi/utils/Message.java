package com.iscreammm.restapi.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Message<T> {
    private final Boolean state;
    private final String message;
    private final T data;

    public Message(Boolean state, String message, T data) {
        this.state = state;
        this.message = message;
        this.data = data;
    }

    public Boolean getState() {
        return state;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    @Override
    public String toString() {

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        return gson.toJson(this);
    }
}
