package kr.co.sunnyvale.sunny.builder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import kr.co.sunnyvale.sunny.domain.Role;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.extend.SecurityUser;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * 테스트용도의 User 객체를 리턴해주는 빌더. Slipp.net 소스 참고
 * 
 * @author Mook
 *
 */
public class SecurityUserBuilder {
	
	private String name;
	private String email;
	private Long userId;
	private String profilePic;
	private String ipAddress;
	private int type;
	private List<Role> roles;

	public static SecurityUserBuilder generator() {
		return new SecurityUserBuilder();
	}
	
	public SecurityUser build() {
		
		Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		
		authorities = new ArrayList<GrantedAuthority>();
		if( roles != null )
		{
			for(Role role : roles){
				authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName().toUpperCase()));
			}
		}

		SecurityUser securityUser =  new SecurityUser( email, null, null, true, true, true, true, authorities );

		securityUser.setUserId(userId);
		securityUser.setName(name);
		securityUser.setEmail(email);
		securityUser.setUserId(userId);
		securityUser.setProfilePic(profilePic);
		securityUser.setIpAddress(ipAddress);
		securityUser.setType(type);
		return securityUser;
	}

	public SecurityUserBuilder test1() {
		this.userId = UserBuilder.USER1_ID;
		this.name = UserBuilder.USER1_NAME;
		this.email = UserBuilder.USER1_EMAIL;
		
		List<Role> tmpRoles = new ArrayList<Role>();
		tmpRoles.add(new Role(Role.ID_ADMIN, Role.ROLE_ADMIN, "어드민"));
		tmpRoles.add(new Role(Role.ID_USER, Role.ROLE_USER, "일반유저"));
		this.roles = tmpRoles;
		
		return this;
	}
	
	public SecurityUserBuilder test2() {
		this.userId = UserBuilder.USER2_ID;
		this.name = UserBuilder.USER2_NAME;
		this.email = UserBuilder.USER2_EMAIL;
		this.profilePic = null;
		
		List<Role> tmpRoles = new ArrayList<Role>();
		tmpRoles.add(new Role(Role.ID_ADMIN, Role.ROLE_ADMIN, "어드민"));
		tmpRoles.add(new Role(Role.ID_USER, Role.ROLE_USER, "일반유저"));
		this.roles = tmpRoles;
		
		return this;
	}

	public SecurityUserBuilder test3() {
		this.userId = UserBuilder.USER3_ID;
		this.name = UserBuilder.USER3_NAME;
		this.email = UserBuilder.USER3_EMAIL;
		this.profilePic = null;
		
		List<Role> tmpRoles = new ArrayList<Role>();
		tmpRoles.add(new Role(Role.ID_USER, Role.ROLE_USER, "일반유저"));
		this.roles = tmpRoles;
		
		return this;
	}
	
	public SecurityUserBuilder test4() {
		this.userId = UserBuilder.USER4_ID;
		this.name = UserBuilder.USER4_NAME;
		this.email = UserBuilder.USER4_EMAIL;
		this.profilePic = null;
		
		List<Role> tmpRoles = new ArrayList<Role>();
		tmpRoles.add(new Role(Role.ID_ADMIN, Role.ROLE_ADMIN, "어드민"));
		tmpRoles.add(new Role(Role.ID_USER, Role.ROLE_USER, "일반유저"));
		this.roles = tmpRoles;
		
		return this;
	}

}
