package com.iscreammm.restapi.controller;

import com.iscreammm.restapi.service.GenderService;
import com.iscreammm.restapi.service.ProfileService;
import com.iscreammm.restapi.service.UserService;
import com.iscreammm.restapi.utils.Message;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {

    private final GenderService genderService;
    private final ProfileService profileService;
    private final UserService userService;

    @Autowired
    public AuthenticationController(GenderService genderService,
                                    ProfileService profileService, UserService userService) {
        this.genderService = genderService;
        this.profileService = profileService;
        this.userService = userService;
    }

    @PostMapping(path = "/auth", consumes="application/json")
    public String registerUser(@RequestBody String data) throws JSONException, IOException, MessagingException {
        profileService.addProfile(data);

        return (new Message<>(true, "", 1)).toString();
    }

    @GetMapping(path = "/auth", consumes="application/json")
    public String loginUser(@RequestBody String data) throws JSONException, IOException {
        String profileData = userService.getProfile(data);

        return (new Message<>(true, "", profileData)).toString();
    }

    @GetMapping(path = "/activate/{code}")
    public String activateUser(@PathVariable String code) throws IOException {
        userService.activateUser(code);

        return (new Message<>(true, "", 1)).toString();
    }

    @GetMapping(path = "/refresh/{token}")
    public String refreshToken(@PathVariable String token) throws IOException {
        String data = userService.refreshToken(token);

        return (new Message<>(true, "", data)).toString();
    }
}
