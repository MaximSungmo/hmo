package kr.co.sunnyvale.sunny.domain;

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
import javax.persistence.Table;
import javax.persistence.Transient;

import kr.co.sunnyvale.sunny.domain.extend.SmallGroupIdName;

import org.hibernate.annotations.BatchSize;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "SMALL_GROUP")
public class SmallGroup {
	
	public static final String DELIMITER = "-";
	
//	public static int PRIVACY_PUBLIC=0;  // 전체공개
//	public static int PRIVACY_CLOSED=1;  // 비밀그룹
//	public static int PRIVACY_SECRET=2;  // 완전 비밀
	
//	public static int JOIN_POWER_ANYONE=0;  // 누구나 가입할 수 있음
//	public static int JOIN_POWER_ADMIN=1;  // 관리자에게 가입 승인을 받아야함
	
	public static final long SYSTEM_GROUP = 0;
	
	public static final int TYPE_ME=0;
	public static final int TYPE_LOBBY=1;
	public static final int TYPE_FRIEND=2;
	public static final int TYPE_DEPARTMENT=3; // 부서 부서는 자식 부서를 가질 수 있다.
	public static final int TYPE_PROJECT=4; // 과제, 프로젝트
	public static final int TYPE_GROUP=5;
	public static final int TYPE_USER_DEFINE_GROUP=6;
	
	
	@Id
	@Column(name = "id", columnDefinition="bigint(20)" )
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	public SmallGroup(){
		this.setDepth(0);
		this.setOrdering(0);
		this.setThreadSeq(0);
		this.setParent(null);
		this.setParentSmallGroupId(null);
		
		
		this.setCreateDate(new Date());
		this.setUpdateDate(new Date());
		this.setDeleteFlag(false);
	}

	public SmallGroup(Long id) {
		this();
		this.setId(id);
	}

	public SmallGroup(String title) {
		this();
		this.setTitle( title );
	}

	@Column(name = "name")
	private String name;

	@Column(name = "description")
	private String description;
	
	@Column(name = "type")
	private int type;
	
	@JsonIgnore
	@OneToOne(fetch = FetchType.LAZY)
	private Media coverPhoto;
	
	@OneToMany( mappedBy = "smallGroup", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE})
	private List<Media> photos;
	
	
	
	@OneToMany( mappedBy = "smallGroup", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE})
	private List<SmallGroupInactiveUser> inactiveUsers;
	
	@OneToMany( mappedBy = "smallGroup", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE})
	private List<Draft> drafts;
	
	@OneToMany( mappedBy = "smallGroup", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE})
	private List<Content> stories;

	@OneToMany( mappedBy = "smallGroup", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE})
	private List<InactiveUserAndSmallGroup> inactiveUser;
	
	@ManyToOne
	@JoinColumn(name = "parent_id")
	private SmallGroup parent;
	
	@OneToMany(fetch = FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE})
	@JoinColumn(name = "parent_id")
	//@BatchSize(size=10)
	private List<SmallGroup> children;
	
	@Transient
	private List<SmallGroup> transientChildren;
	

	@Column(name = "children_department_count")
	private int childrenDepartmentCount;
	
	@Column(name = "children_project_count")
	private int childrenProjectCount;
	
	/**
	 * 가문. 조상 번호만을 가진다.
	 */
	@Column(name = "thread")
	private long thread;
	
	@Column(name = "depth")
	private int depth;
	
	@Column(name = "thread_seq")
	private int threadSeq;
	
	// 삭제여부 deleteFlag 가 true라면 삭제처리된 자료.
	@Column(name = "delete_flag")
	private boolean deleteFlag;
	
	@Column(name = "parent_small_group_id")
	private Long parentSmallGroupId;
	
	@Column(name = "absolute_path")
	private String absolutePath;
	
	
	
	/**
	 * 같은 Depth 내에서의 위치
	 */
	@Column(name = "ordering")
	private int ordering; 
	
	@Column(name = "cover_pic")
	private String coverPic;
	
	@Column(name = "cover_pic_thumb")
	private String coverPicThumb;
	
	@Column(name = "url_name")
	private String urlName;
	
	@Column(name = "user_count", columnDefinition="integer default 0" )
	private Integer userCount;

	@Column(name = "inactive_user_count", columnDefinition="integer default 0" )
	private Integer inactiveUserCount;
	
