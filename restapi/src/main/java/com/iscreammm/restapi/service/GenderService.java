package com.iscreammm.restapi.service;

import com.iscreammm.restapi.model.Gender;
import com.iscreammm.restapi.repository.GenderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class GenderService {
    private final GenderRepository genderRepository;
    @Autowired
    public GenderService(GenderRepository genderRepository) {
        this.genderRepository = genderRepository;
    }
    @Transactional
    public Gender getGender(String genderName) throws IOException {
        Gender gender = genderRepository.findByGender(genderName);

        if (gender == null) {
            throw new IOException("Gender must be 'Мужской' or 'Женский'");
        } else {
            return gender;
        }
    }
    @Transactional
    public void update(Gender gender) {
        genderRepository.save(gender);
    }
}
