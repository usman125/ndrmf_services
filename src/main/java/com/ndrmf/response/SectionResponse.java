package com.ndrmf.response;

import java.io.Serializable;
import java.util.List;

import com.ndrmf.setting.model.Section;

public class SectionResponse extends ServiceResponse  implements Serializable {
    private List<SectionInfo> sectionInfos;

    public List<SectionInfo> getSectionInfos() {
        return sectionInfos;
    }

    public void setSectionInfos(List<SectionInfo> sectionInfos) {
        this.sectionInfos = sectionInfos;
    }
}
