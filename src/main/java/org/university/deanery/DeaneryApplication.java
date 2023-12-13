package org.university.deanery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

@SpringBootApplication
public class DeaneryApplication {

	public static void main(String[] args) {
//		String timeStamp = new SimpleDateFormat("dd.MM.yyyy.HH.mm").format(new java.util.Date());
		SpringApplication.run(DeaneryApplication.class, args);
	}

}
