package com.ndrmf.engine.service;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.ndrmf.common.rFile;
import com.ndrmf.common.FileRepository;
import com.ndrmf.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.File;


@Service
public class FileStoreService {
	@Autowired private FileRepository fileRepo;
	@Autowired private UserRepository userRepo;
	
	public final String UPLOAD_PATH = "/home/tempUploads/";

	@Transactional
	public rFile saveFile(MultipartFile file, UUID userId) throws IOException {
		UUID id = UUID.randomUUID();
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		String ext = fileName.substring(fileName.lastIndexOf("."));
		StringBuilder pathBuilder = new StringBuilder();
		pathBuilder.append(UPLOAD_PATH).append(id).append(ext);
		String path = pathBuilder.toString();
		
		rFile f = new rFile();
		f.setId(id);
		f.setFileName(fileName);
		f.setPath(path);
		f.setUploadedAt(new Date());
		f.setUploadedBy(userRepo.getOne(userId));
		f.setFileType(ext);
		try {
			f.setData(file.getBytes());
		}
		catch(IOException ex)
		{
			throw new IllegalArgumentException("Cannot read contents of multipart file", ex);
		}
		
//		File dest = new File(path);
//
//		try {
//			file.transferTo(dest);
//		} catch (IllegalStateException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	
		
		return fileRepo.save(f);
	}
	
	public rFile readFile(UUID userId) {
		rFile file = new rFile();
		return file;
	}
}
