package com.savemyroaming;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SavemyroamingApplication {

	public static void main(String[] args) {
		SpringApplication.run(SavemyroamingApplication.class, args);
	}

//	@Bean
//	public WebMvcConfigurer corsConfigurer() {
//		return new WebMvcConfigurer() {
//			@Override
//			public void addCorsMappings(CorsRegistry registry) {
//				registry.addMapping("/**").allowedOrigins("*");
//			}
//		};
//	}

//		@Bean
//	public CaptchaService getCapatchaImplementation (){
//		return new MockCaptchaService();
//	}

}
