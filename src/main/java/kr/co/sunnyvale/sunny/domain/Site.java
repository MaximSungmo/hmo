package kr.co.sunnyvale.sunny.domain;

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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "SITE")
public class Site {


	public static int PRIVACY_PUBLIC=0;  // 전체공개. 검색에도 노출되어있고 게시글도 전부 다 보임
	public static int PRIVACY_CLOSED=1;  // 검색에는 노출되지만 들어올 수는 없음
	public static int PRIVACY_SECRET=2;  // 검색에 노출 안되고 들어올수도 없음
	
	public static int JOIN_POWER_ANYONE=0;  // 누구나 가입할 수 있음
	public static int JOIN_POWER_ADMIN=1;  // 관리자에게 가입 승인을 받아야함
	
	
	public Site(){
		this.createDate = new Date();
	}
	
	public Site(Long siteId) {
		this.id = siteId;
	}

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	// ex) 회사 이름
	@Column(name = "company_name")
	private String companyName;
	
	// ex) sunnyvale.co.kr 과 같은 도메인
	@Column(name = "company_domain")
	private String companyDomain;
	
	// ex) URL네임. sunnysns.com/meet42 에서 meet42 부분
	@Column(name = "path")
	private String path;
	
	// 국가/지역
	@Column(name = "contury_code")
	private String conturyCode;
	
	// 폰/번호
	@Column(name = "company_phone")
	private String companyPhone;
	
	@Column(name = "company_introduce")
	private String companyIntroduce;
	
	// 홈페이지
	@Column(name = "homepage")
	private String homepage;
	
	// 작은 크기 로고 
	@Column(name = "small_logo_src")
	private String smallLogoSrc;

	// 큰 크기 로고 
	@Column(name = "large_logo_src")
	private String largeLogoSrc;
	
	@Column(name = "employee_size", columnDefinition="integer default 0" )
	private int employeeSize;

	@Column(name = "company_region")
	private String companyRegion;
	
	@Column(name = "cdn")
	private String cdn;
	
	

	@Column(name = "upload_max_size" )
	private Long uploadMaxSize;

	@Column(name = "access_ip_pds")
	private String accessIpPds;
	
	@Column(name = "privacy", columnDefinition="integer default 1" )
	private int privacy = PRIVACY_CLOSED;
	
	@Column(name = "join_power", columnDefinition="integer default 0")
	private int joinPower;
	
	@Column(name = "notice_duration", columnDefinition="integer default 3")
	private int noticeDuration = 3;
	
