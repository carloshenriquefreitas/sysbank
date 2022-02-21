package com.chfreitas.sysbank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = { "com.chfreitas.sysbank.model" })
@EnableJpaRepositories(basePackages = { "com.chfreitas.sysbank.repository" })
@ComponentScan(basePackages = { "com.chfreitas.sysbank.service" })
@ComponentScan(basePackages = { "com.chfreitas.sysbank.controller" })
public class SysbankApplication {

	public static void main(String[] args) {
		SpringApplication.run(SysbankApplication.class, args);
	}

}
