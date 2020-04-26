package com.ndrmf.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ndrmf.model.*;
import com.ndrmf.repository.*;
import com.ndrmf.request.*;
import com.ndrmf.response.ServiceResponse;
import com.ndrmf.user.model.User;
import com.ndrmf.user.repository.UserRepository;
import com.ndrmf.utils.CommonConstants;
import com.ndrmf.utils.CommonUtils;

import java.util.*;

@Service
public class AccreditationServiceImpl implements AccreditationService {

    private final SectionRepository sectionRepository;
    private final UserRepository userRepository;
    private final AccreditationRepository accreditationRepository;
    private final SectionReviewRepository sectionReviewRepository;
    private final ComponentReviewRepository componentReviewRepository;

    public AccreditationServiceImpl(SectionRepository sectionRepository, UserRepository userRepository, AccreditationRepository accreditationRepository,SectionReviewRepository sectionReviewRepository, ComponentReviewRepository componentReviewRepository) {
        this.sectionRepository = sectionRepository;
        this.userRepository = userRepository;
        this.accreditationRepository = accreditationRepository;
        this.sectionReviewRepository = sectionReviewRepository;
        this.componentReviewRepository = componentReviewRepository;
    }


    @Override
    public ResponseEntity<ServiceResponse>  getAllActiveSections() {
        ResponseEntity<ServiceResponse> responseEntity;
        ServiceResponse serviceResponse;
        List<Section> sections = null;

        sections = sectionRepository.findAllByActiveTrue();

        if (CommonUtils.isNullOrEmptyCollection(sections)) {
            serviceResponse = CommonUtils.dataNotFoundResponse(null);
        } else {
            serviceResponse = CommonUtils.mapGetSections(sections);
        }
        responseEntity = ResponseEntity.ok(serviceResponse);
        return responseEntity;

    }

    @Override
    public ResponseEntity<ServiceResponse> getAllActiveSectionsWithFormIdentity(String formIdentity) {
        ResponseEntity<ServiceResponse> responseEntity;
        ServiceResponse serviceResponse;
        List<Section> sections = null;
        sections = sectionRepository.findByFormIdentityAndActiveTrue(formIdentity);

        if (CommonUtils.isNullOrEmptyCollection(sections)) {
            serviceResponse = CommonUtils.dataNotFoundResponse(null);
        } else {
            serviceResponse = CommonUtils.mapGetSections(sections);
        }
        responseEntity = ResponseEntity.ok(serviceResponse);
        return responseEntity;


    }

    @Override
    public ResponseEntity<ServiceResponse> updateSections(SectionRequest sectionRequest) {
        ResponseEntity<ServiceResponse> responseEntity;
        ServiceResponse serviceResponse;

        if (CommonUtils.isInvalidSection(sectionRequest)) {

            serviceResponse = CommonUtils.invalidClientReqResponse();
            responseEntity = ResponseEntity.badRequest().body(serviceResponse);
        } else {
            Section section = sectionRepository.findBySectionKey(sectionRequest.getSectionKey());
            if (null == section) {
                serviceResponse = CommonUtils.dataNotFoundResponse(null);
                responseEntity = ResponseEntity.status(HttpStatus.NOT_FOUND).body(serviceResponse);
            } else {
                section = sectionRepository.save(CommonUtils.mapSectionAdditionRequest(sectionRequest));
                serviceResponse = CommonUtils.successResponse(CommonConstants.UPDATE_USER_SECTION_SUCCESS_DESC);
                responseEntity = ResponseEntity.ok(serviceResponse);
            }
        }


        return responseEntity;
    }

    @Override
    public ResponseEntity<ServiceResponse> addSection(SectionRequest sectionRequest) {
        ServiceResponse serviceResponse;
        ResponseEntity<ServiceResponse> responseEntity;

        if (CommonUtils.isInvalidSection(sectionRequest)) {
            serviceResponse = CommonUtils.invalidClientReqResponse();
            return ResponseEntity.badRequest().body(serviceResponse);
        }

        Section section = CommonUtils.mapSectionAdditionRequest(sectionRequest);
        sectionRepository.save(section);
        serviceResponse = CommonUtils.successResponse(CommonConstants.UPDATE_USER_SECTION_SUCCESS_DESC);
        return ResponseEntity.ok(serviceResponse);
    }

    @Override
    public ResponseEntity<ServiceResponse> getAllActiveAccreditation() {
        ResponseEntity<ServiceResponse> responseEntity;
        ServiceResponse serviceResponse;

        List<Accreditation> accreditations = accreditationRepository.findAll();
        if(CommonUtils.isNullOrEmptyCollection(accreditations)){
            serviceResponse = CommonUtils.dataNotFoundResponse(null);
        } else {
            serviceResponse = CommonUtils.mapAccreditationResponse(accreditations);
        }
        responseEntity = ResponseEntity.ok(serviceResponse);

        return responseEntity;
    }

