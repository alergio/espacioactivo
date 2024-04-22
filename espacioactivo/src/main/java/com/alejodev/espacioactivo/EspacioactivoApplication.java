package com.alejodev.espacioactivo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
public class EspacioactivoApplication {

	public static void main(String[] args) {
		SpringApplication.run(EspacioactivoApplication.class, args);
	}

}
