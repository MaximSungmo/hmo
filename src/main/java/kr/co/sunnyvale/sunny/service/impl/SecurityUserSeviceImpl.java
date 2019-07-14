package kr.co.sunnyvale.sunny.service.impl;

import java.util.Collection;
import java.util.List;

import kr.co.sunnyvale.sunny.domain.Role;
import kr.co.sunnyvale.sunny.domain.Site;
import kr.co.sunnyvale.sunny.domain.SiteInactiveUser;
import kr.co.sunnyvale.sunny.domain.SmallGroup;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.extend.SecurityUser;
import kr.co.sunnyvale.sunny.domain.extend.SmallGroupIdName;
import kr.co.sunnyvale.sunny.domain.extend.Stream;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.exception.AttemptToLoginFromInactiveUser;
import kr.co.sunnyvale.sunny.exception.NoExistsUserException;
import kr.co.sunnyvale.sunny.repository.hibernate.UserRepository;
import kr.co.sunnyvale.sunny.security.AbstractSecurityUser;
import kr.co.sunnyvale.sunny.security.InactiveAdminException;
import kr.co.sunnyvale.sunny.security.InactiveUserException;
import kr.co.sunnyvale.sunny.security.InactiveUserNotYetAcceptInviteEmailException;
import kr.co.sunnyvale.sunny.security.NoExistUserLoginException;
import kr.co.sunnyvale.sunny.security.SecurityUserService;
import kr.co.sunnyvale.sunny.service.LoginInfoService;
import kr.co.sunnyvale.sunny.service.SiteService;
import kr.co.sunnyvale.sunny.service.SmallGroupService;
import kr.co.sunnyvale.sunny.service.SunnyService;
import kr.co.sunnyvale.sunny.service.UserService;
import kr.co.sunnyvale.sunny.util.SecurityUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

/**
 * {SpringSecurity}
 * 
 * <p>
 * {@link kr.co.sunnyvale.redwood.security.SecurityUserService}의 응용 구현체다
 *  
 * <p>
 * loadUserByUsernamed에서 사용자 프린셔플(보통 사용자 아이디, 야캠프에선 이메일)를 받아
 * 응용 레포지토리에서 사용자에 맞는 인증에 필요한 크레뎐셜(비밀번호)와 인증 성공시 시큐리티 콘텍스(세션)에 저장할 정보를  
 * AbstractSecurityUser를 상속한 응용의 클래스(야캠프에선 도메인 클래스로  {@link kr.co.sunnyvale.sunny.domain.extend.redwood.domain.SecurityUser})
 * 를 리턴해야 한다.
 *  
 * @author kickscar
 *
 */
@Service( value="securityUserService" )
public class SecurityUserSeviceImpl implements SecurityUserService {

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private LoginInfoService loginInfoService;
	
	@Autowired
	private SunnyService sunnyService; 
	
	@Autowired
	private SiteService siteService;
	
	@Autowired
	private SmallGroupService smallGroupService; 
	
	@Autowired
	private SiteInactiveUserService siteInactiveUserService; 
	
	@Override
	@Transactional
	public AbstractSecurityUser loadUserByUsername( String username ) throws UsernameNotFoundException, DataAccessException {
		
		
		
		User user = ( User )userService.findByEmail( username );
		
		if(user == null) {
//			
			SiteInactiveUser siteInactiveUser = siteInactiveUserService.findAdminByEmail(username);
//			
			if( siteInactiveUser != null ){
				throw new InactiveAdminException(username);
			}
//			
//			// 초대메일 받은 사용자가 로그인을 시도했을 때
//			if( siteInactiveUser.getType() == SiteInactiveUser.TYPE_INVITE ){
//				throw new InactiveUserNotYetAcceptInviteEmailException(username);
//			}
//			
//			// 관리자가 아직 인증받지 않았을 경우. CustomUrlAuthenticationFailureHandler 에서 /a/reactivate 로 보냄
//			if( siteInactiveUser.isAdmin() ){
//				throw new InactiveAdminException(username);
//			}
//			
//			// 사용자가 가입 요청을 보내놓은 상태. 그냥 Fail 하는게 나을듯?
//			if( siteInactiveUser.getStatus() == SiteInactiveUser.STATUS_REQUEST ){
//				throw new NoExistUserLoginException(username);
//			}
//			
//			// 사용자에게 메일을 보내놓은 상태 /user/reactivate 로 보냄
//			if( siteInactiveUser.getStatus() == SiteInactiveUser.STATUS_SEND_EMAIL ){
//				throw new InactiveUserException(username);
//			}
		}
		Site site = siteService.findById(user.getSite().getId());
		
		
		if( user.getRoles() == null || user.getRoles().size() == 0 ){
			throw new NoExistUserLoginException(username);
		}
		boolean isSuperAdmin = false;
		for(Role role : user.getRoles() ){
			if( role.getId() == Role.ID_SUPER_ADMIN){
				isSuperAdmin = true;	
			}
		}
		Collection<GrantedAuthority> authorities = SecurityUtils.parseSecurityRoles(user.getRoles());
		
		
//		authorities.add( new SimpleGrantedAuthority( user.getAuthority() ) );

		SecurityUser securityUser =  new SecurityUser( user.getEmail(), user.getPassword(), user.getSalt(), true, true, true, true, authorities );
		
		try{
			securityUser.setUserId( user.getId() );
			securityUser.setName(user.getName());
			securityUser.setProfilePic(user.getProfilePic());
			securityUser.setEmail(user.getEmail());
			securityUser.setStatus(user.getStatus());
			securityUser.setAdmin(user.isAdmin());
			securityUser.setSuperAdmin(isSuperAdmin);
			securityUser.setToken(user.getPassword());
			
			securityUser.setUploadMaxSize(site.getUploadMaxSize());
			securityUser.setSiteId(site.getId());
			securityUser.setSiteName( site.getCompanyName() );
			
			
			
			if( user.getFriendSmallGroup() != null )
				securityUser.setFriendSmallGroupId( user.getFriendSmallGroup().getId() );
			
			if( user.getMySmallGroup() != null )
				securityUser.setMySmallGroupId( user.getMySmallGroup().getId() );
	
			
			List<SmallGroup> departments = smallGroupService.getSmallGroupList(site, null, user, SmallGroup.TYPE_DEPARTMENT, new Stream(100));
			if( departments != null && departments.size() > 0 ){
				List<SmallGroupIdName> sessionDepartments = Lists.newArrayList();
				for( SmallGroup department : departments ){
					sessionDepartments.add(new SmallGroupIdName( department.getId(), department.getName() ));
				}
				securityUser.setDepartments(sessionDepartments);
			}
			Sunny sunny = sunnyService.parseCurrent(false);
			loginInfoService.successLogin(sunny, user);
				
		}catch(Exception ex){
			ex.printStackTrace();
			throw ex;
		}
		return securityUser;
	}

