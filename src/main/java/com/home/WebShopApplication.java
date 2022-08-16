package com.home;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.home.config.StorageProperties;
import com.home.service.StorageService;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class WebShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebShopApplication.class, args);
	}

	@Bean
	CommandLineRunner init(StorageService storageService) {
	    return (args -> {
		storageService.init();
	    });
	}

}
