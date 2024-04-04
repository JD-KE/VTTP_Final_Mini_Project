package com.jd.eventhall.MainAppBackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.jd.eventhall.MainAppBackend.config.EnableCors;

@SpringBootApplication
public class MainAppBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(MainAppBackendApplication.class, args);
	}

	@Bean
	@Order(value = -102)
	public WebMvcConfigurer corsConfigurer() {
		// order at highest, or at -100 to get priority before security
		// since we are here, remember to set secret key
		String[] origins = new String[] {"http://localhost:4200"};
		return new EnableCors("/**", origins);
	}
}
