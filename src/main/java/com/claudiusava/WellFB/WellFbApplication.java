package com.claudiusava.WellFB;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WellFbApplication {

	public static String UPLOAD_DIRECTORY;
	public static String UPLOAD_BASE;
	public static String DRAWABLE_RESOURCES;
	public static String CSS_RESOURCES;

	public static void main(String[] args) {
		UPLOAD_BASE = "/uploads/";
		UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/src/main/resources" + UPLOAD_BASE;
		DRAWABLE_RESOURCES = System.getProperty("user.dir") + "/src/main/resources/static/drawable/";
		CSS_RESOURCES = System.getProperty("user.dir") + "/src/main/resources/static/css/";

		SpringApplication.run(WellFbApplication.class, args);
	}


}
