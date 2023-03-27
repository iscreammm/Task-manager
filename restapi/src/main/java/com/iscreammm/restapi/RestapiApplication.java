package com.iscreammm.restapi;

import com.iscreammm.restapi.model.Gender;
import com.iscreammm.restapi.repository.GenderRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RestapiApplication {

	private final GenderRepository genderRepository;

	@Autowired
	public RestapiApplication(GenderRepository genderRepository) {
		this.genderRepository = genderRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(RestapiApplication.class, args);
	}

	@Bean
	InitializingBean sendDatabase() {
		return () -> {
			if (genderRepository.findByGender("Male") == null
					&& genderRepository.findByGender("Female") == null)
			{
				genderRepository.save(new Gender("Male"));
				genderRepository.save(new Gender("Female"));
			}
		};
	}
}
