package kr.co.sunnyvale.sunny.domain.dto;

import java.util.List;

import kr.co.sunnyvale.sunny.domain.Menu;
import kr.co.sunnyvale.sunny.domain.SmallGroup;
import kr.co.sunnyvale.sunny.domain.Tag;

public class CurrentInfo {

	Integer notificationCount;
	
	Integer friendRequestCount;
	
	Integer noticeCount;
	
	Integer messageCount;
	
	Integer departmentCount;
	
	Integer groupCount;
	
	Integer projectCount;
	
	Integer friendCount;
	
	List<SmallGroup> leftDepartments;
	
	List<SmallGroup> leftGroups;

	List<SmallGroup> leftProjects;
	
	List<Menu> leftMenus;
	
	List<Tag> topTags;
	

	public Integer getNotificationCount() {
		return notificationCount;
	}

	public void setNotificationCount(Integer notificationCount) {
		this.notificationCount = notificationCount;
	}

	public Integer getFriendRequestCount() {
		return friendRequestCount;
	}

	public void setFriendRequestCount(Integer friendRequestCount) {
		this.friendRequestCount = friendRequestCount;
	}

	public Integer getMessageCount() {
		return messageCount;
	}

	public void setMessageCount(Integer messageCount) {
		this.messageCount = messageCount;
	}

	public Integer getDepartmentCount() {
		return departmentCount;
	}

	public void setDepartmentCount(Integer departmentCount) {
		this.departmentCount = departmentCount;
	}

	public Integer getGroupCount() {
		return groupCount;
	}

	public void setGroupCount(Integer groupCount) {
		this.groupCount = groupCount;
	}

	public Integer getProjectCount() {
		return projectCount;
	}

	public void setProjectCount(Integer projectCount) {
		this.projectCount = projectCount;
	}

	public Integer getFriendCount() {
		return friendCount;
	}

	public void setFriendCount(Integer friendCount) {
		this.friendCount = friendCount;
	}

	public List<SmallGroup> getLeftDepartments() {
		return leftDepartments;
	}

	public void setLeftDepartments(List<SmallGroup> leftDepartments) {
		this.leftDepartments = leftDepartments;
	}

	public List<SmallGroup> getLeftGroups() {
		return leftGroups;
	}

	public void setLeftGroups(List<SmallGroup> leftGroups) {
		this.leftGroups = leftGroups;
	}

	public List<SmallGroup> getLeftProjects() {
		return leftProjects;
	}

	public void setLeftProjects(List<SmallGroup> leftProjects) {
		this.leftProjects = leftProjects;
	}

	public List<Menu> getLeftMenus() {
		return leftMenus;
	}

	public void setLeftMenus(List<Menu> leftMenus) {
		this.leftMenus = leftMenus;
	}

	public Integer getNoticeCount() {
		return noticeCount;
	}

	public void setNoticeCount(Integer noticeCount) {
		this.noticeCount = noticeCount;
	}

	public List<Tag> getTopTags() {
		return topTags;
	}

	public void setTopTags(List<Tag> topTags) {
		this.topTags = topTags;
	}

}