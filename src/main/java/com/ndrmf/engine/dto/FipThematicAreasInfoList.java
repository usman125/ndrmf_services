package com.ndrmf.engine.dto;
import java.util.UUID;

public class FipThematicAreasInfoList {
    private UUID areaId;
    private int experience;
    private String counterpart;


    public UUID getAreaId() {
        return areaId;
    }

    public void setAreaId(UUID areaId) {
        this.areaId = areaId;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public String getCounterpart() {
        return counterpart;
    }

    public void setCounterpart(String counterpart) {
        this.counterpart = counterpart;
    }
}