	@JsonIgnore
	@OneToMany( mappedBy = "site", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE})
	private List<Domain> domains;
	
	@JsonIgnore
	@OneToMany( mappedBy = "site", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE})
	private List<Template> templates;
	
	@JsonIgnore
	@OneToMany( mappedBy = "site", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE})
	private List<AuthToken> authTokens;
	
	@JsonIgnore
	@OneToMany( mappedBy = "site", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE})
	private List<AccessIp> accessIp;
	
	
	
	@JsonIgnore
	@OneToMany( mappedBy = "site", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE})
	protected List<Content> stories;

	@JsonIgnore
	@OneToMany( mappedBy = "site", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE})
	protected List<SmallGroup> smallGroups;
	
	@JsonIgnore
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "lobby_small_group_id")
	private SmallGroup lobbySmallGroup;
	
	
	@JsonIgnore
	@OneToMany( mappedBy = "site", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE})
	protected List<Media> mediaes;
	
	@OneToMany( mappedBy = "site", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE})
	protected List<SiteMenu> siteMenus;
	
	@JsonIgnore
	@OneToMany( mappedBy = "site", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE})
	protected List<LoginInfo> loginInfos;
	

	@JsonIgnore
	@OneToMany( mappedBy = "site", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE})
	protected List<Tag> tags;

	@JsonIgnore
	@OneToMany( mappedBy = "site", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE})
	protected List<User> users;
	
	
	@JsonIgnore
	@OneToMany( mappedBy = "site", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE})
	protected List<SiteInactiveUser> siteInactiveUsers;

	@Column(name = "create_date")
	private Date createDate;

	@Column(name = "delete_flag", columnDefinition="boolean default false")
	private boolean deleteFlag;
	
	@Transient
	private List<Menu> leftMenus;
	
	@Transient
	private String currentDocumentDomain;
	
	@Transient
	private boolean isJoinedUser;

	@Transient
	private boolean isAlreadyRequestUser;

	@Transient
	private int storyCount;
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}


	public String getCompanyDomain() {
		return companyDomain;
	}

	public void setCompanyDomain(String companyDomain) {
		this.companyDomain = companyDomain;
	}

	public String getSmallLogoSrc() {
		return smallLogoSrc;
	}

	public void setSmallLogoSrc(String smallLogoSrc) {
		this.smallLogoSrc = smallLogoSrc;
	}

	public String getLargeLogoSrc() {
		return largeLogoSrc;
	}

	public void setLargeLogoSrc(String largeLogoSrc) {
		this.largeLogoSrc = largeLogoSrc;
	}

	public List<Domain> getDomains() {
		return domains;
	}

	public void setDomains(List<Domain> domains) {
		this.domains = domains;
	}

	public List<Content> getStories() {
		return stories;
	}

	public void setStories(List<Content> stories) {
		this.stories = stories;
	}

	public SmallGroup getLobbySmallGroup() {
		return lobbySmallGroup;
	}

	public void setLobbySmallGroup(SmallGroup lobbySmallGroup) {
		this.lobbySmallGroup = lobbySmallGroup;
	}

	public List<SmallGroup> getSmallGroups() {
		return smallGroups;
	}

	public void setSmallGroups(List<SmallGroup> smallGroups) {
		this.smallGroups = smallGroups;
	}

	public List<Media> getMediaes() {
		return mediaes;
	}

	public void setMediaes(List<Media> mediaes) {
		this.mediaes = mediaes;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

//	public List<User> getUsers() {
//		return users;
//	}
//
//	public void setUsers(List<User> users) {
//		this.users = users;
//	}

	
	
	public String getCurrentDocumentDomain() {
		return currentDocumentDomain;
	}

	public List<SiteInactiveUser> getSiteInactiveUsers() {
		return siteInactiveUsers;
	}

	public void setSiteInactiveUsers(List<SiteInactiveUser> siteInactiveUsers) {
		this.siteInactiveUsers = siteInactiveUsers;
	}

	public void setCurrentDocumentDomain(String currentDocumentDomain) {
		this.currentDocumentDomain = currentDocumentDomain;
	}


	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public List<AccessIp> getAccessIp() {
		return accessIp;
	}

	public void setAccessIp(List<AccessIp> accessIp) {
		this.accessIp = accessIp;
	}

	public String getConturyCode() {
		return conturyCode;
	}

	public void setConturyCode(String conturyCode) {
		this.conturyCode = conturyCode;
	}

	public String getCompanyPhone() {
		return companyPhone;
	}

	public void setCompanyPhone(String companyPhone) {
		this.companyPhone = companyPhone;
	}

	public int getEmployeeSize() {
		return employeeSize;
	}

	public void setEmployeeSize(int employeeSize) {
		this.employeeSize = employeeSize;
	}

	public String getCompanyRegion() {
		return companyRegion;
	}

	public void setCompanyRegion(String companyRegion) {
		this.companyRegion = companyRegion;
	}

	public String getPathOrId() {
		if( path != null )
			return path;
		
		return String.valueOf(id);
	}

	public String getCdn() {
		return cdn;
	}

	public void setCdn(String cdn) {
		this.cdn = cdn;
	}

	public Long getUploadMaxSize() {
		return uploadMaxSize;
	}

	public void setUploadMaxSize(Long uploadMaxSize) {
		this.uploadMaxSize = uploadMaxSize;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getHomepage() {
		return homepage;
	}

	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}

	public int getPrivacy() {
		return privacy;
	}

	public void setPrivacy(int privacy) {
		this.privacy = privacy;
	}

	public int getJoinPower() {
		return joinPower;
	}

	public void setJoinPower(int joinPower) {
		this.joinPower = joinPower;
	}

	public boolean isDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(boolean deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public boolean isJoinedUser() {
		return isJoinedUser;
	}

	public void setJoinedUser(boolean isJoinedUser) {
		this.isJoinedUser = isJoinedUser;
	}

	public boolean isAlreadyRequestUser() {
		return isAlreadyRequestUser;
	}

	public void setAlreadyRequestUser(boolean isAlreadyRequestUser) {
		this.isAlreadyRequestUser = isAlreadyRequestUser;
	}

	public String getAccessIpPds() {
		return accessIpPds;
	}

	public void setAccessIpPds(String accessIpPds) {
		this.accessIpPds = accessIpPds;
	}

	public int getNoticeDuration() {
		return noticeDuration;
	}

	public void setNoticeDuration(int noticeDuration) {
		this.noticeDuration = noticeDuration;
	}

	public String getCompanyIntroduce() {
		return companyIntroduce;
	}

	public void setCompanyIntroduce(String companyIntroduce) {
		this.companyIntroduce = companyIntroduce;
	}

	public int getStoryCount() {
		return storyCount;
	}

	public void setStoryCount(int storyCount) {
		this.storyCount = storyCount;
	}
	
}
