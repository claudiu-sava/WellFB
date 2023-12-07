package com.claudiusava.WellFB;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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

	public static void main(String[] args) {
		TEMPLATE_BASE = "/templates/fragments/";
		TEMPLATE_DIRECTORY = System.getProperty("user.dir") + "/src/main/resources" + TEMPLATE_BASE;
		HQ_UPLOAD_BASE = "/hq/";
		HQ_UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/src/main/resources" + HQ_UPLOAD_BASE;
		UPLOAD_BASE = "/uploads/";
		UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/src/main/resources" + UPLOAD_BASE;
		DRAWABLE_RESOURCES = System.getProperty("user.dir") + "/src/main/resources/static/drawable/";
		CSS_RESOURCES = System.getProperty("user.dir") + "/src/main/resources/static/css/";
		JS_RESOURCES = System.getProperty("user.dir") + "/src/main/resources/static/js/";
		AVATAR_BASE = "/avatars/";
		AVATAR_DIRECTORY = System.getProperty("user.dir") + "/src/main/resources" + AVATAR_BASE;

		SpringApplication.run(WellFbApplication.class, args);
	}


}
