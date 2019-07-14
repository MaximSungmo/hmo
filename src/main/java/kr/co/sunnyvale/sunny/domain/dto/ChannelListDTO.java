package kr.co.sunnyvale.sunny.domain.dto;

import java.util.Date;
import java.util.List;

import kr.co.sunnyvale.sunny.domain.User;

public class ChannelListDTO {

	private long id;

	private int type;

	private String lastTextSnippet;

	private String lastUserProfilePic;

	private String lastUserName;

	private Date updateDate;
	
    private int userCount; 

	private String link;

	private List<User> tmpJoinedUsers;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}


	public String getLastUserProfilePic() {
		return lastUserProfilePic;
	}

	public void setLastUserProfilePic(String lastUserProfilePic) {
		this.lastUserProfilePic = lastUserProfilePic;
	}

	public String getLastUserName() {
		return lastUserName;
	}

	public void setLastUserName(String lastUserName) {
		this.lastUserName = lastUserName;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getLastTextSnippet() {
		return lastTextSnippet;
	}

	public void setLastTextSnippet(String lastTextSnippet) {
		this.lastTextSnippet = lastTextSnippet;
	}

	public int getUserCount() {
		return userCount;
	}

	public void setUserCount(int userCount) {
		this.userCount = userCount;
	}

	public List<User> getTmpJoinedUsers() {
		return tmpJoinedUsers;
	}

	public void setTmpJoinedUsers(List<User> tmpJoinedUsers) {
		this.tmpJoinedUsers = tmpJoinedUsers;
	}

}
