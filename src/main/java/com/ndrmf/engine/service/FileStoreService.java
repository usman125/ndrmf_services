package com.ndrmf.engine.service;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.ndrmf.common.File;
import com.ndrmf.common.FileRepository;
import com.ndrmf.user.repository.UserRepository;

@Service
public class FileStoreService {
	@Autowired private FileRepository fileRepo;
	@Autowired private UserRepository userRepo;
	
	private final String UPLOAD_PATH = "/home/uploads/";
	
	public File saveFile(MultipartFile file, UUID userId) {
		UUID id = UUID.randomUUID();
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		String ext = fileName.substring(fileName.lastIndexOf("."));
		StringBuilder pathBuilder = new StringBuilder();
		pathBuilder.append(UPLOAD_PATH).append(id).append(ext);
		String path = pathBuilder.toString();
		
		File f = new File();
		f.setId(id);
		f.setFileName(fileName);
		f.setPath(path);
		f.setUploadedAt(new Date());
		f.setUploadedBy(userRepo.getOne(userId));
		
		return fileRepo.save(f);
	}
}
