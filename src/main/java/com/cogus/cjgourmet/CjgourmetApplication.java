package com.cogus.cjgourmet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableAutoConfiguration, @ComponentScan, @Configuration을 묶어 놓은 것
public class CjgourmetApplication {

	public static void main(String[] args) {
		SpringApplication.run(CjgourmetApplication.class, args);
	}

}
