package com.ndrmf.service;

import org.springframework.http.ResponseEntity;

import com.ndrmf.request.*;
import com.ndrmf.response.ServiceResponse;

public interface AccreditationService {

    ResponseEntity<ServiceResponse> getAllActiveSections();
    ResponseEntity<ServiceResponse> getAllActiveSectionsWithFormIdentity(String formIdentity);
    ResponseEntity<ServiceResponse> updateSections(SectionRequest sectionRequest);
    ResponseEntity<ServiceResponse> addSection(SectionRequest sectionRequest);

    ResponseEntity<ServiceResponse> getAllActiveAccreditation();
    ResponseEntity<ServiceResponse> updateAccreditation(AccUpdateRequest accUpdateRequest);
    ResponseEntity<ServiceResponse> addAccreditation(AccCreateRequest accCreateRequest);

    ResponseEntity<ServiceResponse> addReviewForAccreditation(ReviewCreateRequest reviewCreateRequest);
    ResponseEntity<ServiceResponse> getAllSectionReviews();
    ResponseEntity<ServiceResponse> getAccredSectionsReviews(AccredSectionReviewRequest accredSectionReviewRequest);
    ResponseEntity<ServiceResponse> getAccredLatestSectionReview(AccredSectionReviewRequest accredSectionReviewRequest);

    ResponseEntity<ServiceResponse> getAllSectionReviewsByReviewer(String reviewerName);
}
