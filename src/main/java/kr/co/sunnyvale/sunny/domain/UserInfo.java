package kr.co.sunnyvale.sunny.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import kr.co.sunnyvale.sunny.domain.validator.constraints.ValidPhone;
import kr.co.sunnyvale.sunny.domain.validator.constraints.ValidSex;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table( name = "USER_INFO" )
public class UserInfo implements Serializable{


	public static final int PERMISSION_PUBLIC = 0;
	public static final int PERMISSION_FRIEND = 1;
	public static final int PERMISSION_PRIVATE = 2;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2275601421732135119L;

	/**
	 * 
	 */

	@Id
	@Column(name = "id", columnDefinition="bigint(20)" )
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	public UserInfo(){
	}

	
	public UserInfo(User user) {
		this.setUser(user);
	}


	// 성별
	@ValidSex
	@Column(name ="sex")
	private String sex; 
	
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	@JsonIgnore
	private User user;

	@Column(name = "friend_count", columnDefinition="integer default 0")
	private int friendCount;

	@Column(name = "department_count", columnDefinition="integer default 0")
	private int departmentCount;

	@Column(name = "group_count", columnDefinition="integer default 0")
	private int groupCount;

	@Column(name = "project_count", columnDefinition="integer default 0")
	private int projectCount;

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}


	public int getFriendCount() {
		return friendCount;
	}


	public void setFriendCount(int friendCount) {
		this.friendCount = friendCount;
	}


	public int getDepartmentCount() {
		return departmentCount;
	}


	public void setDepartmentCount(int departmentCount) {
		this.departmentCount = departmentCount;
	}


	public int getGroupCount() {
		return groupCount;
	}


	public void setGroupCount(int groupCount) {
		this.groupCount = groupCount;
	}


	public int getProjectCount() {
		return projectCount;
	}


	public void setProjectCount(int projectCount) {
		this.projectCount = projectCount;
	}
	
	
	
}