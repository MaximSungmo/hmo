package kr.co.sunnyvale.sunny.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import kr.co.sunnyvale.sunny.domain.extend.Stream;
import kr.co.sunnyvale.sunny.domain.validator.constraints.ValidEmail;
import kr.co.sunnyvale.sunny.domain.validator.constraints.ValidName;
import kr.co.sunnyvale.sunny.domain.validator.constraints.ValidPassword;
import kr.co.sunnyvale.sunny.util.StringUtils;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table( name = "USER" )
public class User implements Serializable{

	public final static String PREFIX_NUMBER_USERID = "1";
	public final static String SERVICE_USERID = "userId";
	public final static String DEFAULT_PROFILE_PIC = "/assets/sunny/2.0/img/default-profile-80x80.png";
	public final static String DEFAULT_COVER_PIC = "/assets/desktop/img/icon/default-profile-80x80.png";
	
	public final static int RELATION_FRIEND_REQUEST_NONE = 0;
	public final static int RELATION_FRIEND_REQUEST_FROM_ME = 1;
	public final static int RELATION_FRIEND_REQUEST_TO_ME = 2;
	public final static int RELATION_FRIEND_REQUEST_FRIEND = 3;
	
	public final static int STATUS_WORKING = 0;
	public final static int STATUS_VACATION = 1;
	public final static int STATUS_LEAVE = 2;
	
	public static final Long SUPER_USER = 0L;
	
	public User(){
		this.setCreateDate(new Date());
		this.setUpdateDate(new Date());
		this.setDeleteFlag(false);
		this.setSalt(StringUtils.genRandomString( 10 ));
	}

	public User(Long id) {
		this.id = id;
	}

	/**
	 *  기본정보
	 */
	@Id
	@Column(name = "id", columnDefinition="bigint(20)" )
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ValidEmail
	@Column( name="email")
	protected String email;


	@Column( name="job_title_1")
	private String jobTitle1;
	
	@Column( name="job_title_2")
	private String jobTitle2;
	
	@Column( name="job_title_3")
	private String jobTitle3;

	
	@ValidName
	@Column( name="name", length=40 )
	protected String name;

//	@ValidURLName
//	@Column( name="sunny_url", length=50 )	
//	protected String sunnyUrl;

	@ValidPassword
	@Column( name="password", length=64 )
	protected String password;
	
	@Transient
	private String passwordConfirm;

	
	@Column( name="salt", length=64 )
	@JsonIgnore
	protected String salt;
	
	
	@ManyToMany(targetEntity = Role.class, cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY)
	@JoinTable(name = "USER_ROLE", joinColumns = { 
			@JoinColumn(name = "user_id", nullable = false, updatable = false) }, 
			inverseJoinColumns = { @JoinColumn(name = "role_id", nullable = false, updatable = false) })
	@OrderBy("name ASC")
	@JsonIgnore
	private List<Role> roles;
//	
	

	@Column(name = "is_admin", columnDefinition="boolean default false")
	private boolean isAdmin;
	
		
	
	/*
	 * SiteUser 로 옮겨져야할 부분
	 * 
	 */
//	@Column( name="job_title_1")
//	private String jobTitle1;
//	
//	@Column( name="job_title_2")
//	private String jobTitle2;
//	
//	@Column( name="job_title_3")
//	private String jobTitle3;
//	

	@Column( name="innercall" )
	private String innercall;

	@Column( name="status_message")
	private String statusMessage;
	
	@Column(name = "status", columnDefinition="integer default 0")
	private int status;
	
	@Column(name = "admin_comment")
	private String adminComment;
	/*
	 * SiteUser 로 옮겨져야할 부분 ENd
	 */
	
	@Column( name="phone")
	private String phone;
	
	@Column(name = "profile_pic")
	protected String profilePic;
	
	@Column(name = "create_date")
	private Date createDate;
	
	@Column(name = "update_date")
	protected Date updateDate;
	
