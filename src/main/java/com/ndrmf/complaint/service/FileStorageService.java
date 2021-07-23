package com.ndrmf.complaint.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ndrmf.complaint.dto.UploadFileResponseDTO;
import com.ndrmf.complaint.model.Complaint;
import com.ndrmf.complaint.model.ComplaintAttachment;
import com.ndrmf.complaint.repository.ComplaintAttachmentRepository;
import com.ndrmf.complaint.repository.ComplaintRepository;
import com.ndrmf.exception.FileStorageException;
import com.ndrmf.exception.MyFileNotFoundException;
import com.ndrmf.exception.ValidationException;

@Service
public class FileStorageService {
	
	@Autowired private ComplaintAttachmentRepository fileStorageRepo;
	@Autowired private ComplaintRepository complaintRepo;
	
	private Path fileStorageLocation;
	private static final String UploadDir="/home/uploads";

    public UploadFileResponseDTO storeFile(MultipartFile file, UUID applicationRefNo) {
        // Normalize file name
    	this.fileStorageLocation = Paths.get(UploadDir+"/"+applicationRefNo)
                .toAbsolutePath().normalize();

		try {
			if (Files.exists(fileStorageLocation)) {
				System.out.println("Path Already Exists");
			} else {
				Files.createDirectories(this.fileStorageLocation);
			}
            
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
           
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
            		.path("/file")
                    .path("/downloadFile")
                    .path("/"+applicationRefNo)
                    .path("/"+fileName)
                    .toUriString();
            SaveFileInDB(applicationRefNo,targetLocation.toString(),fileDownloadUri,fileName);
            return new UploadFileResponseDTO(fileName, fileDownloadUri,
                    file.getContentType(), file.getSize());
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    private void SaveFileInDB(UUID applicationRefNo, String uploadPath, String fileDownloadUri, String fileName) {
    	try {
    		ComplaintAttachment complaintAttachment = new ComplaintAttachment();
    		Complaint c = complaintRepo.findById(applicationRefNo)
    				.orElseThrow(() -> new ValidationException("Invalid Complaint ID"));
    		
    		complaintAttachment.setComplaintRef(c);
    		complaintAttachment.setFileName(fileName);
    		complaintAttachment.setPath(uploadPath);
    		complaintAttachment.setUploadedAt(new Date());
    		fileStorageRepo.save(complaintAttachment);

			} catch (Exception e) {
			e.printStackTrace();
		}
    	
    }

	public Resource loadFileAsResource(String fileName, String ApplicationRefNo) {
        try {
        	this.fileStorageLocation = Paths.get(UploadDir+"/"+ApplicationRefNo)
                    .toAbsolutePath().normalize();

            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + fileName, ex);
        }
    }

}
