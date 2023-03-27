package com.iscreammm.restapi.service;

import com.iscreammm.restapi.model.User;
import com.iscreammm.restapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String getProfile(String data) throws IOException, JSONException {
        JSONObject jsonObject = new JSONObject(data);

        String login = jsonObject.getString("login");
        String password = jsonObject.getString("password");

        User user = userRepository.findByLogin(login);

        if (user == null) {
            throw new IOException("Login isn't correct");
        } else if (!Objects.equals(user.getPassword(), password)) {
            throw new IOException("Password isn't correct");
        }

        return user.getProfile().toString();
    }

    public boolean isLoginFree(String login) {
        return (userRepository.findByLogin(login) == null);
    }

    public void update(User user) {
        userRepository.save(user);
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
