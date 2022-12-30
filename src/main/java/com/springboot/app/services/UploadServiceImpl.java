package com.springboot.app.services;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadServiceImpl implements IUploadService {

	private final static String UPLOADS_FOLDER = "uploads";

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public Resource load(String filename) throws MalformedURLException {
		Path pathFoto = getPath(filename);
		log.info("pathFoto: " + pathFoto);

		Resource r = new UrlResource(pathFoto.toUri());
		if (!r.exists() && !r.isReadable()) {
			throw new RuntimeException("Error: no se puede cargar la imagen: " + pathFoto.toString());
		}
		return r;
	}

	@Override
	public String copy(MultipartFile file) throws IOException {
		String uniqueFilename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
		Path rootPath = getPath(uniqueFilename);

		log.info("rootPath: " + rootPath);

//		byte[] bytes = foto.getBytes();
//		Path rutaCompleta = Paths.get(rootPath + File.separator + foto.getOriginalFilename());
//		Files.write(rutaCompleta, bytes);

		Files.copy(file.getInputStream(), rootPath);

		return uniqueFilename;
	}

	@Override
	public boolean delete(String filename) {
		Path pathFoto = getPath(filename);
		File f = pathFoto.toFile();
		if (f.exists() && f.canRead())
			if (f.delete())
				return true;
		return false;
	}

	public Path getPath(String filename) {
		return Paths.get(UPLOADS_FOLDER).resolve(filename).toAbsolutePath();
	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(Paths.get(UPLOADS_FOLDER).toFile());
	}

	@Override
	public void init() throws IOException {
		Files.createDirectory(Paths.get(UPLOADS_FOLDER));
	}

}
