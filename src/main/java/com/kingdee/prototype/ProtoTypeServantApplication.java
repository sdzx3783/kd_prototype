package com.kingdee.prototype;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class ProtoTypeServantApplication {
	
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(ProtoTypeServantApplication.class);
		app.setBannerMode(Banner.Mode.OFF);
		app.run(args);
	}
}
