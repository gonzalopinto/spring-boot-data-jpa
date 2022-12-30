package com.springboot.app.services;

import java.io.IOException;
import java.net.MalformedURLException;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface IUploadService {

	Resource load(String filename) throws MalformedURLException;

	String copy(MultipartFile file) throws IOException;

	boolean delete(String filename);
	
	void deleteAll();
	
	void init() throws IOException;

}
