package com.ndrmf.engine.dto;

import com.ndrmf.setting.dto.ThematicAreaItem;
import com.ndrmf.user.dto.UserLookupItem;

public class FipThematicAreasListItem {
    private int id;
    private UserLookupItem fip;
    private ThematicAreaItem thematicAreaItem;
    private Integer experience;
    private String counterpart;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserLookupItem getFip() {
        return fip;
    }

    public void setFip(UserLookupItem fip) {
        this.fip = fip;
    }

    public ThematicAreaItem getThematicAreaItem() {
        return thematicAreaItem;
    }

    public void setThematicAreaItem(ThematicAreaItem thematicAreaItem) {
        this.thematicAreaItem = thematicAreaItem;
    }

    public String getCounterpart() {
        return counterpart;
    }

    public void setCounterpart(String counterpart) {
        this.counterpart = counterpart;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

}
