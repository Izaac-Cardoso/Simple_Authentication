package com.cardoso_izaac.SimpleAuthentication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

//@EnableConfigurationProperties(RsaKeyConficProperty.class)
@SpringBootApplication
public class SimpleAuthenticationApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimpleAuthenticationApplication.class, args);
	}

}
