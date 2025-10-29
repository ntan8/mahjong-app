package com.mahjong;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MahjongApplication {

	public static void main(String[] args) {
		System.out.println("Working Directory: " + System.getProperty("user.dir"));

		SpringApplication.run(MahjongApplication.class, args);

	}

}
