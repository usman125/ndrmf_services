package com.ndrmf.util;

import com.ndrmf.model.*;
import com.ndrmf.request.*;
import com.ndrmf.response.*;
import com.ndrmf.setting.model.Section;
import com.ndrmf.user.model.Role;
import com.ndrmf.user.model.User;

import java.util.*;

public class CommonUtils {

    private CommonUtils() {
    }

    public static User mapUserRegdRequest(UserRegdRequest userRegdServiceRequest){
        User user = new User();
        user.setUsername(userRegdServiceRequest.getUsername());
        user.setEmail(userRegdServiceRequest.getEmail());
        user.setPassword(userRegdServiceRequest.getPassword());

        return user;
    }

    public static ServiceResponse mapUserRegdResponse(User user){
        UserRegdResponse userRegdServiceResponse = new UserRegdResponse();

        userRegdServiceResponse.setResponseCode(ResponseCode.SUCCESS.getRespCode());
        userRegdServiceResponse.setResponseDesc(CommonConstants.USER_REGISTRATION_SUCCESS_DESC);
        userRegdServiceResponse.setEmail(user.getEmail());
        userRegdServiceResponse.setUsername(user.getUsername());
        userRegdServiceResponse.setFirstName(user.getFirstName());
        userRegdServiceResponse.setFamilyName(user.getLastName());

        return userRegdServiceResponse;
    }

    public static boolean isInvalidLoginRequest(LoginRequest loginRequest){
        return (null == loginRequest
                || null == loginRequest.getUsername()
                || null == loginRequest.getPassword());
    }

    public static ServiceResponse mapLoginResponse(User user){
        LoginResponse loginResponse = new LoginResponse();

        loginResponse.setResponseCode(ResponseCode.SUCCESS.getRespCode());
        loginResponse.setResponseDesc(CommonConstants.LOGIN_SUCCESS_DESC);
        loginResponse.setUserInfo(prepareUserInfo(user));

        return loginResponse;
    }

    public static ServiceResponse mapGetUsers(List<User> users){
        UserResponse userResponse = new UserResponse();
        List<UserInfo> userInfos = new ArrayList<>();

        userResponse.setResponseCode(ResponseCode.SUCCESS.getRespCode());
        userResponse.setResponseDesc(ResponseCode.SUCCESS.getRespDesc());
        for (User user : users) {
            userInfos.add(prepareUserInfo(user));
        }
        userResponse.setUserInfoList(userInfos);

        return userResponse;
    }

    private static UserInfo prepareUserInfo(User user){
        UserInfo userInfo = new UserInfo();

        userInfo.setEmail(user.getEmail());
        userInfo.setUsername(user.getUsername());
        userInfo.setFirstName(user.getFirstName());
        userInfo.setFamilyName(user.getLastName());

        List<String> roles = new ArrayList<>();
        for (Role role : user.getAuthorities()) {
            roles.add(role.getName());
        }
        userInfo.setRoleNames(roles);

        return userInfo;
    }

    public static boolean isInvalidDefineTypeRequest(TypeRequest typeRequest){
        return (null == typeRequest
                || null == typeRequest.getTypeName());
    }

    public static boolean isInvalidUserRoleRequest(AddRoleUserRequest addRoleUserRequest){
        return (null == addRoleUserRequest
                || null == addRoleUserRequest.getUsername()
                || null == addRoleUserRequest.getName());
    }

    public static ServiceResponse mapAddRoleUserResponse(User user){
        AddRoleUserResponse addRoleUserResponse = new AddRoleUserResponse();

        addRoleUserResponse.setResponseCode(ResponseCode.SUCCESS.getRespCode());
        addRoleUserResponse.setResponseDesc(CommonConstants.USER_ROLE_DEFINED_SUCCESS_DESC);
        addRoleUserResponse.setUsername(user.getUsername());

        return addRoleUserResponse;
    }
    public static Set<String> mapGetRoleNames(Set<Role> roleSet){
        Set<String> roleSetName = new HashSet<>();
        for (Role role:roleSet) {
            roleSetName.add(role.getName());
        }
        return roleSetName;
    }

    public static boolean isNullOrEmptyCollection(Collection<?> collection){
        return null == collection || collection.isEmpty();
    }

    public static ServiceResponse mapRoleResponse(List<Role> roles){
        RoleResponse roleResponse = new RoleResponse();
        List<String> roleNames = new ArrayList<>();

        roleResponse.setResponseCode(ResponseCode.SUCCESS.getRespCode());
        roleResponse.setResponseDesc(ResponseCode.SUCCESS.getRespDesc());
        for (Role role : roles) {
            roleNames.add(role.getName());
        }
        roleResponse.setRoles(roleNames);

        return roleResponse;
    }

