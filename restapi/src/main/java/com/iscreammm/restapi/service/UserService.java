package com.iscreammm.restapi.service;

import com.iscreammm.restapi.model.User;
import com.iscreammm.restapi.repository.UserRepository;
import com.iscreammm.restapi.security.config.JedisConfig;
import com.iscreammm.restapi.security.jwt.JwtUtils;
import com.iscreammm.restapi.utils.JwtResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.ScanParams;
import redis.clients.jedis.resps.ScanResult;

import java.io.IOException;
import java.rmi.AccessException;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final JedisConfig jedisConfig;
    private final JedisPool jedisPool;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       JwtUtils jwtUtils, JedisConfig jedisConfig) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.jedisConfig = jedisConfig;
        this.jedisPool = new JedisPool(jedisConfig.getHost(), jedisConfig.getPort());
    }

    public String getProfile(String data) throws IOException, JSONException {
        JSONObject jsonObject = new JSONObject(data);

        String username = jsonObject.getString("username");
        String password = jsonObject.getString("password");

        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new IOException("Username isn't correct");
        } else if (!user.isActive()) {
            throw new AccessException("Mail isn't confirmed");
        } else if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IOException("Password isn't correct");
        }

        String accessToken = jwtUtils.generateJwtAccessToken(user);
        String refreshToken = jwtUtils.generateJwtRefreshToken(user);

        updatePair(refreshToken, username);

        return new JwtResponse(accessToken, refreshToken).toString();
    }

    public String refreshToken(String token) throws IOException {
        String username = null;

        jwtUtils.validateJwtRefreshToken(token);

        try(Jedis jedis = jedisPool.getResource()) {
            username = jedis.get(token);
        }

        if (username == null) {
            throw new IOException("Invalid token!");
        }

        User user = userRepository.findByUsername(username);

        String accessToken = jwtUtils.generateJwtAccessToken(user);
        String refreshToken = jwtUtils.generateJwtRefreshToken(user);

        updatePair(refreshToken, username);

        return new JwtResponse(accessToken, refreshToken).toString();
    }

    public void activateUser(String code) throws IOException {
        User user = userRepository.findByCode(code);

        if (code == null) {
            throw new IOException("Invalid code");
        } else {
            user.setActive(true);
            user.setCode(null);

            userRepository.save(user);
        }
    }

    public void updatePair(String refreshToken, String username) {
        String cursor = "0";
        ScanParams scanParams = new ScanParams().count(1);

        try(Jedis jedis = jedisPool.getResource()) {
            do {
                ScanResult<String> scanResult = jedis.scan(cursor, scanParams);

                if ((scanResult.getResult().size() != 0) && jedis.get(scanResult.getResult().get(0)).equals(username)) {
                    jedis.del(scanResult.getResult().get(0));
                }

                cursor = scanResult.getCursor();
            } while (!"0".equals(cursor));

            jedis.set(refreshToken, username);
        }
    }

    public boolean isUsernameFree(String username) {
        return (userRepository.findByUsername(username) == null);
    }

    public void update(User user) {
        userRepository.save(user);
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    public User getUser(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("Username isn't correct");
        } else {
            return user;
        }
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = getUser(login);

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                user.getAuthorities());
    }
}
