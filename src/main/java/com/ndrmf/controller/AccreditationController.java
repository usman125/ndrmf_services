package com.ndrmf.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.ndrmf.request.*;
import com.ndrmf.response.ServiceResponse;
import com.ndrmf.service.AccreditationService;
import com.ndrmf.service.AccreditationServiceImpl;
import com.ndrmf.service.FormService;
import com.ndrmf.service.FormServiceImpl;

import javax.validation.Valid;

@RequestMapping("/accreditation")
@RestController
public class AccreditationController {

    private final AccreditationService accreditationService;
    private final FormService formService;

    public AccreditationController(AccreditationServiceImpl accreditationServiceImpl, FormServiceImpl formServiceImpl) {
        this.accreditationService = accreditationServiceImpl;
        this.formService = formServiceImpl;
    }

    @PostMapping("/addSection")
    @Transactional
    public ResponseEntity<ServiceResponse> addSection(@Valid @RequestBody SectionRequest sectionRequest){
        return accreditationService.addSection(sectionRequest);
    }


    @GetMapping("/getAllActiveSections")
    @Transactional(readOnly = true)
    public ResponseEntity<ServiceResponse> getActiveSections(){
        return accreditationService.getAllActiveSections();
    }
    @GetMapping("/getAllActiveSectionsWithFormIdentity/{formIdentity}")
    @Transactional(readOnly = true)
    public ResponseEntity<ServiceResponse> getActiveSectionsWithFormIdentity(@PathVariable String formIdentity){
        return accreditationService.getAllActiveSectionsWithFormIdentity(formIdentity);
    }

    @PutMapping("/updateSection")
    @Transactional
    public ResponseEntity<ServiceResponse> updateActiveSection(@Valid @RequestBody SectionRequest sectionRequest){
        return accreditationService.updateSections(sectionRequest);
    }

    @PostMapping("/addSurvey")
    @Transactional
    public ResponseEntity<ServiceResponse> createSurvey(@Valid @RequestBody FormCreationRequest formCreationRequest){
        return formService.createForm(formCreationRequest);
    }

    @PutMapping("/UpdateServay")
    @Transactional
    public ResponseEntity<ServiceResponse> updateSurvey(@Valid @RequestBody FormUpdationRequest formUpdationRequest){
        return formService.updateForm(formUpdationRequest);
    }

    @GetMapping("/getAllActivesurvey")
    @Transactional(readOnly = true)
    public ResponseEntity<ServiceResponse> getAllSurvey(){
        return formService.getAllForms();
    }

    @Transactional
    @PostMapping("/addAccreditation")
    public ResponseEntity<ServiceResponse> createAccreditation(@Valid @RequestBody AccCreateRequest accCreateRequest){
        return accreditationService.addAccreditation(accCreateRequest);
    }

    @Transactional
    @PutMapping("/updateAccreditation")
    public ResponseEntity<ServiceResponse> updateAccreditation(@Valid @RequestBody AccUpdateRequest accUpdateRequest){
        return accreditationService.updateAccreditation(accUpdateRequest);
    }

    @Transactional(readOnly = true)
    @GetMapping("/getAllAccreditations")
    public ResponseEntity<ServiceResponse> getAllAccreditations(){
        return accreditationService.getAllActiveAccreditation();
    }

    @Transactional
    @PostMapping("/addReview")
    public ResponseEntity<ServiceResponse> addReview(@Valid @RequestBody ReviewCreateRequest reviewCreateRequest){
        return accreditationService.addReviewForAccreditation(reviewCreateRequest);
    }
    @Transactional
    @PutMapping("/getAccreditationsSectionReviews")
    public ResponseEntity<ServiceResponse> getAllAccredSectionReviews(@Valid @RequestBody AccredSectionReviewRequest accredSectionReviewRequest){
        return accreditationService.getAccredSectionsReviews(accredSectionReviewRequest);
    }
    @Transactional
    @PutMapping("/getLatestAccredSectionReviews")
    public ResponseEntity<ServiceResponse> getLatestAccredSectionReviews(@Valid @RequestBody AccredSectionReviewRequest accredSectionReviewRequest){
        return accreditationService.getAccredLatestSectionReview(accredSectionReviewRequest);
    }
    @Transactional
    @GetMapping("/getAllSectionReviews")
    public ResponseEntity<ServiceResponse> geAllAccredSectionReviews(){
        return accreditationService.getAllSectionReviews();
    }
    @Transactional
    @GetMapping("/getAllSectionReviewsByUser/{reviewerName}")
    public ResponseEntity<ServiceResponse> geAllAccredSectionReviewsByUser(@PathVariable String reviewerName){
        return accreditationService.getAllSectionReviewsByReviewer(reviewerName);
    }

}