    public static boolean isInvalidUsrRegdRequest(UserRegdRequest userRegdRequest){
        return null == userRegdRequest
                || null == userRegdRequest.getEmail()
                || null == userRegdRequest.getUsername()
                || null == userRegdRequest.getPassword()
                || null == userRegdRequest.getTypeName();
    }

    public static ServiceResponse invalidClientReqResponse(){
        ServiceResponse serviceResponse = new ServiceResponse();
        serviceResponse.setResponseCode(ResponseCode.INVALID_CLIENT_REQUEST.getRespCode());
        serviceResponse.setResponseDesc(ResponseCode.INVALID_CLIENT_REQUEST.getRespDesc());
        return serviceResponse;
    }

    public static ServiceResponse invalidCredentialsResponse(){
        ServiceResponse serviceResponse = new ServiceResponse();
        serviceResponse.setResponseCode(ResponseCode.INVALID_CREDENTIALS.getRespCode());
        serviceResponse.setResponseDesc(ResponseCode.INVALID_CREDENTIALS.getRespDesc());
        return serviceResponse;
    }

    public static ServiceResponse duplicateResourceResponse(){
        ServiceResponse serviceResponse = new ServiceResponse();
        serviceResponse.setResponseCode(ResponseCode.DUPLICATE_RESOURCE.getRespCode());
        serviceResponse.setResponseDesc(ResponseCode.DUPLICATE_RESOURCE.getRespDesc());
        return serviceResponse;
    }

    public static ServiceResponse dataNotFoundResponse(String responseDesc){
        ServiceResponse serviceResponse = new ServiceResponse();
        serviceResponse.setResponseCode(ResponseCode.DATA_NOT_FOUND.getRespCode());
        if(null == responseDesc){
            responseDesc = ResponseCode.DATA_NOT_FOUND.getRespDesc();
        }
        serviceResponse.setResponseDesc(responseDesc);
        return serviceResponse;
    }

    public static ServiceResponse successResponse(String desc) {
        ServiceResponse serviceResponse = new ServiceResponse();
        serviceResponse.setResponseCode(ResponseCode.SUCCESS.getRespCode());
        if (null == desc) {
            serviceResponse.setResponseDesc(ResponseCode.SUCCESS.getRespDesc());
        } else {
            serviceResponse.setResponseDesc(desc);
        }
        return serviceResponse;
    }


    public static boolean isInvalidUserGroupRequest(UserGroupRequest userGroupRequest){
        return null == userGroupRequest
                || null == userGroupRequest.getUsername()
                || null  == userGroupRequest.getGroupName();
    }

    public static ServiceResponse mapUserGroupResponse(User user){
        UserGroupResponse userGroupResponse = new UserGroupResponse();
        List<String> groupNames = new ArrayList<>();

        userGroupResponse.setResponseCode(ResponseCode.SUCCESS.getRespCode());
        userGroupResponse.setResponseDesc(CommonConstants.USER_GROUP_DEFINED_SUCCESS_DESC);

        return userGroupResponse;
    }

    public static boolean isInvalidProfileUpdate(ProfileUpdationRequest profileUpdationRequest){
        return null == profileUpdationRequest
                || null == profileUpdationRequest.getUsername();
    }

    public static boolean isInvalidSection(SectionRequest sectionRequest){
        return null == sectionRequest
                || null == sectionRequest.getSectionName() || null == sectionRequest.getSectionKey();
    }

    public static boolean isInvalidFormCreationRequest(FormCreationRequest formCreationRequest){
        return null == formCreationRequest
                || null == formCreationRequest.getSectionName()
                || null == formCreationRequest.getDisplayType()
                || null == formCreationRequest.getFormIdentity()
                || null == formCreationRequest.getStatus()
                || null == formCreationRequest.getComponent()
                || null == formCreationRequest.getSectionKey();
    }

    public static Form mapFormCreationRequest(FormCreationRequest formCreationRequest, Section section){
        Form form = new Form();

        form.setName(formCreationRequest.getSectionName());
        form.setDisplayType((formCreationRequest.getDisplayType()));
        form.setFormIdentity(formCreationRequest.getFormIdentity());
        form.setPassingScore(formCreationRequest.getPassingScore());
        form.setTotalScore(formCreationRequest.getTotalScore());
        form.setStatus(formCreationRequest.getStatus());
        form.setComponent(formCreationRequest.getComponent());
        form.setSection(section);
        form.setActive(true);

        return form;
    }

    public static boolean isInvalidFormUpdationRequest(FormUpdationRequest formUpdationRequest){
        return null == formUpdationRequest
                || null == formUpdationRequest.getSectionKey()
                || null == formUpdationRequest.getStatus();
    }