	@Column(name = "type", columnDefinition="integer default 0")
	private int type;

	
	@Column(name = "last_selected_permission", columnDefinition="integer default 0")
	private int lastSelectedPermission;

//	@Column(name = "description")
//	private String description;
	
	@Column(name = "agree_email", columnDefinition="boolean default false")
	private boolean agreeEmail;
	
	@Column(name = "delete_flag", columnDefinition="boolean default false")
	private boolean deleteFlag = false;
	
//	@ManyToMany(fetch = FetchType.LAZY)
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "USER_SMALL_GROUP_ACCESS", joinColumns = { 
	@JoinColumn(name = "user_id", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "small_group_id", nullable = false, updatable = false) })
	@BatchSize(size=10)
	@Where(clause = "delete_flag = 'false' and type = '3'")
	private List<SmallGroup> departments;
	
	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "USER_SMALL_GROUP_ACCESS", joinColumns = { 
			@JoinColumn(name = "user_id", nullable = false, updatable = false) }, 
			inverseJoinColumns = { @JoinColumn(name = "small_group_id", nullable = false, updatable = false) })
	@BatchSize(size=10)
	@Where(clause = "delete_flag = 'false'")
	private List<SmallGroup> smallGroups;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "USER_MESSAGE_INFO_OBJECTED", joinColumns = { 
			@JoinColumn(name = "user_id", nullable = false, updatable = false) }, 
			inverseJoinColumns = { @JoinColumn(name = "message_info_id", nullable = false, updatable = false) })
	@BatchSize(size=10)
	private List<MessageInfo> objectedMessageInfos;
	
	@OneToMany( mappedBy = "creator", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE})
	private List<SmallGroup> createdSmallGroups;

	@OneToMany( mappedBy = "user", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE})
	private List<FeelAndContentAndUser> feelAndContentAndUsers;

	
	@OneToMany( mappedBy = "user", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE})
	private List<ContentReadUser> contentReadUsers;

	@OneToMany( mappedBy = "user", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE})
	private List<ActivatorRelation> activatorRelations;

	
//	@OneToMany( mappedBy = "user", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE})
//	private List<SiteInactiveUser> siteInactiveUsers;

	
	@OneToMany( mappedBy = "user", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE})
	private List<LastRead> lastReads;

//	@OneToMany( mappedBy = "user", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE})
//	private List<SiteUser> siteUsers;
//	
//	
//	@OneToMany( mappedBy = "user", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE})
//	private List<Email> emails;
//	
	@OneToMany( mappedBy = "onlyFriendUser", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE})
	private List<SmallGroup> myOnlyFriendGroup;
	
	@JsonIgnore
	@OneToMany( mappedBy = "onlyMineUser", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE})
	private List<SmallGroup> myOnlySmallGroup;
	
	@OneToMany( mappedBy = "followed", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE})
	private List<Friend> followedRelation;
	
	@OneToMany( mappedBy = "follower", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE})
	private List<Friend> followerRelation;
	
	@OneToMany( mappedBy = "user", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE})
	protected List<ReceiverRelation> myReceiverRelations;
	
	@OneToMany( mappedBy = "targetUser", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE})
	protected List<ReceiverRelation> targetReceiverRelations;
	
	
	@OneToMany( mappedBy = "receiver", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE})
	protected List<Notification> receiveNotifications;
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}


	@JsonIgnore
	@OneToMany( mappedBy = "user", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE} )
	private List<Media> mediaes;
	

	@JsonIgnore
	@OneToOne(fetch = FetchType.LAZY)
	private Media profilePhoto;
	
	@OneToOne( mappedBy = "user", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE} )
	private UserInfo info;


