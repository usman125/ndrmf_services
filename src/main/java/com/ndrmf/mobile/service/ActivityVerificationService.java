package com.ndrmf.mobile.service;
import com.ndrmf.common.AuthPrincipal;
import com.ndrmf.common.rFile;
import com.ndrmf.engine.dto.TpvTasksFilesListItem;
import com.ndrmf.engine.dto.TpvTasksListItem;
import com.ndrmf.engine.model.ProjectProposal;
import com.ndrmf.engine.model.TpvTasks;
import com.ndrmf.engine.model.TpvTasksFiles;
import com.ndrmf.engine.repository.ProjectProposalRepository;
import com.ndrmf.engine.service.FileStoreService;
import com.ndrmf.exception.ValidationException;
import com.ndrmf.mobile.dto.ActivityVerificationFilesListItem;
import com.ndrmf.mobile.dto.ActivityVerificationItem;
import com.ndrmf.mobile.dto.ActivityVerificationRequest;
import com.ndrmf.mobile.model.ActivityVerification;
import com.ndrmf.mobile.model.ActivityVerificationFiles;
import com.ndrmf.mobile.repository.ActivityVerificationRepository;
import com.ndrmf.user.dto.UserLookupItem;
import com.ndrmf.user.repository.UserRepository;
import com.ndrmf.user.service.UserService;
import com.ndrmf.util.enums.ProcessStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Service
public class ActivityVerificationService {
    @Autowired
    private FileStoreService fileStoreService;
    @Autowired
    private ActivityVerificationRepository avRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private UserService userService;
    @Autowired
    private ProjectProposalRepository projPropRepo;

    @Transactional
    public UUID commenceActivityVerification(
            AuthPrincipal principle,
            ActivityVerificationRequest body
    ) throws IOException {

        ProjectProposal p = projPropRepo.findById(body.getProposalId())
                .orElseThrow(() -> new ValidationException("INALID PROPOSAL ID"));

        ActivityVerification av = new ActivityVerification();
        av.setInitiatedBy(userRepo.findById(principle.getUserId()).get());
        av.setActivityId(body.getActivityId());
        av.setQuarter(body.getQuarter());
        av.setProjectProposalRef(p);
        av.setStatus(body.getStatus());
        av.setRating(body.getRating());
        av.setCreated_at(new Date());
        av.setGeneralComments(body.getComments());

        UUID avId;

        avId = avRepo.save(av).getId();

        return avId;
    }


    @Transactional
    public String uploadFileForActivityVerification(
            AuthPrincipal principle,
            UUID avId,
            MultipartFile file
    ) throws IOException {

        ActivityVerification av = avRepo.findById(avId)
                .orElseThrow(() -> new ValidationException("VERIFY ID is not valid."));

        rFile persistedFile;
        persistedFile = fileStoreService.saveFile(file, principle.getUserId());

        ActivityVerificationFiles avf = new ActivityVerificationFiles();
        avf.setFileRef(persistedFile);
        avf.setPicByte(file.getBytes());
        av.addFile(avf);

        return persistedFile.getPath();
    }

    @Transactional
    public ActivityVerificationItem getRequestsByActivityAndQuarter(AuthPrincipal currentUser, String activityId, int quarter) throws IOException {
        ActivityVerification av = avRepo.findRequestsForActivtyAndQuarter(activityId, quarter)
                .orElseThrow(() -> new ValidationException("ACTIVITY ID NOT VALID"));
        ActivityVerificationItem dto = new ActivityVerificationItem();

        dto.setId(av.getId());
        dto.setStatus(av.getStatus());
        dto.setInitiatedBy(new UserLookupItem(av.getInitiatedBy().getId(), av.getInitiatedBy().getFullName()));
        dto.setGeneralComments(av.getGeneralComments());
        dto.setCreated_at(av.getCreated_at());
        dto.setQuarter(av.getQuarter());
        dto.setActivityId(av.getActivityId());
        dto.setRating(av.getRating());
        
        av.getFiles().stream().forEach(c -> {
            ActivityVerificationFilesListItem avfli = new ActivityVerificationFilesListItem();
            avfli.setName(c.getFileRef().getFileName());
            avfli.setPath(c.getFileRef().getPath());
            avfli.setCreated_by(c.getCreatedBy());
            dto.addFile(avfli);
        });

        return dto;
    }
}