    public static boolean isInvalidAccCreationRequest(AccCreateRequest accCreateRequest){

        return null == accCreateRequest
                || null == accCreateRequest.getUserName()
                || null == accCreateRequest.getSectionKey()
                || null == accCreateRequest.getFormSubmitData()
                || null == accCreateRequest.getStatus()
                || null == accCreateRequest.getFormIdentity()
                || null == accCreateRequest.getRequestKey();
    }

    public static Accreditation mapAccCreationRequest(AccCreateRequest accCreateRequest, User user, Section section){
        Accreditation accreditation = new Accreditation();

        accreditation.setAccUser(user);
        accreditation.setAccreditationSection(section);
        accreditation.setFormIdentity(accCreateRequest.getFormIdentity());
        accreditation.setFormSubmitData(accCreateRequest.getFormSubmitData());
        accreditation.setStatus(accCreateRequest.getStatus());
        accreditation.setStartDate(accCreateRequest.getStartDate());
        accreditation.setEndDate(accCreateRequest.getEndDate());
        accreditation.setRequestKey(accCreateRequest.getRequestKey());
        accreditation.setUserUpdateFlag(accCreateRequest.isUserUpdateFlag());
        accreditation.setRating(accCreateRequest.getRatings());
        accreditation.setCurrentReview(accCreateRequest.getCurrentReview());
        accreditation.setPrevReview(accCreateRequest.getPrevReview());

        return accreditation;
    }

    public static boolean isInvalidAccUpdateRequest(AccUpdateRequest accUpdateRequest){

        return null == accUpdateRequest
                || null == accUpdateRequest.getSectionKey()
                || null == accUpdateRequest.getUserName()
                || null == accUpdateRequest.getFormData()
                || null == accUpdateRequest.getStatus();
    }

    public static void updateAccreditationObj(Accreditation accreditation, AccUpdateRequest accUpdateRequest){
        accreditation.setFormSubmitData(accUpdateRequest.getFormSubmitData());
        //accreditation.setFormdata
        accreditation.setStatus(accUpdateRequest.getStatus());
        accreditation.setStartDate(accUpdateRequest.getStartDate());
        accreditation.setEndDate(accUpdateRequest.getEndDate());
        accreditation.setUserUpdateFlag(accUpdateRequest.isUserUpdateFlag());
        accreditation.setRating(accUpdateRequest.getRatings());
        accreditation.setCurrentReview(null == accUpdateRequest.getCurrentReview() ? accreditation.getCurrentReview() : accUpdateRequest.getCurrentReview());
        accreditation.setPrevReview(null == accUpdateRequest.getPrevReview() ? accreditation.getPrevReview() : accUpdateRequest.getPrevReview());
    }

    public static ServiceResponse mapAccreditationResponse(List<Accreditation> accreditations){
        AccreditationResponse accreditationResponse = new AccreditationResponse();
        List<AccreditationInfo> accreditationInfos = new ArrayList<>();

        for (Accreditation accreditation : accreditations) {
            accreditationInfos.add(prepareAccreditationInfo(accreditation));
        }
        accreditationResponse.setResponseCode(ResponseCode.SUCCESS.getRespCode());
        accreditationResponse.setResponseDesc(ResponseCode.SUCCESS.getRespDesc());
        accreditationResponse.setAccreditationInfos(accreditationInfos);


        return accreditationResponse;
    }

    private static AccreditationInfo prepareAccreditationInfo(Accreditation accreditation){
        AccreditationInfo accreditationInfo = new AccreditationInfo();

        accreditationInfo.setUserName(accreditation.getAccUser().getUsername());
        accreditationInfo.setSectionKey(accreditation.getAccreditationSection().getId().toString());
        accreditationInfo.setFormSubmitData(accreditation.getFormSubmitData());
        accreditationInfo.setStatus(accreditation.getStatus());
        accreditationInfo.setFormIdentity(accreditation.getFormIdentity());
        accreditationInfo.setStartDate(accreditation.getStartDate());
        accreditationInfo.setEndDate(accreditation.getEndDate());
        accreditationInfo.setRequestKey(accreditation.getRequestKey());
        accreditationInfo.setUserUpdateFlag(accreditation.isUserUpdateFlag());
        accreditationInfo.setRatings(accreditation.getRating());
        accreditationInfo.setCurrentReview(accreditation.getCurrentReview());
        accreditationInfo.setPrevReview(accreditation.getPrevReview());

        return accreditationInfo;
    }

    public static boolean isInvalidReviewCreateRequest(ReviewCreateRequest reviewCreateRequest){
        return null == reviewCreateRequest
                || null == reviewCreateRequest.getUsername()
                || isNullOrEmptyCollection(reviewCreateRequest.getCompReviewCreateRequestList())
                || null == reviewCreateRequest.getSectionKey()
                || null == reviewCreateRequest.getStatus();
    }