//	@JsonIgnore
//	@OneToMany( mappedBy = "user", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE} )
//	private List<Admin> admins;
//	
	/**
	 *  Mapping 
	 */
	@JsonIgnore
	@OneToMany(mappedBy = "user", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE})
	@OrderBy("createDate DESC")
	protected List<AuthToken> authTokens;

	@JsonIgnore
	@OneToMany(mappedBy = "user", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE})
	protected List<Content> stories;
	

//	@JsonIgnore
//	@OneToMany(mappedBy = "user", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE})
//	protected List<ContentViewUser> contentViewUsers;
//	
//	@JsonIgnore
//	@OneToMany(mappedBy = "user", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE})
//	protected List<ActivatorRelation> activators;
//	

	
	
//	@JsonIgnore
//	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE})
//	protected List<EvaluateContentUser> evaluateContentUsers;

	@JsonIgnore
	@OneToMany(mappedBy = "postUser", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE})
	protected List<Content> postToMeStories;

	@JsonIgnore
	@ManyToMany(targetEntity = Channel.class, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE },mappedBy = "users", fetch = FetchType.LAZY)
	protected List<Channel> channels;
	
	@JsonIgnore
	@OneToMany(mappedBy = "user", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE})
	protected List<LoginInfo> loginLogos;
	
	@JsonIgnore
	@OneToMany(mappedBy = "favorited", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE})
	protected List<FavoriteUser> favoritedUsers;
	
	@JsonIgnore
	@OneToMany(mappedBy = "sender", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE})
	protected List<MessageInfo> sendMessages;
	
	@JsonIgnore
	@OneToMany(mappedBy = "subjectUser", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE})
	protected List<MessageInfo> subjectMessages;
//	
//	@JsonIgnore
//	@OneToMany(mappedBy = "objectUser", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE})
//	protected List<MessageInfo> objectMessages;
	@JsonIgnore
	@OneToMany(mappedBy = "user", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE})
	protected List<BookMark> bookMarks;
	
//	@OneToMany( mappedBy = "user", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE})
//	protected List<ReceiverRelation> myReceiverRelations;
//	
//	@OneToMany( mappedBy = "targetUser", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE})
//	protected List<ReceiverRelation> targetReceiverRelations;


	@JsonIgnore
//	@OneToMany( mappedBy = "user", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE} )
//	private List<UserSmallGroupAccess> userSmallGroupAccessList;
//	
	@OneToMany( mappedBy = "user", fetch=FetchType.LAZY )
	private List<UserSmallGroupAccess> userSmallGroupAccessList;
	
	@OneToMany( mappedBy = "user", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE})
	private List<SmallGroupInactiveUser> smallGroupInactiveUsers;

	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "site_id")
	@NotFound(action = NotFoundAction.IGNORE)
	private Site site;

	@JsonIgnore
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "friend_small_group_id")
	private SmallGroup friendSmallGroup;
	

	@JsonIgnore
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "my_small_group_id")
	private SmallGroup mySmallGroup;
	
	/**
	 * 상대방과 내가 무슨 관계인지 보여줌
	 * 0 - 친구 아님, 친구요청 서로 안보냄
	 * 1 - 친구 아님, 내가 친구요청 보냄
	 * 2 - 친구 아님, 상대방이 친구요청 받음
	 * 3 - 친구임
	 * @return
	 */
	@Transient
	private int relationFriendRequest;
	
	@Transient
	private Date lastLogin;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}



//	public String getSunnyUrl() {
//		return sunnyUrl;
//	}
//
//	public void setSunnyUrl(String sunnyUrl) {
//		this.sunnyUrl = sunnyUrl;
//	}

	@JsonIgnore
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

//	public String getEmail() {
//		return email;
//	}
//
//	public void setEmail(String email) {
//		this.email = email;
//	}

//	public String getSecondEmail() {
//		return secondEmail;
//	}
//
//	public void setSecondEmail(String secondEmail) {
//		this.secondEmail = secondEmail;
//	}


	public List<AuthToken> getAuthTokens() {
		return authTokens;
	}

	public void setAuthTokens(List<AuthToken> authTokens) {
		this.authTokens = authTokens;
	}


	public String getProfilePic() {
		if( this.profilePic == null )
			return this.DEFAULT_PROFILE_PIC;
		
		return this.profilePic;
	}

	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}
