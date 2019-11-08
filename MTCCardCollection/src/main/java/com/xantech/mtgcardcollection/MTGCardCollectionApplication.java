package com.xantech.mtgcardcollection;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class MTGCardCollectionApplication {
	public static void main(String[] args) {
		SpringApplication.run(MTGCardCollectionApplication.class, args);
	}
}
