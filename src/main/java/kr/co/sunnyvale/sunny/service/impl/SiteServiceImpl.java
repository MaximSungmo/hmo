package kr.co.sunnyvale.sunny.service.impl;

import java.util.List;

import kr.co.sunnyvale.sunny.domain.Role;
import kr.co.sunnyvale.sunny.domain.Site;
import kr.co.sunnyvale.sunny.domain.SiteInactiveUser;
import kr.co.sunnyvale.sunny.domain.SmallGroup;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.extend.Page;
import kr.co.sunnyvale.sunny.domain.extend.Stream;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.domain.post.SiteUpdateDTO;
import kr.co.sunnyvale.sunny.exception.SimpleSunnyException;
import kr.co.sunnyvale.sunny.repository.hibernate.SiteInactiveUserRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.SiteRepository;
import kr.co.sunnyvale.sunny.service.AfterService;
import kr.co.sunnyvale.sunny.service.MailService;
import kr.co.sunnyvale.sunny.service.SiteService;
import kr.co.sunnyvale.sunny.service.SmallGroupService;
import kr.co.sunnyvale.sunny.service.StoryService;
import kr.co.sunnyvale.sunny.service.SunnyService;
import kr.co.sunnyvale.sunny.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

@Service( value="siteService" )
@Transactional
public class SiteServiceImpl implements SiteService {
	
	@Autowired
	private SiteRepository siteRepository;
	
	@Autowired
	private SiteInactiveUserService siteInactiveUserService; 

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private MailService mailService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private StoryService storyService;
	
	@Autowired
	private SmallGroupService smallGroupService; 

	@Autowired
	private SiteInactiveUserRepository siteInactiveUserRepository;
	
	@Autowired
	private MessageSource messageSource; 
	
	@Autowired
	private AfterService afterService; 
	
	
	@Override
	public boolean checkDomain(String domain) {
		
		if( domain == null )
			return false; 
		
		Long siteId = (Long) siteRepository.findColumnByObject("id", "domain", domain);
		
		if( siteId != null && siteId > 0 ){
			return true;
		}
		return false;
	}

	@Override
	@Transactional( readOnly = true)
	public Site getSiteInstalled() {
		Site site = siteRepository.getCurrentSite();
		return site;
	}	


	@Override
	@Transactional( readOnly = true)
	@Cacheable(	value="sunnySiteCache", key="#id" )
	public Site findById(Long id) {
		return siteRepository.select(id);
	}

//	@Override
//	@Transactional( readOnly = true)
//	@Cacheable(	value="sunnySiteCache", key="#id" )
//	public Site findFromPathOrId(String id) {
//		return siteRepository.findFromPathOrId(id);
//	}


	@Override
	public Site findByUserId(Long userId) {
		return siteRepository.findByUserId(userId);
	}



	@Override
	@Transactional(readOnly = true)
	public Page<Site> getOpenSites(Long userId, String query, String ordering, Boolean desc,
			Integer page, int pageSize) {
		
		Page<Site> pagedResult = siteRepository.getOpenSites( query, ordering, desc, page, pageSize);
		
		if( userId == null )
			return pagedResult; 
		
		for( Site site : pagedResult.getContents() ){
			
			
			boolean isJoinedUser = userService.isJoined(site.getId(), userId);
			if( isJoinedUser == true ){
				site.setJoinedUser( true );
				continue;
			}
			
			
//			SiteInactiveUser siteInactiveUser = siteInactiveUserService.findBySiteAndUser( site.getId(), userId );
//			if( siteInactiveUser != null ){
//				site.setAlreadyRequestUser(true);
//			}
		}
		
		return pagedResult; 
	}


	
	
	@Override
	@Transactional
	public void requestSignup( Sunny sunny, SiteInactiveUser siteInactiveUser ) {
		siteInactiveUser.setStatus(SiteInactiveUser.STATUS_REQUEST);
		siteInactiveUser.setType( SiteInactiveUser.TYPE_REQUEST );
		siteInactiveUserService.save( siteInactiveUser );
		
		
		afterService.requestSignup( sunny, siteInactiveUser );
	}


