package kr.co.sunnyvale.sunny.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import kr.co.sunnyvale.sunny.domain.Role;
import kr.co.sunnyvale.sunny.domain.extend.SecurityUser;

public class SecurityUtils {
	
	public static Collection<GrantedAuthority> parseSecurityRoles(List<Role> roles ) {
		Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		

		authorities = new ArrayList<GrantedAuthority>();

		if( roles == null )
			return authorities;
		
		for(Role role : roles){
			authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName().toUpperCase()));
		}
		return authorities;
	}
}
