package com.iscreammm.restapi.service;

import com.iscreammm.restapi.model.Gender;
import com.iscreammm.restapi.repository.GenderRepository;
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

    public Gender getGender(String genderName) throws IOException {
        Gender gender = genderRepository.findByGender(genderName);

        if (gender == null) {
            throw new IOException("Not supported gender");
        } else {
            return gender;
        }
    }

    public void update(Gender gender) {
        genderRepository.save(gender);
    }
}