	@Override
	@Transactional
	public AbstractSecurityUser loadUserByUserId( Long userId ) throws UsernameNotFoundException, DataAccessException {
		
		User user = ( User )userRepository.select( userId );
		if(user == null) {
			throw new NoExistsUserException();			
		}

		Site site = siteService.findById(user.getSite().getId());


		if( user.getRoles() == null || user.getRoles().size() == 0 ){
			throw new NoExistUserLoginException(Long.toString(userId));
		}
//		boolean isAdmin = false;
//		for(Role role : user.getRoles() ){
//			if( role.getId() == Role.ID_INACTIVE_ADMIN || role.getId() == Role.ID_INACTIVE_USER ){
//				throw new AttemptToLoginFromInactiveUser();	
//			}else if( role.getId() == Role.ID_ADMIN ){
//				isAdmin = true;	
//			}
//		}
		boolean isSuperAdmin = false;
		for(Role role : user.getRoles() ){
			if( role.getId() == Role.ID_SUPER_ADMIN){
				isSuperAdmin = true;	
			}
		}
		
		Collection<GrantedAuthority> authorities = SecurityUtils.parseSecurityRoles(user.getRoles());
		
		
		SecurityUser securityUser =  new SecurityUser( user.getEmail(), user.getPassword(), user.getSalt(), true, true, true, true, authorities );
		
		try{
			securityUser.setUserId( user.getId() );
			securityUser.setName(user.getName());
			securityUser.setProfilePic(user.getProfilePic());
			securityUser.setEmail(user.getEmail());
			securityUser.setStatus(user.getStatus());
			securityUser.setAdmin(user.isAdmin());
			securityUser.setSuperAdmin(isSuperAdmin);
			securityUser.setToken(user.getPassword());
			
			securityUser.setUploadMaxSize(site.getUploadMaxSize());
			securityUser.setSiteId(site.getId());
			securityUser.setSiteName( site.getCompanyName() );
			
			List<SmallGroup> departments = smallGroupService.getSmallGroupList(site, null, user, SmallGroup.TYPE_DEPARTMENT, new Stream(100));
			if( departments != null && departments.size() > 0 ){
				List<SmallGroupIdName> sessionDepartments = Lists.newArrayList();
				for( SmallGroup department : departments ){
					sessionDepartments.add(new SmallGroupIdName( department.getId(), department.getName() ));
				}
				securityUser.setDepartments(sessionDepartments);
			}
			
			Sunny sunny = sunnyService.parseCurrent(false);
	
			loginInfoService.successLogin(sunny, user);
	
		}catch(Exception ex){
			ex.printStackTrace();
			throw ex;
		}

		return securityUser;
	}

	

	@Override
	@Transactional
	public SecurityUser signinByUsername(String username) throws UsernameNotFoundException,
			DataAccessException {
		SecurityUser securityUser =  (SecurityUser) loadUserByUsername(username);
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authentication = securityContext.getAuthentication();
		SecurityContextHolder.getContext().setAuthentication( new UsernamePasswordAuthenticationToken( securityUser, authentication.getCredentials(), securityUser.getAuthorities() ) );
		return securityUser;
	}

}
