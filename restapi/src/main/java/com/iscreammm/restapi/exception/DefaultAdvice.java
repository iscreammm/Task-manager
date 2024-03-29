package com.iscreammm.restapi.exception;

import com.iscreammm.restapi.utils.Message;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.mail.MessagingException;
import org.json.JSONException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import redis.clients.jedis.exceptions.JedisException;

import java.io.IOException;
import java.rmi.AccessException;

@RestControllerAdvice
public class DefaultAdvice {

    @ExceptionHandler(JSONException.class)
    @ResponseBody
    public String handleException(JSONException e) {
        Message<Integer> message = new Message<>(false, "Can't parse input json: " + e.getMessage(), -1);

        return message.toString();
    }

    @ExceptionHandler(SignatureException.class)
    @ResponseBody
    public String handleException(SignatureException e) {
        Message<Integer> message = new Message<>(false, "Invalid JWT signature: " + e.getMessage(), -1);

        return message.toString();
    }

    @ExceptionHandler(MalformedJwtException.class)
    @ResponseBody
    public String handleException(MalformedJwtException e) {
        Message<Integer> message = new Message<>(false, "Invalid JWT token: " + e.getMessage(), -1);

        return message.toString();
    }

    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseBody
    public String handleException(ExpiredJwtException e) {
        Message<Integer> message = new Message<>(false, "JWT token is expired: " + e.getMessage(), -1);

        return message.toString();
    }

    @ExceptionHandler(UnsupportedJwtException.class)
    @ResponseBody
    public String handleException(UnsupportedJwtException e) {
        Message<Integer> message = new Message<>(false, "JWT token is unsupported: " + e.getMessage(), -1);

        return message.toString();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public String handleException(IllegalArgumentException e) {
        Message<Integer> message = new Message<>(false, "JWT claims string is empty: " + e.getMessage(), -1);

        return message.toString();
    }

    @ExceptionHandler(MessagingException.class)
    @ResponseBody
    public String handleException(MessagingException e) {
        Message<Integer> message = new Message<>(false, "Mail sending error: " + e.getMessage(), -1);

        return message.toString();
    }

    @ExceptionHandler(JedisException.class)
    @ResponseBody
    public String handleException(JedisException e) {
        Message<Integer> message = new Message<>(false, "Jedis error occurred: " + e.getMessage(), -1);

        return message.toString();
    }

    @ExceptionHandler(AccessException.class)
    @ResponseBody
    public String handleException(AccessException e) {
        Message<Integer> message = new Message<>(false, "Access error: " + e.getMessage(), -1);

        return message.toString();
    }

    @ExceptionHandler(IOException.class)
    @ResponseBody
    public String handleException(IOException e) {
        Message<Integer> message = new Message<>(false, "Input error: " + e.getMessage(), -1);

        return message.toString();
    }
}
