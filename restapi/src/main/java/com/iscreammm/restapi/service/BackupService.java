package com.iscreammm.restapi.service;

import com.iscreammm.restapi.model.Backup;
import com.iscreammm.restapi.model.User;
import com.iscreammm.restapi.repository.BackupRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class BackupService {
    private final BackupRepository backupRepository;
    private final UserService userService;

    @Autowired BackupService(BackupRepository backupRepository, UserService userService) {
        this.backupRepository = backupRepository;
        this.userService = userService;
    }

    @Transactional
    public Backup getBackup(String username) {
        User user = userService.getUser(username);

        return user.getBackup();
    }

    @Transactional
    public void setBackup(String username, String data) {
        User user = userService.getUser(username);

        Backup backup = user.getBackup();

        backup.setBackup(data);
        backup.setDate(new Date());

        backupRepository.save(backup);
        userService.update(user);
    }

    @Transactional
    public void update(Backup backup) {
        backupRepository.save(backup);
    }
}
