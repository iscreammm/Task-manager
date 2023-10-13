package com.iscreammm.restapi.controller;

import com.iscreammm.restapi.model.Backup;
import com.iscreammm.restapi.security.jwt.JwtUtils;
import com.iscreammm.restapi.service.BackupService;
import com.iscreammm.restapi.utils.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class BackupController {
    private final BackupService backupService;
    private final JwtUtils jwtUtils;

    @Autowired
    public BackupController(BackupService backupService, JwtUtils jwtUtils) {
        this.backupService = backupService;
        this.jwtUtils = jwtUtils;
    }

    @GetMapping(path = "/sync")
    public String getBackup(@RequestHeader(value = "Authorization") String header) {
        String token = header.substring(7, header.length());
        String username = jwtUtils.getUsernameFromJwtToken(token);

        Backup backup = backupService.getBackup(username);

        return (new Message<>(true, "", backup.toString())).toString();
    }

    @PostMapping(path = "/sync", consumes="application/json")
    public String setBackup(@RequestHeader(value = "Authorization") String header, @RequestBody String data) {
        String token = header.substring(7, header.length());
        String username = jwtUtils.getUsernameFromJwtToken(token);

        backupService.setBackup(username, data);

        return (new Message<>(true, "", 1)).toString();
    }
}
