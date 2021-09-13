package com.ndrmf.engine.dto;
import com.ndrmf.user.dto.UserItem;

import java.util.List;

public class EligPlusQual {
	private QualificationItem qualItem;
	private EligibilityItem eligItem;
	private List<FipThematicAreasListItem> thematicAreasListItems;
	private UserItem userInfo;
	
	public QualificationItem getQualItem() {
		return qualItem;
	}
	public void setQualItem(QualificationItem qualItem) {
		this.qualItem = qualItem;
	}
//	public List<EligibilityItem> getEligItem() {
//		return eligItem;
//	}
//	public void setEligItem(List<EligibilityItem> eligItem) {
//		this.eligItem = eligItem;
//	}


	public EligibilityItem getEligItem() {
		return eligItem;
	}

	public void setEligItem(EligibilityItem eligItem) {
		this.eligItem = eligItem;
	}

	public List<FipThematicAreasListItem> getThematicAreasListItems() {
		return thematicAreasListItems;
	}

	public void setThematicAreasListItems(List<FipThematicAreasListItem> thematicAreasListItems) {
		this.thematicAreasListItems = thematicAreasListItems;
	}

	public UserItem getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserItem userInfo) {
		this.userInfo = userInfo;
	}
}
