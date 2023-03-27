package com.iscreammm.restapi.exception;

import com.iscreammm.restapi.utils.Message;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

@RestControllerAdvice
public class DefaultAdvice {

    @ExceptionHandler(JSONException.class)
    @ResponseBody
    public String handleException() {
        Message<Integer> message = new Message<>(false, "Can't parse input json", -1);

        return message.toString();
    }

    @ExceptionHandler(IOException.class)
    @ResponseBody
    public String handleException(IOException e) {
        Message<Integer> message = new Message<>(false, e.getMessage(), -1);

        return message.toString();
    }
}
