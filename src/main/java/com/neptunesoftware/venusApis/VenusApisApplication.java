package com.neptunesoftware.venusApis;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableEncryptableProperties
@SpringBootApplication
public class VenusApisApplication {

	public static void main(String[] args) {
		SpringApplication.run(VenusApisApplication.class, args);
	}

}