//
//	@JsonIgnore
//	public List<User> getFeeders() {
//		return feeders;
//	}
//	@JsonIgnore
//	public void setFeeders(List<User> feeders) {
//		this.feeders = feeders;
//	}
//	@JsonIgnore
//	public List<User> getReaders() {
//		return readers;
//	}
//	@JsonIgnore
//	public void setReaders(List<User> readers) {
//		this.readers = readers;
//	}


	public static Stream getDefaultPage(){
		Stream page = new Stream();
		
		return page;
	}

	@JsonIgnore
	public List<Role> getRoles() {
		return roles;
	}
	@JsonIgnore
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public void addRole(Role role) {
		if( this.roles == null ){
			this.roles = new ArrayList<Role>();
		}
		this.roles.add(role);
	}
	
	public void addSmallGroup(SmallGroup smallGroup){
		if(this.smallGroups == null){
			this.smallGroups = new ArrayList<SmallGroup>();
		}
		this.smallGroups.add(smallGroup);
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public List<Content> getPostToMeStories() {
		return postToMeStories;
	}

	public void setPostToMeStories(List<Content> postToMeStories) {
		this.postToMeStories = postToMeStories;
	}

	public List<Channel> getChannels() {
		return channels;
	}

	public void setChannels(List<Channel> channels) {
		this.channels = channels;
	}

	public Media getProfilePhoto() {
		return profilePhoto;
	}

	public void setProfilePhoto(Media profilePhoto) {
		this.profilePhoto = profilePhoto;
	}

	public UserInfo getInfo() {
		return info;
	}

	public void setInfo(UserInfo info) {
		this.info = info;
	}


//	public Site getSite() {
//		return site;
//	}
//
//	public void setSite(Site site) {
//		this.site = site;
//	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPasswordConfirm() {
		return passwordConfirm;
	}

	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}

	public List<Media> getMediaes() {
		return mediaes;
	}

	public void setMediaes(List<Media> mediaes) {
		this.mediaes = mediaes;
	}

