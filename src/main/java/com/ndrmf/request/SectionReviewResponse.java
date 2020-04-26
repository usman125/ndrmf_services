package com.ndrmf.request;

import java.util.List;

import com.ndrmf.response.ServiceResponse;

public class SectionReviewResponse extends ServiceResponse {

    private List<SectionReviewInfo> sectionReviewInfos;

    public List<SectionReviewInfo> getSectionReviewInfos() {
        return sectionReviewInfos;
    }

    public void setSectionReviewInfos(List<SectionReviewInfo> sectionReviewInfos) {
        this.sectionReviewInfos = sectionReviewInfos;
    }
}
