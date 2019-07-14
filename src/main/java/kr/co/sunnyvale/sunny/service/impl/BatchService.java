package kr.co.sunnyvale.sunny.service.impl;

import java.util.List;
import java.util.regex.Pattern;

import kr.co.sunnyvale.sunny.domain.Media;
import kr.co.sunnyvale.sunny.redis.RedisPublisher;
import kr.co.sunnyvale.sunny.repository.hibernate.MediaRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.UserRepository;
import kr.co.sunnyvale.sunny.service.AdminService;
import kr.co.sunnyvale.sunny.service.NotificationService;
import kr.co.sunnyvale.sunny.service.ReceiverRelationService;
import kr.co.sunnyvale.sunny.service.SiteService;
import kr.co.sunnyvale.sunny.service.UserService;
import kr.co.sunnyvale.sunny.service.UserSmallGroupAccessService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service( value="batchService" )
@Transactional
public class BatchService {
	
	@Autowired
	private UserService userService;
//	
//	@Autowired
//	private ActivityService activityService;
//	
	@Autowired
	private NotificationService notificationService;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private ReceiverRelationService receiverRelationService;

	@Autowired
	private RedisPublisher redisPublisher;
	
	@Autowired
	private SiteService siteService;
	
	@Autowired
	private UserSmallGroupAccessService userSmallGroupAccessService;
	
	@Autowired
	private UserRepository userRepository; 
	
	@Autowired
	private AdminService adminService;

	@Autowired
	private MediaRepository mediaRepository;
	
	@Transactional
	public void retypeMedia() {
		List<Media> mediaes = mediaRepository.getAll(null);
		
		for(Media media : mediaes ){
			if( media.getExtName() != null ){
				if(Pattern.compile("doc|docx").matcher(media.getExtName()).matches() ){
					media.setMediaType(Media.TYPE_WORD);
				}else if( Pattern.compile("xls|xlsx").matcher(media.getExtName()).matches() ){
					media.setMediaType(Media.TYPE_EXCEL);
				}else if( Pattern.compile("ppt|pptx").matcher(media.getExtName()).matches() ){
					media.setMediaType(Media.TYPE_POWERPOINT);
				}else if( Pattern.compile("hwp").matcher(media.getExtName()).matches() ){
					media.setMediaType(Media.TYPE_HWP);
				}else if( Pattern.compile("pdf").matcher(media.getExtName()).matches() ){
					media.setMediaType(Media.TYPE_PDF);
				}
			}
		}
	}
	
	
//	@Transactional
//	public void migrationMultiSite(){
//		
//		
//		
//		// User 에 있는 기존 Email 들을 다 새로 생성함
//		for(Email email : emailRepository.getAll(null) ){
//			emailRepository.delete(email);
//		}
//		
//		for(SiteUser siteUser : siteUserRepository.getAll(null) ){
//			siteUserRepository.delete(siteUser);
//		}
//		
//		List<User> users = userRepository.getAll(null);
//		
//		// 모든 이메일을 Email 에다 넣는다.
//		for(User userEach : users ){
//			
//			User user = userRepository.select( userEach.getId() );
//			System.out.println("유저 아이디 : " + user.getId() + " Email : " + user.getEmail() );
//			System.out.println("사이트 : " + user.getSite().getId());
//			
//			Email email = new Email();
//			email.setUser(user);
//			email.setEmail( user.getEmail() );
//			emailRepository.save(email);
//			
//			SiteUser siteUser = new SiteUser();
//			siteUser.setJobTitle1( user.getJobTitle1() );
//			siteUser.setJobTitle2( user.getJobTitle2() );
//			siteUser.setJobTitle3( user.getJobTitle3() );
//			siteUser.setCreateDate( user.getCreateDate() );
//			siteUser.setStatusMessage( user.getStatusMessage() );
//			siteUser.setAdminComment( user.getAdminComment() );
//			siteUser.setUser( user );
//			siteUser.setSite( user.getSite() );
//			siteUser.setAdmin( adminService.findBySiteAndUser(user.getSite().getId(), user.getId() ) == null ? false : true );
//			
//			
//			List<Role> roles = Lists.newArrayList();
//			
//			Role role = new Role(Role.ID_USER);
//			roles.add(role);
//			
//			if( siteUser.isAdmin() ){
//				Role adminRole = new Role(Role.ID_ADMIN);
//				roles.add(adminRole);
//			}
//			siteUser.setRoles( roles );
//			
//			siteUserRepository.save(siteUser);
//			
//			user.setRoles(roles);
//			user.setDefaultSite( user.getSite() );
//			userRepository.update( user );
//			
//			
//		}
//		
//		
//		
//	}


}