//	public Admin getAdmin() {
//		return admin;
//	}
//
//	public void setAdmin(Admin admin) {
//		this.admin = admin;
//	}

	public List<Content> getStories() {
		return stories;
	}

	public void setStories(List<Content> stories) {
		this.stories = stories;
	}

	public List<LoginInfo> getLoginLogos() {
		return loginLogos;
	}

	public void setLoginLogos(List<LoginInfo> loginLogos) {
		this.loginLogos = loginLogos;
	}


	public List<SmallGroup> getSmallGroups() {
		return smallGroups;
	}

	public void setSmallGroups(List<SmallGroup> smallGroups) {
		this.smallGroups = smallGroups;
	}

	public boolean isAgreeEmail() {
		return agreeEmail;
	}

	public void setAgreeEmail(boolean agreeEmail) {
		this.agreeEmail = agreeEmail;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public int getLastSelectedPermission() {
		return lastSelectedPermission;
	}

	public void setLastSelectedPermission(int lastSelectedPermission) {
		this.lastSelectedPermission = lastSelectedPermission;
	}

//	public String getAdminComment() {
//		return adminComment;
//	}
//
//	public void setAdminComment(String adminComment) {
//		this.adminComment = adminComment;
//	}
//
//	public String getJobTitle1() {
//		return jobTitle1;
//	}
//
//	public void setJobTitle1(String jobTitle1) {
//		this.jobTitle1 = jobTitle1;
//	}
//
//	public String getJobTitle2() {
//		return jobTitle2;
//	}
//
//	public void setJobTitle2(String jobTitle2) {
//		this.jobTitle2 = jobTitle2;
//	}
//
//	public String getJobTitle3() {
//		return jobTitle3;
//	}
//
//	public void setJobTitle3(String jobTitle3) {
//		this.jobTitle3 = jobTitle3;
//	}
//
	public List<SmallGroup> getDepartments() {
		return departments;
	}

	public void setDepartments(List<SmallGroup> departments) {
		this.departments = departments;
	}

//	public String getDescription() {
//		return description;
//	}
//
//	public void setDescription(String description) {
//		this.description = description;
//	}

//	public int getStatus() {
//		return status;
//	}
//
//	public void setStatus(int status) {
//		this.status = status;
//	}
//	

	public List<UserSmallGroupAccess> getUserSmallGroupAccessList() {
		return userSmallGroupAccessList;
	}

	public void setUserSmallGroupAccessList(
			List<UserSmallGroupAccess> userSmallGroupAccessList) {
		this.userSmallGroupAccessList = userSmallGroupAccessList;
	}
	
	

	public boolean isDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(boolean deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
	
	

	public int getRelationFriendRequest() {
		return relationFriendRequest;
	}

	public void setRelationFriendRequest(int relationFriendRequest) {
		this.relationFriendRequest = relationFriendRequest;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}
//
//	public String getInnercall() {
//		return innercall;
//	}
//
//	public void setInnercall(String innercall) {
//		this.innercall = innercall;
//	}

	

	public List<FavoriteUser> getFavoritedUsers() {
		return favoritedUsers;
	}

	public void setFavoritedUsers(List<FavoriteUser> favoritedUsers) {
		this.favoritedUsers = favoritedUsers;
	}

	@JsonIgnore
	public String getJobTitlesString(){
		String retString = this.getJobTitle1();
		if( this.jobTitle2 != null && !this.jobTitle2.isEmpty() ){
			retString += ", " + this.getJobTitle2();
		}
		
		if( this.jobTitle3 != null && !this.jobTitle3.isEmpty() ){
			retString += ", " + this.getJobTitle3();
		}
		
		return retString;
	}
	
	
	public List<BookMark> getBookMarks() {
		return bookMarks;
	}

	public void setBookMarks(List<BookMark> bookMarks) {
		this.bookMarks = bookMarks;
	}
	
	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}


//	public List<Admin> getAdmins() {
//		return admins;
//	}
//
//	public void setAdmins(List<Admin> admins) {
//		this.admins = admins;
//	}

	public List<SmallGroup> getMyOnlySmallGroup() {
		return myOnlySmallGroup;
	}

	public void setMyOnlySmallGroup(List<SmallGroup> myOnlySmallGroup) {
		this.myOnlySmallGroup = myOnlySmallGroup;
	}

	public SmallGroup getFriendSmallGroup() {
		return friendSmallGroup;
	}

	public void setFriendSmallGroup(SmallGroup friendSmallGroup) {
		this.friendSmallGroup = friendSmallGroup;
	}

	public SmallGroup getMySmallGroup() {
		return mySmallGroup;
	}

	public void setMySmallGroup(SmallGroup mySmallGroup) {
		this.mySmallGroup = mySmallGroup;
	}

	public String getJobTitle1() {
		return jobTitle1;
	}

	public void setJobTitle1(String jobTitle1) {
		this.jobTitle1 = jobTitle1;
	}

	public String getJobTitle2() {
		return jobTitle2;
	}

	public void setJobTitle2(String jobTitle2) {
		this.jobTitle2 = jobTitle2;
	}

	public String getJobTitle3() {
		return jobTitle3;
	}

	public void setJobTitle3(String jobTitle3) {
		this.jobTitle3 = jobTitle3;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public String getInnercall() {
		return innercall;
	}

	public void setInnercall(String innercall) {
		this.innercall = innercall;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getAdminComment() {
		return adminComment;
	}

	public void setAdminComment(String adminComment) {
		this.adminComment = adminComment;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}
	

}