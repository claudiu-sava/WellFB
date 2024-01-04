package com.claudiusava.WellFB;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;

@SpringBootApplication
public class WellFbApplication {

	public static String UPLOAD_DIRECTORY;
	public static String UPLOAD_BASE;
	public static String DRAWABLE_RESOURCES;
	public static String CSS_RESOURCES;
	public static String JS_RESOURCES;
	public static String AVATAR_DIRECTORY;
	public static String AVATAR_BASE;
	public static String HQ_UPLOAD_BASE;
	public static String HQ_UPLOAD_DIRECTORY;
	public static String TEMPLATE_BASE;
	public static String TEMPLATE_DIRECTORY;
	public static final String PROJECT_LOCATION = System.getProperty("user.dir");

	public static void main(String[] args) {
		TEMPLATE_BASE = "/templates/fragments/";
		TEMPLATE_DIRECTORY = PROJECT_LOCATION + "/src/main/resources" + TEMPLATE_BASE;
		HQ_UPLOAD_BASE = "/hq/";
		HQ_UPLOAD_DIRECTORY = PROJECT_LOCATION + "/src/main/resources" + HQ_UPLOAD_BASE;
		UPLOAD_BASE = "/uploads/";
		UPLOAD_DIRECTORY = PROJECT_LOCATION + "/src/main/resources" + UPLOAD_BASE;
		DRAWABLE_RESOURCES = PROJECT_LOCATION + "/src/main/resources/static/drawable/";
		CSS_RESOURCES = PROJECT_LOCATION + "/src/main/resources/static/css/";
		JS_RESOURCES = PROJECT_LOCATION + "/src/main/resources/static/js/";
		AVATAR_BASE = "/avatars/";
		AVATAR_DIRECTORY = PROJECT_LOCATION + "/src/main/resources" + AVATAR_BASE;

		try{
			Files.createDirectories(Paths.get(HQ_UPLOAD_DIRECTORY));
		} catch (Exception e){
			System.out.println("HQ_UPLOAD_DIRECTORY already exists");
		}

		try {
			Files.createDirectories(Paths.get(UPLOAD_DIRECTORY));
		} catch (Exception e) {
			System.out.println("UPLOAD_DIRECTORY already exists");
		}

		SpringApplication.run(WellFbApplication.class, args);

	}


}