	@Override
	@Transactional
	public void acceptInactiveUserAfterConfirm( Sunny sunny, Long siteInactiveUserId) {
		
		SiteInactiveUser siteInactiveUser = siteInactiveUserService.findById( siteInactiveUserId );
		
		siteInactiveUser.setStatus( SiteInactiveUser.STATUS_SEND_EMAIL );
		siteInactiveUserService.update(siteInactiveUser);
		mailService.sendConfirmSiteInactiveUserMail( sunny, siteInactiveUser );
	}

//	@Override
//	@Transactional
//	public void accept(Sunny sunny, Long siteInactiveUserId) {
//		
//		SiteInactiveUser siteInactiveUser = siteInactiveUserService.findById(siteInactiveUserId);
//		
//		
//		User user = null; 
//		Site site = siteRepository.select(siteInactiveUser.getSite().getId());
//		
//		if( siteInactiveUser.getUser() == null ){
//			user = new User();
//			user.setName(siteInactiveUser.getName());
//			user.setPassword( siteInactiveUser.getPassword() );
//			user.setSalt(siteInactiveUser.getSalt());
//
//
////			user.setSite(siteInactiveUser.getSite());
//			
//			userService.save(user);
//			
//			//나만의 스몰그룹을 생성
//			SmallGroup mySmallGroup = smallGroupService.generateMySmallGroup(site, user);
//			user.setMySmallGroup(mySmallGroup);
//			
//			
//			//친구 스몰그룹을 생성.
//			SmallGroup friendSmallGroup = smallGroupService.generateFriendSmallGroup(site, user);
//			user.setFriendSmallGroup(friendSmallGroup);
//			
//			userService.update(user.getId(), user);
//		}else{
//			user = userService.findById(siteInactiveUser.getUser().getId());
//		}
//				
//		Email email = emailRepository.findUniqByObject("email", siteInactiveUser.getEmail());
//		
//		if( email == null ){
//			email = new Email();
//			email.setEmail(siteInactiveUser.getEmail());
//			email.setUser(user);
//			emailRepository.save(email);
//		}
//		
//		
//		SiteUser siteUser = new SiteUser();
//		siteUser.setSite(site);
//		siteUser.setUser(user);
//		siteUser.setEmail(siteInactiveUser.getEmail());
//
//		List<Role> roles = Lists.newArrayList();
//		
//		Role role = new Role(Role.ID_USER);
//		roles.add(role);
//		siteUser.setRoles(roles);
//
//		
//		siteUserRepository.save(siteUser);
//		
//		smallGroupService.addUserToSmallGroup(user.getId(), site.getLobbySmallGroup().getId(), false);
//
//		siteInactiveUserRepository.delete(siteInactiveUser);
//		
//	}


	@Override
	@Transactional(readOnly = true)
	public List<Site> getUserJoinedSites(Long userId, Stream stream) {
		return siteRepository.getUserJoinedSites( userId, stream );
	}


	@Override
	@CacheEvict( value = "sunnySiteCache", key="#id")
	@Transactional
	public void update(Long id, SiteUpdateDTO siteUpdateDto) {
		
		Site site = siteRepository.select(id);
		if( site == null ){
			throw new SimpleSunnyException();
		}
		
		
		if( siteUpdateDto.getCompanyName() != null ){
			site.setCompanyName( siteUpdateDto.getCompanyName() );
		}
		
		if( siteUpdateDto.getCompanyDomain() != null ){
			site.setCompanyDomain( siteUpdateDto.getCompanyDomain() );
		}
		
		if( siteUpdateDto.getCompanyPhone() != null ){
			site.setCompanyPhone( siteUpdateDto.getCompanyPhone() );
		}
		
		if( siteUpdateDto.getHomepage() != null ){
			site.setHomepage( siteUpdateDto.getHomepage() );
		}
		
		if( siteUpdateDto.getPrivacy() != null ){
			site.setPrivacy( siteUpdateDto.getPrivacy() );
		}
		
		if( siteUpdateDto.getNoticeDuration() != null ){
			site.setNoticeDuration( siteUpdateDto.getNoticeDuration() );
		}
		
		if( siteUpdateDto.getAccessIpPds() != null ){
			site.setAccessIpPds( siteUpdateDto.getAccessIpPds() );
		}
		
		if( siteUpdateDto.getCompanyIntroduce() != null ){
			site.setCompanyIntroduce( siteUpdateDto.getCompanyIntroduce() );
		}
		
		siteRepository.update(site);
		
	}


