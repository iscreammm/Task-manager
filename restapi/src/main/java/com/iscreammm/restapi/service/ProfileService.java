package com.iscreammm.restapi.service;

import com.iscreammm.restapi.model.Gender;
import com.iscreammm.restapi.model.Profile;
import com.iscreammm.restapi.model.User;
import com.iscreammm.restapi.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;
    public final GenderService genderService;
    public final UserService userService;

    @Autowired
    public ProfileService(ProfileRepository profileRepository, GenderService genderService, UserService userService) {
        this.profileRepository = profileRepository;
        this.genderService = genderService;
        this.userService = userService;
    }

    public void addProfile(String data) throws IOException, JSONException {
        JSONObject jsonObject = new JSONObject(data);

        String login = jsonObject.getString("login");
        String password = jsonObject.getString("password");
        String name = jsonObject.getString("name");
        String genderName = jsonObject.getString("gender");

        if (name.length() > 20) {
            throw new IOException("Name length must be less than 20!");
        } else if (login.length() > 16) {
            throw new IOException("Login length must be less than 16!");
        } else if (!userService.isLoginFree(login)) {
            throw new IOException("Login is busy!");
        } else if (password.length() > 16) {
            throw new IOException("Password length must be less than 16!");
        }

        Gender gender = genderService.getGender(genderName);

        Profile profile = new Profile(name);
        User user = new User(login, password);

        userService.update(user);
        profile.setUser(user);

        gender.addProfile(profile);
        genderService.update(gender);
        profile.setGender(gender);

        profileRepository.save(profile);
    }

    public void update(Profile profile) {
        profileRepository.save(profile);
    }

    public void deleteProfile(Long profileId) {
        profileRepository.deleteById(profileId);
    }
}
