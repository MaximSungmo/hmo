package kr.co.sunnyvale.sunny.service.impl;

import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.UserInfo;
import kr.co.sunnyvale.sunny.repository.hibernate.AuthTokenRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.MediaRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.RoleRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.SequenceRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.SiteRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.SmallGroupRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.SmallGroupSmallGroupAccessRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.UserInfoRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.UserRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.UserSmallGroupAccessRepository;
import kr.co.sunnyvale.sunny.service.MailService;
import kr.co.sunnyvale.sunny.service.SiteService;
import kr.co.sunnyvale.sunny.service.SmallGroupService;
import kr.co.sunnyvale.sunny.service.UserInfoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service( value="userInfoService" )
@Transactional
public class UserInfoServiceImpl implements UserInfoService {
	
	@Autowired
	private SiteRepository siteRepository;
	
	@Autowired
	private MediaRepository mediaRepository;
	
	@Autowired
	private AuthTokenRepository authTokenRepository;
	
	@Autowired
	private SmallGroupRepository smallGroupRepository; 
	
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserInfoRepository userInfoRepository; 
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private MailService mailService;
	
	@Autowired
	private SequenceRepository sequenceRepository;
	
	@Autowired
	private MessageSource messageSource; 
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private SiteService siteService;
	
	@Autowired
	private SmallGroupService smallGroupService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserSmallGroupAccessRepository userSmallGroupAccessRepository;
	
	@Autowired
	private SmallGroupSmallGroupAccessRepository smallGroupSmallGroupAccessRepository;

	@Override
	@Transactional
	public UserInfo findByUserId(Long userId) {
		
		UserInfo userInfo = userInfoRepository.findUniqByObject("user.id", userId);
		
		if( userInfo == null ){
			userInfo = new UserInfo();
			userInfo.setUser(new User(userId));
			userInfoRepository.save(userInfo);
		}
		
		return userInfo;
	}


	

}
