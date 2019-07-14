package kr.co.sunnyvale.sunny.service.impl;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import kr.co.sunnyvale.sunny.domain.Site;
import kr.co.sunnyvale.sunny.domain.extend.SecurityUser;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.exception.PageNotFoundException;
import kr.co.sunnyvale.sunny.repository.hibernate.DomainRepository;
import kr.co.sunnyvale.sunny.service.SiteService;
import kr.co.sunnyvale.sunny.service.SmallGroupService;
import kr.co.sunnyvale.sunny.service.SunnyService;
import kr.co.sunnyvale.sunny.service.UserService;
import kr.co.sunnyvale.sunny.util.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.DeviceResolver;
import org.springframework.mobile.device.LiteDeviceResolver;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service( value="sunnyService" )
@Transactional
public class SunnyServiceImpl implements SunnyService {

	@Autowired
	private SiteService siteService; 
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private DomainRepository domainRepository;
	
	@Autowired
	private SmallGroupService smallGroupService; 
	
	private final DeviceResolver deviceResolver = new LiteDeviceResolver();
	
	@Override
	public Sunny parseCurrent(boolean shouldExistsSite) {
		
		ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();       
		HttpServletRequest hsr = sra.getRequest();
		String serverName = hsr.getServerName();
		
		Sunny sunny = new Sunny();
		
		/*
		 * 기본 정보 저장
		 * serverName = www.sunnysns.com 전부 다 가져옴
		 * documentDomain = sunnysns.com 만 가져옴 (CDN 등에서 쓰임)
		 * requestURI = 뒤의 것들을 가져옴 www.sunnysns.com/index/a -> /index/a
		 * ipAddress = 127.0.0.1
		 * sessionId = 브라우저 세션
		 */
		sunny.setServerName(serverName);
		sunny.setServerPort( hsr.getServerPort() );
		sunny.setServerProtocol( hsr.getScheme() );
		sunny.setDocumentDomain( StringUtils.getBaseDomain(serverName) );
		sunny.setRequestURI(hsr.getRequestURI());
		sunny.setIpAddress(hsr.getRemoteAddr());
		sunny.setSessionId(hsr.getSession().getId());
		sunny.setDevice( deviceResolver.resolveDevice(hsr));
		
		
		/**
		 *  설치형에서는 Site가 하나만 존재하기 떄문에 해당 Security User의 Site 정보가 의미가 없다.
		 *
		SecurityUser securityUser = null;
		if (SecurityContextHolder.getContext().getAuthentication() != null) {
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (principal != null && principal instanceof UserDetails) {
				securityUser = (SecurityUser) principal;
			}
		}
		if( securityUser == null ) {
			return sunny;
		}
		sunny.setSite( siteService.findById(securityUser.getSiteId()) );
		*/
		sunny.setSite( siteService.getSiteInstalled() );
		
		return sunny;
	}

	private void checkUserJoinedSite(boolean shouldExistsSite, SecurityUser securityUser, Sunny sunny) {
		// 현재 세션에 현재 사이트가 저장된 경우 끝
		if( securityUser.getSiteId().equals( sunny.getSite().getId() ) ){
			return;
		}
// 사이트에 속했는지 확인해서 만약 확인했으면 securityUser 정보도 변경
//		
//		if( siteUser == null){
//			if( shouldExistsSite == true )
//				throw new PageNotFoundException(); 	
//	
//			sunny.setNoExistsSite(true);
//			return;
//		}
//		
//		
//		SecurityContext securityContext = SecurityContextHolder.getContext();
//		Authentication authentication = securityContext.getAuthentication();
//		securityUser.setSiteId(sunny.getSite().getId());
//		securityUser.setEmail( siteUser.getEmail() );
//		
//		//securityUser.setDepartments( null );//TODO 부서 넣으세요
//		
//		List<SmallGroup> departments = smallGroupService.getSmallGroupList(sunny.getSite(), null, new User( securityUser.getUserId() ), SmallGroup.TYPE_DEPARTMENT, new Stream(100));
//		if( departments != null && departments.size() > 0 ){
//			List<SmallGroupIdName> sessionDepartments = Lists.newArrayList();
//			for( SmallGroup department : departments ){
//				sessionDepartments.add(new SmallGroupIdName( department.getId(), department.getName() ));
//			}
//			securityUser.setDepartments(sessionDepartments);
//		}else{
//			securityUser.setDepartments(null);
//		}
//		
//		securityUser.setStatus( siteUser.getStatus() );
////		securityUser.setMySmallGroupId( user.getMySmallGroup().getId() );
////		securityUser.setFriendSmallGroupId( user.getFriendSmallGroup().getId() );
//		securityUser.setStatusMessage( siteUser.getStatusMessage() );
//		
//		
//		
//		
//		boolean isAdmin = false; 
//		for(Role role : siteUser.getRoles() ){
//			if( role.getId() == Role.ID_INACTIVE_ADMIN || role.getId() == Role.ID_INACTIVE_USER ){
//				throw new AttemptToLoginFromInactiveUser();	
//			}else if( role.getId() == Role.ID_ADMIN ){
//				isAdmin = true;	
//			}
//		}
//		securityUser.setAdmin(isAdmin);
//		Collection<GrantedAuthority> authorities = SecurityUtils.parseSecurityRoles(siteUser.getRoles());
//		securityUser.setAuthorities(authorities);
//		
//		SecurityContextHolder.getContext().setAuthentication(
//				new UsernamePasswordAuthenticationToken(securityUser,
//						authentication.getCredentials(), authorities));
//		
	}

}