    @Override
    public ResponseEntity<ServiceResponse> updateAccreditation(AccUpdateRequest accUpdateRequest) {
        ResponseEntity<ServiceResponse> responseEntity;
        ServiceResponse serviceResponse;

        if(CommonUtils.isInvalidAccUpdateRequest(accUpdateRequest)){
            serviceResponse = CommonUtils.invalidClientReqResponse();
            responseEntity = ResponseEntity.badRequest().body(serviceResponse);
        } else {

            Optional<Accreditation> optionalAccreditation = Optional.ofNullable(accreditationRepository.findByAccreditationSection_SectionKeyAndAccUser_Username(accUpdateRequest.getSectionKey(), accUpdateRequest.getUserName()));

            if(!optionalAccreditation.isPresent()){
                serviceResponse = CommonUtils.dataNotFoundResponse(null);
                responseEntity = ResponseEntity.status(HttpStatus.NOT_FOUND).body(serviceResponse);
            } else {
                Accreditation accreditation = optionalAccreditation.get();
                CommonUtils.updateAccreditationObj(accreditation, accUpdateRequest);
                accreditationRepository.save(accreditation);
                serviceResponse = CommonUtils.successResponse(CommonConstants.ACCREDITATION_UPDATE_SUCCESS_DESC);
                responseEntity = ResponseEntity.ok(serviceResponse);
            }
        }

        return responseEntity;
    }

    @Override
    public ResponseEntity<ServiceResponse> addAccreditation(AccCreateRequest accCreateRequest) {
        ResponseEntity<ServiceResponse> responseEntity;
        ServiceResponse serviceResponse;

        if(CommonUtils.isInvalidAccCreationRequest(accCreateRequest)){
            serviceResponse = CommonUtils.invalidClientReqResponse();
            responseEntity = ResponseEntity.badRequest().body(serviceResponse);
        } else {
            User user = userRepository.findByUsername(accCreateRequest.getUserName());
            Section section = sectionRepository.findBySectionKey(accCreateRequest.getSectionKey());
            Optional<Accreditation> optionalAccr = Optional.ofNullable(accreditationRepository.findByAccreditationSection_SectionKeyAndAccUser_Username(accCreateRequest.getSectionKey(), accCreateRequest.getUserName()));
            if(null == user || null == section){
                serviceResponse = CommonUtils.dataNotFoundResponse(null);
                responseEntity = ResponseEntity.status(HttpStatus.NOT_FOUND).body(serviceResponse);
            } else if(optionalAccr.isPresent()) {
                serviceResponse = CommonUtils.duplicateResourceResponse();
                responseEntity = ResponseEntity.status(HttpStatus.CONFLICT).body(serviceResponse);
            }else {
                Accreditation accreditation = CommonUtils.mapAccCreationRequest(accCreateRequest, user, section);
                accreditationRepository.save(accreditation);
                serviceResponse = CommonUtils.successResponse(CommonConstants.ACCREDITATION_CREATION_SUCCESS_DESC);
                responseEntity = ResponseEntity.ok(serviceResponse);
            }
        }

        return responseEntity;
    }

    @Override
    public ResponseEntity<ServiceResponse> addReviewForAccreditation(ReviewCreateRequest reviewCreateRequest) {
        ResponseEntity<ServiceResponse> responseEntity;
        ServiceResponse serviceResponse;

        if(CommonUtils. isInvalidReviewCreateRequest(reviewCreateRequest)){

            serviceResponse = CommonUtils.invalidClientReqResponse();
            responseEntity = ResponseEntity.badRequest().body(serviceResponse);
        } else {
            Optional<Accreditation> optionalAccr = Optional.ofNullable(accreditationRepository.findByAccreditationSection_SectionKeyAndAccUser_Username(reviewCreateRequest.getSectionKey(), reviewCreateRequest.getUsername()));
            User sectionReviewer = userRepository.findByUsername(reviewCreateRequest.getSectionReviewer());

            if(optionalAccr.isPresent() || null == sectionReviewer){

                Accreditation accreditation = optionalAccr.get();
                Set<ComponentReview> componentReviewSet = new HashSet<>();
                SectionReview sectionReview = CommonUtils.mapReviewCreateRequest(reviewCreateRequest,sectionReviewer, accreditation);

                for (CompReviewCreateRequest compReviewCreateRequest : reviewCreateRequest.getCompReviewCreateRequestList()) {
                    ComponentReview componentReview = CommonUtils.mapCompReviewCreateRequest(compReviewCreateRequest,sectionReview);
                    componentReviewSet.add(componentReview);
                }
                sectionReview.setComponentReviewSet(componentReviewSet);
                sectionReviewRepository.save(sectionReview);
                serviceResponse = CommonUtils.successResponse(CommonConstants.SECTION_REVIEW_CREATE_SUCCESS_DESC);
            } else {
                serviceResponse = CommonUtils.dataNotFoundResponse(null);
            }
            responseEntity = ResponseEntity.ok(serviceResponse);
        }
        return responseEntity;
    }

