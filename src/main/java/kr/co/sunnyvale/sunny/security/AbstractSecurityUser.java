package kr.co.sunnyvale.sunny.security;

import java.io.Serializable;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * {SpringSecurity}
 * 
 * <p>
 * Spring Security의 UserDetails 구현한 추상 클래스
 * <p>
 * 인터페이스 UserDetails는 인증시, username(principal), password(credential)세팅하고 인증 루틴에 제공하기 위한 인터페이스를 정의하고 있음.
 * 그 후, 인증 세션객체로 사용되기때문에 자세한 사용자 정보를 담고 있어야 함.
 * <p>
 * Spring Security에서는 기본으로  UserDetails를 구현한 User클래스를 제공하고 있지만,
 * 유저이름, 비밀번호만 담고있어 인증뿐만 아니라 세션에 담을 정보의 확장을 위해 기본기능만 구현한 추상클래스로 redwood.security에서 제공하고 
 * 응용에서는 이 클래스를 상속받아 응용에 맞는 세션정보를 더 추가만 해준다. 
 * 
 * @see
 * 
 * @author kickscar
 *
 *     	
 */
public abstract class AbstractSecurityUser implements UserDetails, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4229111666840138924L;

	@JsonIgnore
	protected String username;

	@JsonIgnore
	protected String password;

	@JsonIgnore
	protected String salt;

	@JsonIgnore
	protected boolean enabled;

	@JsonIgnore
	protected boolean accountNonExpired;

	@JsonIgnore
	protected boolean credentialsNonExpired;

	@JsonIgnore
	protected boolean accountNonLocked;

	@JsonIgnore
	protected Collection<GrantedAuthority> authorities;
	
	public AbstractSecurityUser(){
	}
	
	public AbstractSecurityUser( String username, String password, String salt,
								 boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, 
								 Collection<GrantedAuthority> authorities ) {
		this.username = username;
		this.password = password;
		this.salt = salt;
		this.enabled = enabled;
		this.accountNonExpired = accountNonExpired;
		this.credentialsNonExpired = credentialsNonExpired;
		this.accountNonLocked = accountNonLocked;
		this.authorities = authorities;
	}

	public boolean hasRole(String role) {
		for (GrantedAuthority grantedAuthority : getAuthorities()){
			if( grantedAuthority.getAuthority().equals(role) == true ){
				return true;
			}
		}
		return false;
	}
	
	@JsonIgnore
	public String getSalt() {
		return salt;
	}
	
	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		return authorities;
	}
	
	@JsonIgnore
	@Override
	public String getPassword() {
		return password;
	}
	
	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}