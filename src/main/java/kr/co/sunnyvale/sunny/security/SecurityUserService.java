package kr.co.sunnyvale.sunny.security;

import kr.co.sunnyvale.sunny.domain.extend.SecurityUser;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * {SpringSecurity}
 * 
 * <p>
 * 인터페이스 {@link org.springframework.security.core.userdetails.UserDetailsService}의 구현체는 
 * 인증 프로바이더에 포함되어 인증에 필요한 비밀번호(크레덴셜) 뿐만아니라 인증 성공시 시큐리티 콤텍스트(세션)에 저장될 인증 사용자 정보를 제공하는 역활을 한다.
 * 
 * <p>
 * 기본 UserDetailService의 구현체는 User클래스를 통해  UserDetails를 제공하게 되는데, 각 응용에 맞는 UserDetails 제공하기 위해 
 * 커스텀 UserDetails구현체가 필요하며 {@link kr.co.sunnyvale.redwood.security.AbstractSecurityUser} 가 그를 구현한 구현체가 된다.
 * redwood패키지는 기반 라이브러로 활용되어야 하므로 이 구현체를 추상클래스로 하고 실제 응용해서 상속받아 각 각의 응용에 맞는 정보를 세팅하면 된다.
 * 
 * <p>
 * 위에서 언급한대로 기본 UserDetailService의 구현체는 기본 UserDetails 구현체를 다루게 되므로 커스컴 UserDetailService의 구현체도 필요하고
 * 이 구현체는 실제로 응용의 {@link kr.co.sunnyvale.redwood.security.AbstractSecurityUser}의 구현체를 제공하는 service를 가지고
 * 제공받아야 할 것이다. 그 역활을 하는 service interface가 SecurityUserService 이다.
 * 
 * <p>
 * 응용에서는 이 서비스 인터페이스를 구현하고 응용에 맞는 AbstractSecurityUser 구현체를 리턴하면 된다.
 * 
 * 
 * @author kickscar
 *
 */
public interface SecurityUserService {
	public AbstractSecurityUser loadUserByUsername( String username ) throws UsernameNotFoundException, DataAccessException;
	
	public AbstractSecurityUser loadUserByUserId( Long userId ) throws UsernameNotFoundException, DataAccessException;
	
	public SecurityUser signinByUsername( String username ) throws UsernameNotFoundException, DataAccessException;

	
	
}