//	@Column(name = "privacy", columnDefinition="integer default 0" )
//	private int privacy;
	
//	@Column(name = "join_power", columnDefinition="integer default 0")
//	private int joinPower;
	
	@Column(name = "status", columnDefinition="integer default 0")
	private int status;

	@Column(name = "create_date")
	private Date createDate;

	@Column(name = "update_date")
	protected Date updateDate;
	
	@Column(name = "event_count", columnDefinition="integer default 0" )
	private int eventCount;
	
	@Column(name = "note_count", columnDefinition="integer default 0" )
	private int noteCount;
	
	@Column(name = "approval_count", columnDefinition="integer default 0" )
	private int approvalCount;
	
	@Transient
	private int unreadCount;
	
	@Transient
	private boolean isJoined;
	
	@OneToMany( mappedBy = "smallGroup", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE})
	private List<Tag> tags;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "USER_SMALL_GROUP_ACCESS", joinColumns = { 
			@JoinColumn(name = "small_group_id", nullable = false, updatable = false) }, 
			inverseJoinColumns = { @JoinColumn(name = "user_id", nullable = false, updatable = false) })
	@BatchSize(size=10)
	private List<User> users;

	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "SMALL_GROUP_CONTENT_ACCESS", joinColumns = { 
			@JoinColumn(name = "small_group_id", nullable = false, updatable = false) }, 
			inverseJoinColumns = { @JoinColumn(name = "content_id", nullable = false, updatable = false) })
	@BatchSize(size=10)
	private List<Content> contents;

	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "SMALL_GROUP_SMALL_GROUP_ACCESS", joinColumns = { 
			@JoinColumn(name = "small_group_id", nullable = false, updatable = false) }, 
			inverseJoinColumns = { @JoinColumn(name = "access_small_group_id", nullable = false, updatable = false) })
	@BatchSize(size=10)
	private List<SmallGroup> accessSmallGroups;

	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "SMALL_GROUP_SMALL_GROUP_ACCESS", joinColumns = { 
			@JoinColumn(name = "access_small_group_id", nullable = false, updatable = false) }, 
			inverseJoinColumns = { @JoinColumn(name = "small_group_id", nullable = false, updatable = false) })
	@BatchSize(size=10)
	private List<SmallGroup> targetSmallGroups;
	
//	@JsonIgnore
//	@OneToMany( mappedBy = "smallGroup", fetch = FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE})
//	private List<UserSmallGroupAccess> smallGroupUsers; 
	
	@JsonIgnore
	@OneToMany( mappedBy = "smallGroup", fetch = FetchType.LAZY)
	private List<UserSmallGroupAccess> smallGroupUsers; 
	
	
//	@OneToMany( mappedBy = "smallGroup", fetch = FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE})
//	private List<SmallGroupSmallGroupAccess> smallGroupSmallGroupAccesses;
//	
	@OneToMany( mappedBy = "smallGroup", fetch = FetchType.LAZY)
	private List<SmallGroupSmallGroupAccess> smallGroupSmallGroupAccesses;
	
	
	@OneToMany( mappedBy = "smallGroup", fetch = FetchType.LAZY)
	private List<SmallGroupContentAccess> smallGroupContentAccesses;
	
	@OneToMany( mappedBy = "smallGroup", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE})
	private List<AccessIp> accessIp;
	
	@JsonIgnore
	@ManyToOne( fetch = FetchType.LAZY )
	@JoinColumn(name = "creator_id")
	private User creator;
	
	@OneToMany( mappedBy = "friendSmallGroup", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE})
	private List<User> myFriendUsers;
	
	@OneToMany( mappedBy = "smallGroup", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE})
	private List<SmallGroupApproval> smallGroupApprovals;
	

	@OneToMany( mappedBy = "currentSmallGroup", fetch=FetchType.LAZY, cascade={CascadeType.REMOVE})
	private List<Approval> currentApprovals;
	
	@ManyToOne( fetch = FetchType.LAZY )
	@JoinColumn(name = "only_friend_user_id")
	private User onlyFriendUser;
	
	@ManyToOne( fetch = FetchType.EAGER )
	@JoinColumn(name = "only_mine_user_id")
	private User onlyMineUser;
	
	
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "site_id")
	private Site site;
	
	
	@Transient
	private List<SmallGroupIdName> smallGroupPathIdNames;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public void addAccessSmallGroup(SmallGroup smallGroup){
		if(accessSmallGroups == null){
			accessSmallGroups = new ArrayList<SmallGroup>();
		}
		accessSmallGroups.add(smallGroup);
	}

	public Integer getUserCount() {
		if( this.userCount == null ){
			this.userCount = new Integer(0);
		}
		return userCount;
	}

	public void setUserCount(Integer userCount) {
		this.userCount = userCount;
	}

	public Integer getInactiveUserCount() {
		if( this.inactiveUserCount == null ){
			this.inactiveUserCount = new Integer(0);
		}
		return inactiveUserCount;
	}

	public void setInactiveUserCount(Integer inactiveUserCount) {
		this.inactiveUserCount = inactiveUserCount;
	}

	public String getName() {
		if( name == null && this.onlyMineUser != null ){
			return this.onlyMineUser.getName();
		}
		return name;
	}

	public void setTitle(String name) {
		this.name = name;
	}


	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getUrlName() {
		return urlName;
	}

	public void setUrlName(String urlName) {
		this.urlName = urlName;
	}