    public static ComponentReview mapCompReviewCreateRequest(CompReviewCreateRequest compReviewCreateRequest, SectionReview sectionReview){
        ComponentReview componentReview = new ComponentReview();

        componentReview.setTitle(compReviewCreateRequest.getTitle());
        componentReview.setValue(compReviewCreateRequest.getValue());
        componentReview.setSubmitValue(compReviewCreateRequest.getSubmitValue());
        componentReview.setKey(compReviewCreateRequest.getKey());
        componentReview.setRating(compReviewCreateRequest.getRating());
        componentReview.setStatus(compReviewCreateRequest.getStatus());
        componentReview.setComments(compReviewCreateRequest.getComments());
        componentReview.setSectionReview(sectionReview);

        return componentReview;
    }

    public static SectionReview mapReviewCreateRequest(ReviewCreateRequest reviewCreateRequest, User sectionReviewer, Accreditation accreditation){
        SectionReview sectionReview = new SectionReview();

        sectionReview.setStatus(reviewCreateRequest.getStatus());
        sectionReview.setComments(reviewCreateRequest.getComments());
        sectionReview.setRating(reviewCreateRequest.getRating());
        sectionReview.setSectionReviewer(sectionReviewer);
        sectionReview.setAccreditation(accreditation);

        return sectionReview;
    }

    private static ComponentReviewInfo mapComponentReview(ComponentReview componentReview){
        ComponentReviewInfo componentReviewInfo = new ComponentReviewInfo();

        componentReviewInfo.setTitle(componentReview.getTitle());
        componentReviewInfo.setValue(componentReview.getValue());
        componentReviewInfo.setSubmitValue(componentReview.getSubmitValue());
        componentReviewInfo.setKey(componentReview.getKey());
        componentReviewInfo.setRating(componentReview.getRating());
        componentReviewInfo.setStatus(componentReview.getStatus());
        componentReviewInfo.setComments(componentReview.getComments());

        return componentReviewInfo;
    }

    private static SectionReviewInfo mapSectionReview(SectionReview sectionReview){
        SectionReviewInfo sectionReviewInfo = new SectionReviewInfo();
        List<ComponentReviewInfo> componentReviewInfos = new ArrayList<>();

        //sectionReviewInfo.setUsername(sectionReview.getAccreditation().getAccreditationSection().getUsername());
        sectionReviewInfo.setSectionKey(sectionReview.getAccreditation().getAccreditationSection().getId().toString());
        sectionReviewInfo.setStatus(sectionReview.getStatus());
        sectionReviewInfo.setComments(sectionReview.getComments());
        sectionReviewInfo.setRating(sectionReview.getRating());
        sectionReviewInfo.setReviewer(null!= sectionReview.getSectionReviewer() ? sectionReview.getSectionReviewer().getUsername() : null);

        for (ComponentReview componentReview : sectionReview.getComponentReviewSet()) {
            ComponentReviewInfo componentReviewInfo = CommonUtils.mapComponentReview(componentReview);
            componentReviewInfos.add(componentReviewInfo);
        }
        sectionReviewInfo.setComponentReviewInfos(componentReviewInfos);

        return sectionReviewInfo;
    }

    public static SectionReviewResponse mapSectionReviews(List<SectionReview> sectionReviews){
        SectionReviewResponse sectionReviewResponse = new SectionReviewResponse();
        List<SectionReviewInfo> sectionReviewInfos = new ArrayList<>();

        sectionReviewResponse.setResponseCode(ResponseCode.SUCCESS.getRespCode());
        sectionReviewResponse.setResponseDesc(ResponseCode.SUCCESS.getRespDesc());
        for (SectionReview sectionReview : sectionReviews) {
            sectionReviewInfos.add(mapSectionReview(sectionReview));
        }
        sectionReviewResponse.setSectionReviewInfos(sectionReviewInfos);

        return  sectionReviewResponse;
    }

    public static boolean isInvalidAccredSectionReviewRequest(AccredSectionReviewRequest accredSectionReviewRequest){
        return null == accredSectionReviewRequest
                || null == accredSectionReviewRequest.getUsername()
                || null == accredSectionReviewRequest.getSectionKey();
    }

    public static SectionReview getLatestSectionReview(Set<SectionReview> sectionReviewSet){
        SectionReview latestSectionReview = null;

        for (SectionReview sectionReview : sectionReviewSet) {
            if(null == latestSectionReview) {
                latestSectionReview = sectionReview;
            }
            if (latestSectionReview.getId() < sectionReview.getId()){
                latestSectionReview = sectionReview;
            }
        }

        return latestSectionReview;
    }
}
