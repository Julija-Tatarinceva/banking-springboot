package com.BankingApp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication()
public class BankingApplication {

	private static Logger logger = LogManager.getLogger(BankingApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(BankingApplication.class, args);
		logger.info("Application started");
	}


}
