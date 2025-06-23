package com.example.sp20250610;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.sp20250610.mapper")
public class Sp20250610Application {

	public static void main(String[] args) {
		SpringApplication.run(Sp20250610Application.class, args);
	}

}
