package kr.co.sunnyvale.sunny.domain.dto;

import java.util.Date;

public class NotifyInfoDTO {

	private Date updateDate;
	
	private int unreadCount;

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public int getUnreadCount() {
		return unreadCount;
	}

	public void setUnreadCount(int unreadCount) {
		this.unreadCount = unreadCount;
	}
	
	
	
}
