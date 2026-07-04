package com.visa_iss_kin;

import com.visa_iss_kin.model.Section;
import com.visa_iss_kin.repository.SectionRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class VisaIssKinApplication {
    
	public static void main(String[] args) {
		SpringApplication.run(VisaIssKinApplication.class, args);
	}
        @Bean
        CommandLineRunner afficherSection(SectionRepository sr){
            Section sec = new Section("STD", "Sciences et Techniques Documentaires", LocalDate.now(), "admin");
            sr.save(sec);
            return args->sr.findAll().forEach(System.out::println);
        }

}
