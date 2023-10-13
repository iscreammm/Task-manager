package com.iscreammm.restapi.service;

import com.iscreammm.restapi.model.Backup;
import com.iscreammm.restapi.model.Gender;
import com.iscreammm.restapi.model.Profile;
import com.iscreammm.restapi.model.User;
import com.iscreammm.restapi.repository.ProfileRepository;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

@Service
@ComponentScan(basePackages = {
        "com.iscreammm.restapi.security.config"
})
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final GenderService genderService;
    private final UserService userService;
    private final BackupService backupService;
    private final PasswordEncoder passwordEncoder;
    private final MailSenderService mailSenderService;
    @Autowired
    public ProfileService(ProfileRepository profileRepository, GenderService genderService,
                          UserService userService, BackupService backupService,
                          PasswordEncoder passwordEncoder, MailSenderService mailSenderService) {
        this.profileRepository = profileRepository;
        this.genderService = genderService;
        this.userService = userService;
        this.backupService = backupService;
        this.passwordEncoder = passwordEncoder;
        this.mailSenderService = mailSenderService;
    }
    @Transactional
    public void addProfile(String data) throws IOException, JSONException, MessagingException {
        JSONObject jsonObject = new JSONObject(data);

        String username = jsonObject.getString("username");
        String password = jsonObject.getString("password");
        String name = jsonObject.getString("name");
        String genderName = jsonObject.getString("gender");
        Date birthday = new Date(jsonObject.getLong("birthday"));
        String mail = jsonObject.getString("mail");

        if (name.length() > 50) {
            throw new IOException("Name length must be less than 50");
        } else if (username.length() > 30) {
            throw new IOException("Username length must be less than 30");
        } else if (!userService.isUsernameFree(username)) {
            throw new IOException("Username is busy");
        } else if (password.length() > 16) {
            throw new IOException("Password length must be less than 16");
        } else if (birthday.after(new Date())) {
            throw new IOException("Birthday cannot be in the future time");
        } else if (mail.length() > 40) {
            throw new IOException("Mail length must be less than 40");
        } else if (!mail.contains("@") || (mail.chars().filter(ch -> ch == '@').count() > 1)) {
            throw new IOException("Mail must have one '@'");
        } else if (!userService.isMailFree(mail)) {
            throw new IOException("Mail is busy");
        }

        password = passwordEncoder.encode(password);

        Gender gender = genderService.getGender(genderName);

        String code = DigestUtils.md5Hex(username);

        Profile profile = new Profile(name, birthday);
        User user = new User(username, password, mail, code);

        Backup backup = new Backup();
        user.setBackup(backup);

        profile.setUser(user);
        gender.addProfile(profile);

        profile.setGender(gender);
        profile.setPhoto(Files.readAllBytes(Paths.get("resources/defaultPhoto/avatar.jpg")));

        backupService.update(backup);
        userService.update(user);
        genderService.update(gender);
        profileRepository.save(profile);

        mailSenderService.send(mail, code);
    }
    @Transactional
    public void update(Profile profile) {
        profileRepository.save(profile);
    }
    @Transactional
    public void deleteProfile(Long profileId) {
        profileRepository.deleteById(profileId);
    }
}