	@Override
	@Transactional
	public User signupComplete(SiteInactiveUser extraSiteInactiveUser) {

		SiteInactiveUser siteInactiveUser = siteInactiveUserService.findById(extraSiteInactiveUser.getId());

		if( userService.existsEmail( siteInactiveUser.getEmail() ) ){
			throw new SimpleSunnyException("user.emailExists");
		}
		
		User user = null; 
		Site site = siteRepository.select(siteInactiveUser.getSite().getId());
		
		user = new User();
		
		if( extraSiteInactiveUser.getName() != null ){
			user.setName(extraSiteInactiveUser.getName());
		}else{
			user.setName( siteInactiveUser.getName() );
		}
		
		user.setPassword( passwordEncoder.encodePassword( extraSiteInactiveUser.getPassword(), extraSiteInactiveUser.getSalt()) );
		user.setSalt(extraSiteInactiveUser.getSalt());
		user.setSite(site);
		user.setEmail(siteInactiveUser.getEmail());
		user.setJobTitle1(siteInactiveUser.getJobTitle1());
		user.setJobTitle2(siteInactiveUser.getJobTitle2());
		user.setJobTitle3(siteInactiveUser.getJobTitle3());

		List<Role> roles = Lists.newArrayList();
		Role role = new Role(Role.ID_USER);
		roles.add(role);
		user.setRoles(roles);
		
		userService.save(user);
		
		//나만의 스몰그룹을 생성
		SmallGroup mySmallGroup = smallGroupService.generateMySmallGroup(site, user);
		
		//친구 스몰그룹을 생성.
		SmallGroup friendSmallGroup = smallGroupService.generateFriendSmallGroup(site, user);
		
		user = userService.findById(user.getId());
		user.setMySmallGroup(mySmallGroup);
		user.setFriendSmallGroup(friendSmallGroup);
		
		userService.update(user.getId(), user);
		
		
		smallGroupService.addUserToSmallGroup(user.getId(), site.getLobbySmallGroup().getId(), false);
		
		// 삭제가 나을까 변경이 나을까?
//		siteInactiveUser.setStatus(SiteInactiveUser.STATUS_COMPLETE);
//		siteInactiveUserRepository.update(siteInactiveUser);
		siteInactiveUserRepository.delete(siteInactiveUser);
		return user;
		
		
	}


	@Override
	@Transactional(readOnly = true)
	public Page<Site> getAllFromSuper(Integer page, int pageSize) {
		Page<Site> pagedResult = siteRepository.getAllFromSuper(page, pageSize);
		
		for( Site site : pagedResult.getContents() ){
			site.setStoryCount( storyService.getSiteStoryCount( site.getId() ) );
		}
		
		return pagedResult;
	}
	
//	@Override
//	@Transactional
//	public void accept(Sunny sunny, Long siteInactiveUserId) {
//		
//		SiteInactiveUser siteInactiveUser = siteInactiveUserService.findById(siteInactiveUserId);
//		
//		
//		User user = null; 
//		Site site = siteRepository.select(siteInactiveUser.getSite().getId());
//		
//		user = new User();
//		user.setName(siteInactiveUser.getName());
//		user.setPassword( siteInactiveUser.getPassword() );
//		user.setSalt(siteInactiveUser.getSalt());
//		user.setEmail(siteInactiveUser.getEmail());
//
//		List<Role> roles = Lists.newArrayList();
//		
//		Role role = new Role(Role.ID_USER);
//		roles.add(role);
//		user.setRoles(roles);
//		
//		userService.save(user);
//		
//		//나만의 스몰그룹을 생성
//		SmallGroup mySmallGroup = smallGroupService.generateMySmallGroup(site, user);
//		user.setMySmallGroup(mySmallGroup);
//		
//		//친구 스몰그룹을 생성.
//		SmallGroup friendSmallGroup = smallGroupService.generateFriendSmallGroup(site, user);
//		user.setFriendSmallGroup(friendSmallGroup);
//		
//		userService.update(user.getId(), user);
//				
//		smallGroupService.addUserToSmallGroup(user.getId(), site.getLobbySmallGroup().getId(), false);
//
//		siteInactiveUser.setStatus(SiteInactiveUser.STATUS_COMPLETE);
//		siteInactiveUserRepository.update(siteInactiveUser);
//	}

	
}