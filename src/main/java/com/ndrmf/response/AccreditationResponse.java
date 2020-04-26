package com.ndrmf.response;

import java.io.Serializable;
import java.util.List;

public class AccreditationResponse extends ServiceResponse implements Serializable {

    private List<AccreditationInfo> accreditationInfos;

    public List<AccreditationInfo> getAccreditationInfos() {
        return accreditationInfos;
    }

    public void setAccreditationInfos(List<AccreditationInfo> accreditationInfos) {
        this.accreditationInfos = accreditationInfos;
    }

}
