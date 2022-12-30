package com.springboot.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.springboot.app.services.IUploadService;

@SpringBootApplication
public class SpringBootDataJpaApplication implements CommandLineRunner {
	
	@Autowired
	private IUploadService uploadService;
	
	@Autowired
	private BCryptPasswordEncoder pwdEncoder;

	public static void main(String[] args) {
		SpringApplication.run(SpringBootDataJpaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		uploadService.deleteAll();
		uploadService.init();
		
//		String pwd = "12345";
//		for (int i=0; i<2;i++) {
//			String ePwd = pwdEncoder.encode(pwd);
//			System.out.println(ePwd);
//		}
	}

}
