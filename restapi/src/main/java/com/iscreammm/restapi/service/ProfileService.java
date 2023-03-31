package com.iscreammm.restapi.service;

import com.iscreammm.restapi.model.Gender;
import com.iscreammm.restapi.model.Profile;
import com.iscreammm.restapi.model.User;
import com.iscreammm.restapi.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final GenderService genderService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ProfileService(ProfileRepository profileRepository, GenderService genderService,
                          UserService userService, PasswordEncoder passwordEncoder) {
        this.profileRepository = profileRepository;
        this.genderService = genderService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    public void addProfile(String data) throws IOException, JSONException {
        JSONObject jsonObject = new JSONObject(data);

        String username = jsonObject.getString("username");
        String password = jsonObject.getString("password");
        String name = jsonObject.getString("name");
        String genderName = jsonObject.getString("gender");

        if (name.length() > 20) {
            throw new IOException("Name length must be less than 20!");
        } else if (username.length() > 16) {
            throw new IOException("Username length must be less than 16!");
        } else if (!userService.isUsernameFree(username)) {
            throw new IOException("Username is busy!");
        } else if (password.length() > 16) {
            throw new IOException("Password length must be less than 16!");
        }

        password = passwordEncoder.encode(password);

        Gender gender = genderService.getGender(genderName);

        Profile profile = new Profile(name);
        User user = new User(username, password);

        userService.update(user);
        profile.setUser(user);

        gender.addProfile(profile);
        genderService.update(gender);
        profile.setGender(gender);

        profile.setPhoto(Files.readAllBytes(Paths.get("src/main/resources/defaultPhoto/avatar.jpg")));

        profileRepository.save(profile);
    }

    public void update(Profile profile) {
        profileRepository.save(profile);
    }

    public void deleteProfile(Long profileId) {
        profileRepository.deleteById(profileId);
    }
}
