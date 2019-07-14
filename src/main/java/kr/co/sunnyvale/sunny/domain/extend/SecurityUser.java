package kr.co.sunnyvale.sunny.domain.extend;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import kr.co.sunnyvale.sunny.domain.Site;
import kr.co.sunnyvale.sunny.security.AbstractSecurityUser;

import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * {SpringSecurity}
 * 
 * <p>
 * {@link kr.co.sunnyvale.redwood.security.AbstractSecurityUser}의 확장 클래스
 * 커스텀 확장클래스를 통해 시큐리티 콘텍스트(세션)에 응용에 맞는 정보를 저장할 수 있다.
 * 야캠프에서는 basecampName과 userId가 추가 된 인증 사용자의 정보를 세션에  저장하게 된다.
 * 
 * <p>
 * Spring Security에서는 HttpSession의 직접적 접근은 바람직하지 않다.
 * Security Context로 추상화 되어 있기때문에 Security Context를 통해 해당사용자의 Principal 정보를
 * 받아 오면 된다.
 * kr.co.sunnyvale.redwood.security 패키지에서는 컴트롤에서 활용할 수 있는 @AuthUser 어노테이션을  제공한다.
 *  
 * @see
 * 
 * {@link kr.co.sunnyvale.sunny.annotation.redwood.security.annotation.AuthUser}
 *  
 * @author kickscar
 *
 */
public class SecurityUser extends AbstractSecurityUser implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2571799617109120047L;
	/**
	 * 
	 */
	private Long userId;
	@JsonIgnore
	private String ipAddress;
	@JsonIgnore
	private int type;
	private String email;
	private String profilePic;
	private String name;
	private int status;
	private boolean admin;
	private boolean superAdmin;
	private Long siteId;
	private String siteName;
	private String statusMessage;
	private String token;
	
	@JsonIgnore
	private List<SmallGroupIdName> departments;
	
	//private Long defaultSmallGroup;
	@JsonIgnore
	private Long friendSmallGroupId;
	@JsonIgnore
	private Long mySmallGroupId;

	@JsonIgnore
	private Long uploadMaxSize;
	
	public SecurityUser(){
		super();
	}

	public SecurityUser( String username, String password, String salt,
			 			 boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, 
			 			 Collection<GrantedAuthority> authorities ) {
		super( username, password, salt, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
	}

	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}


	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getProfilePic() {
		return profilePic;
	}

	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public void setAuthorities( Collection<GrantedAuthority> authorities ){
		this.authorities = authorities;
	}
	
	
	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public Long getSiteId() {
		return siteId;
	}

	public void setSiteId(Long siteId) {
		this.siteId = siteId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}


	public Long getFriendSmallGroupId() {
		return friendSmallGroupId;
	}

	public void setFriendSmallGroupId(Long friendSmallGroupId) {
		this.friendSmallGroupId = friendSmallGroupId;
	}

	public Long getMySmallGroupId() {
		return mySmallGroupId;
	}

	public void setMySmallGroupId(Long mySmallGroupId) {
		this.mySmallGroupId = mySmallGroupId;
	}

	@Override
	public String toString() {
		return "SecurityUser [userId=" + userId + ", ipAddress=" + ipAddress
				+ ", type=" + type + ", email=" + email + ", profilePic="
				+ profilePic + ", name=" + name + ", status=" + status
				+ ", admin=" + admin + ", siteId=" + siteId
				+ ", statusMessage=" + statusMessage + ", friendSmallGroupId="
				+ friendSmallGroupId + ", mySmallGroupId=" + mySmallGroupId
				+ "]";
	}

	public Long getUploadMaxSize() {
		return uploadMaxSize;
	}

	public void setUploadMaxSize(Long uploadMaxSize) {
		this.uploadMaxSize = uploadMaxSize;
	}

	public List<SmallGroupIdName> getDepartments() {
		return departments;
	}

	public void setDepartments(List<SmallGroupIdName> departments) {
		this.departments = departments;
	}


	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public boolean isSuperAdmin() {
		return superAdmin;
	}

	public void setSuperAdmin(boolean superAdmin) {
		this.superAdmin = superAdmin;
	}

	
	
}
