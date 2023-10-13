package com.iscreammm.restapi.service;

import com.iscreammm.restapi.model.User;
import com.iscreammm.restapi.repository.UserRepository;
import com.iscreammm.restapi.security.config.JedisConfig;
import com.iscreammm.restapi.security.jwt.JwtUtils;
import com.iscreammm.restapi.utils.JwtResponse;
import jakarta.transaction.Transactional;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.params.ScanParams;
import redis.clients.jedis.resps.ScanResult;

import java.io.IOException;
import java.rmi.AccessException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final JedisConfig jedisConfig;
    private final JedisPool jedisPool;

    public UserService() {
        this.userRepository = null;
        this.passwordEncoder = null;
        this.jwtUtils = null;
        this.jedisConfig = null;
        this.jedisPool = null;
    }
    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       JwtUtils jwtUtils, JedisConfig jedisConfig) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.jedisConfig = jedisConfig;
        this.jedisPool = new JedisPool(new JedisPoolConfig(), jedisConfig.getHost(), jedisConfig.getPort(),
                10000, jedisConfig.getPassword());
    }
    @Transactional
    public List<String> checkUsername(String username) throws IOException {
        if (username == null || username.length() < 6 || username.length() > 30) {
            throw new IOException("Username isn't correct");
        }

        List<User> usersWithUsername = userRepository.findByUsernameContains(username);

        if (usersWithUsername.size() == 0) {
            return new ArrayList<>();
        } else {
            List<String> freeUsernames = new ArrayList<>();

            int number = 1;
            AtomicBoolean isFree = new AtomicBoolean(true);
            StringBuilder usernameBuilder = new StringBuilder(username);

            while(freeUsernames.size() < 3) {
                while ((number <= 1000) && (freeUsernames.size() < 3)) {
                    String finalUsername = usernameBuilder.toString();
                    int finalNumber = number;

                    usersWithUsername.forEach(user -> {
                        if (user.getUsername().equals(finalUsername + finalNumber)) {
                            isFree.set(false);
                        }
                    });

                    if (isFree.get()) {
                        freeUsernames.add(usernameBuilder.toString() + number);
                    }

                    isFree.set(true);
                    number++;
                }

                usernameBuilder.append(".");
                number = 1;
            }

            return freeUsernames;
        }
    }
    @Transactional
    public String getProfile(String username, String password) throws IOException, JSONException {
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
    @Transactional
    public String refreshToken(String token) throws IOException {
        String username;

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
    @Transactional
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
    @Transactional
    public User getUser(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("Username isn't correct");
        } else {
            return user;
        }
    }
    @Transactional
    public boolean isUsernameFree(String username) {
        return (userRepository.findByUsername(username) == null);
    }
    @Transactional
    public boolean isMailFree(String mail) {
        return (userRepository.findByMail(mail) == null);
    }
    @Transactional
    public void update(User user) {
        userRepository.save(user);
    }
    @Transactional
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = getUser(username);

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                user.getAuthorities());
    }
}
