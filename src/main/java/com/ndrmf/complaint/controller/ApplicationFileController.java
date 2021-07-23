package com.ndrmf.complaint.controller;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ndrmf.complaint.dto.UploadFileResponseDTO;
import com.ndrmf.complaint.model.ComplaintAttachment;
import com.ndrmf.complaint.repository.ComplaintAttachmentRepository;
import com.ndrmf.complaint.service.FileStorageService;

import io.netty.handler.codec.http.HttpResponse;

@RestController
@RequestMapping("/complaint/attachment/")
public class ApplicationFileController {
	
	private static final Logger logger = LoggerFactory.getLogger(ApplicationFileController.class);

	@Autowired
	private ComplaintAttachmentRepository attachmentRepo;
    @Autowired
    private FileStorageService fileStorageService;
    
    @PostMapping("/uploadFile")
    public UploadFileResponseDTO uploadFile(@RequestParam("file") MultipartFile file, 
    		@RequestParam("applicationRefNo") UUID complaintId) {
    	UploadFileResponseDTO uploadFileResponseDTO = fileStorageService.storeFile(file,complaintId);
        return uploadFileResponseDTO;
    }
//    @GetMapping("/downloadFile/{complaintId}/{fileName:.+}")
    @GetMapping("/downloadFile/{complaintId}/")
    public ResponseEntity<Resource> downloadFile(@PathVariable UUID complaintId, HttpServletRequest request) {
        // Load file as Resource
        
    	ComplaintAttachment attachments = this.attachmentRepo.getComplaintAttachmentByComplaintId(complaintId).get(0);       	
    	
    	Resource resource = fileStorageService.loadFileAsResource(attachments.getFileName(),complaintId.toString());

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
        
    	
    
  @GetMapping("/downloadFile/{complaintId}/{fileName:.+}")
  public ResponseEntity<Resource> downloadFileByName(@PathVariable UUID complaintId,@PathVariable String fileName, HttpServletRequest request) {
      // Load file as Resource
  	Resource resource = fileStorageService.loadFileAsResource(fileName,complaintId.toString());

      // Try to determine file's content type
      String contentType = null;
      try {
          contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
      } catch (IOException ex) {
          logger.info("Could not determine file type.");
      }

      // Fallback to the default content type if type could not be determined
      if(contentType == null) {
          contentType = "application/octet-stream";
      }

      return ResponseEntity.ok()
              .contentType(MediaType.parseMediaType(contentType))
              .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
              .body(resource);
  }

    
    
    
}