    @Override
    public ResponseEntity<ServiceResponse> getAllSectionReviews() {
        ResponseEntity<ServiceResponse> responseEntity;
        ServiceResponse serviceResponse;

        List<SectionReview> sectionReviewList = sectionReviewRepository.findAll();

        if(CommonUtils.isNullOrEmptyCollection(sectionReviewList)){
            serviceResponse = CommonUtils.dataNotFoundResponse(CommonConstants.SECTION_REVIEW_NOT_FOUND_DESC);
        } else {
            serviceResponse = CommonUtils.mapSectionReviews(sectionReviewList);
        }
        responseEntity = ResponseEntity.ok(serviceResponse);

        return responseEntity;
    }

    @Override
    public ResponseEntity<ServiceResponse> getAccredSectionsReviews(AccredSectionReviewRequest accredSectionReviewRequest) {
        ResponseEntity<ServiceResponse> responseEntity;
        ServiceResponse serviceResponse;

        if(CommonUtils.isInvalidAccredSectionReviewRequest(accredSectionReviewRequest)){
            serviceResponse = CommonUtils.invalidClientReqResponse();
            responseEntity = ResponseEntity.badRequest().body(serviceResponse);
        } else {
            Optional<Accreditation> optionalAccred = Optional.ofNullable(accreditationRepository.findByAccreditationSection_SectionKeyAndAccUser_Username(accredSectionReviewRequest.getSectionKey(), accredSectionReviewRequest.getUsername()));
            if(!optionalAccred.isPresent() || CommonUtils.isNullOrEmptyCollection(optionalAccred.get().getSectionReviewSet())){
                serviceResponse = CommonUtils.dataNotFoundResponse(CommonConstants.SECTION_REVIEW_NOT_FOUND_DESC);
            } else {
                serviceResponse = CommonUtils.mapSectionReviews(new ArrayList<>(optionalAccred.get().getSectionReviewSet()));
            }
            responseEntity = ResponseEntity.ok(serviceResponse);
        }

        return responseEntity;
    }

    @Override
    public ResponseEntity<ServiceResponse> getAccredLatestSectionReview(AccredSectionReviewRequest accredSectionReviewRequest) {
        ResponseEntity<ServiceResponse> responseEntity;
        ServiceResponse serviceResponse;

        if(CommonUtils.isInvalidAccredSectionReviewRequest(accredSectionReviewRequest)){
            serviceResponse = CommonUtils.invalidClientReqResponse();
            responseEntity = ResponseEntity.badRequest().body(serviceResponse);
        } else {
            Optional<Accreditation> optionalAccred = Optional.ofNullable(accreditationRepository.findByAccreditationSection_SectionKeyAndAccUser_Username(accredSectionReviewRequest.getSectionKey(),accredSectionReviewRequest.getUsername()));
            if(!optionalAccred.isPresent() || CommonUtils.isNullOrEmptyCollection(optionalAccred.get().getSectionReviewSet())){
                serviceResponse = CommonUtils.dataNotFoundResponse(CommonConstants.SECTION_REVIEW_NOT_FOUND_DESC);
            } else {
                List<SectionReview> sectionReviewList = new ArrayList<>();
                sectionReviewList.add(CommonUtils.getLatestSectionReview(optionalAccred.get().getSectionReviewSet()));
                serviceResponse = CommonUtils.mapSectionReviews(sectionReviewList);
            }
            responseEntity = ResponseEntity.ok(serviceResponse);
        }

        return responseEntity;
    }

    @Override
    public ResponseEntity<ServiceResponse> getAllSectionReviewsByReviewer(String reviewerName) {
        ServiceResponse serviceResponse;
        List<SectionReview> sectionReviews;
        User user =  userRepository.findByUsername(reviewerName);
        if(null == user ) {
            return  ResponseEntity.ok(CommonUtils.dataNotFoundResponse(CommonConstants.SECTION_REVIEW_NOT_FOUND_DESC));
        }
        sectionReviews = sectionReviewRepository.findAllBySectionReviewer(user);
        if(CommonUtils.isNullOrEmptyCollection(sectionReviews)) {
            serviceResponse = CommonUtils.dataNotFoundResponse(CommonConstants.SECTION_REVIEW_NOT_FOUND_DESC);
        } else {
            serviceResponse = CommonUtils.mapSectionReviews(sectionReviews);
        }
        return ResponseEntity.ok(serviceResponse);
    }


}