//
//	public int getPrivacy() {
//		return privacy;
//	}
//
//	public void setPrivacy(int privacy) {
//		this.privacy = privacy;
//	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

//	public List<Content> getStories() {
//		return stories;
//	}
//
//	public void setStories(List<Content> stories) {
//		this.stories = stories;
//	}

	public void addUser(User user) {
		if( this.users == null ){
			this.users = new ArrayList<User>();
		}
		this.users.add(user);
		this.addUserCount();
		
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void addUserCount() {
		if( this.userCount == null ){
			this.userCount = new Integer(1);
			return;
		}
		this.userCount = this.userCount + 1 ;
		
	}	
	public void addInactiveUserCount() {
		if( this.inactiveUserCount == null ){
			this.inactiveUserCount = new Integer(1);
			return;
		}
		this.inactiveUserCount = this.inactiveUserCount + 1 ;
	}

	public Media getCoverPhoto() {
		return coverPhoto;
	}

	public void setCoverPhoto(Media coverPhoto) {
		this.coverPhoto = coverPhoto;
	}

	public String getCoverPic() {
		return coverPic;
	}

	public void setCoverPic(String coverPic) {
		this.coverPic = coverPic;
	}

//	public int getJoinPower() {
//		return joinPower;
//	}
	
//	public String getJoinPowerText(){
//		if( this.joinPower == SmallGroup.JOIN_POWER_ADMIN){
//			return "관리자 승인이 있어야만 가입할 수 있음";
//		}else if( this.joinPower == SmallGroup.JOIN_POWER_ANYONE){
//			return "누구나 가입할 수 있음";
//		}
//		return "그룹";
//	}
//
//	public void setJoinPower(int joinPower) {
//		this.joinPower = joinPower;
//	}
//
//
//	public String getPrivacyText(){
//		
//		if( this.getPrivacy() == SmallGroup.PRIVACY_PUBLIC ){
//			return "공개";
//		}else if( this.getPrivacy() == SmallGroup.PRIVACY_CLOSED ){
//			return "비공개";
//		}else if( this.getPrivacy() == SmallGroup.PRIVACY_SECRET){
//			return "비밀";
//		}
//		return "그룹";
//		
//	}

	public int getUnreadCount() {
		return unreadCount;
	}

	public void setUnreadCount(int unreadCount) {
		this.unreadCount = unreadCount;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public void addEventCount(){
		this.eventCount++;
	}
	
	public void minusEventCount(){
		this.eventCount--;
	}
	
	public int getEventCount() {
		return eventCount;
	}

	public void setEventCount(int eventCount) {
		this.eventCount = eventCount;
	}

	public boolean isJoined() {
		return isJoined;
	}

	public void setJoined(boolean isJoined) {
		this.isJoined = isJoined;
	}


	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public List<Media> getPhotos() {
		return photos;
	}

	public void setPhotos(List<Media> photos) {
		this.photos = photos;
	}

	public List<Content> getStories() {
		return stories;
	}

	public void setStories(List<Content> stories) {
		this.stories = stories;
	}

	public List<InactiveUserAndSmallGroup> getInactiveUser() {
		return inactiveUser;
	}

	public void setInactiveUser(List<InactiveUserAndSmallGroup> inactiveUser) {
		this.inactiveUser = inactiveUser;
	}

	public SmallGroup getParent() {
		return parent;
	}

	public void setParent(SmallGroup parent) {
		this.parent = parent;
	}

	public List<SmallGroup> getChildren() {
		return children;
	}

	public void setChildren(List<SmallGroup> children) {
		this.children = children;
	}

	public long getThread() {
		return thread;
	}

	public void setThread(long thread) {
		this.thread = thread;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public Long getParentSmallGroupId() {
		return parentSmallGroupId;
	}

	public void setParentSmallGroupId(Long parentSmallGroupId) {
		this.parentSmallGroupId = parentSmallGroupId;
	}

	public String getAbsolutePath() {
		return absolutePath;
	}
	
	public void setAbsolutePath(String absolutePath, Long id) {
		this.absolutePath = (absolutePath == null ? "" : absolutePath) + DELIMITER + id + DELIMITER;
	}
	
	public void setWrapAbsolutePath(Long id) {
		if( id == null )
			return;
		this.absolutePath = DELIMITER + id + DELIMITER;
	}
	
	public int getOrdering() {
		return ordering;
	}

	public void setOrdering(int ordering) {
		this.ordering = ordering;
	}

	public List<UserSmallGroupAccess> getSmallGroupUsers() {
		return smallGroupUsers;
	}

	public void setSmallGroupUsers(List<UserSmallGroupAccess> smallGroupUsers) {
		this.smallGroupUsers = smallGroupUsers;
	}

	public List<AccessIp> getAccessIp() {
		return accessIp;
	}

	public void setAccessIp(List<AccessIp> accessIp) {
		this.accessIp = accessIp;
	}

	public boolean getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(boolean deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public int getThreadSeq() {
		return threadSeq;
	}

	public void setThreadSeq(int threadSeq) {
		this.threadSeq = threadSeq;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Content> getContents() {
		return contents;
	}

	public void setContents(List<Content> contents) {
		this.contents = contents;
	}

	public List<SmallGroup> getAccessSmallGroups() {
		return accessSmallGroups;
	}

	public void setAccessSmallGroups(List<SmallGroup> accessSmallGroups) {
		this.accessSmallGroups = accessSmallGroups;
	}

	public List<SmallGroup> getTargetSmallGroups() {
		return targetSmallGroups;
	}

	public void setTargetSmallGroups(List<SmallGroup> targetSmallGroups) {
		this.targetSmallGroups = targetSmallGroups;
	}

	public String getCoverPicThumb() {
		return coverPicThumb;
	}

	public void setCoverPicThumb(String coverPicThumb) {
		this.coverPicThumb = coverPicThumb;
	}

	public int getChildrenDepartmentCount() {
		return childrenDepartmentCount;
	}

	public void setChildrenDepartmentCount(int childrenDepartmentCount) {
		this.childrenDepartmentCount = childrenDepartmentCount;
	}

	public int getChildrenProjectCount() {
		return childrenProjectCount;
	}

	public void setChildrenProjectCount(int childrenProjectCount) {
		this.childrenProjectCount = childrenProjectCount;
	}

	public int getNoteCount() {
		return noteCount;
	}

	public void setNoteCount(int noteCount) {
		this.noteCount = noteCount;
	}

	public int getApprovalCount() {
		return approvalCount;
	}

	public void setApprovalCount(int approvalCount) {
		this.approvalCount = approvalCount;
	}

	public User getOnlyMineUser() {
		return onlyMineUser;
	}

	public void setOnlyMineUser(User onlyMineUser) {
		this.onlyMineUser = onlyMineUser;
	}

	public List<SmallGroupIdName> getSmallGroupPathIdNames() {
		return smallGroupPathIdNames;
	}

	public void setSmallGroupPathIdNames(
			List<SmallGroupIdName> smallGroupPathIdNames) {
		this.smallGroupPathIdNames = smallGroupPathIdNames;
	}

	public List<SmallGroup> getTransientChildren() {
		return transientChildren;
	}

	public void setTransientChildren(List<SmallGroup> transientChildren) {
		this.transientChildren = transientChildren;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}
	
}
