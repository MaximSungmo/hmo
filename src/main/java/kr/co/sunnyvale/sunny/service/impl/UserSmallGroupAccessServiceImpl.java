package kr.co.sunnyvale.sunny.service.impl;

import java.util.List;

import kr.co.sunnyvale.sunny.domain.SmallGroup;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.UserSmallGroupAccess;
import kr.co.sunnyvale.sunny.domain.extend.Page;
import kr.co.sunnyvale.sunny.domain.extend.Stream;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.repository.hibernate.SmallGroupSmallGroupAccessRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.UserSmallGroupAccessRepository;
import kr.co.sunnyvale.sunny.service.SiteService;
import kr.co.sunnyvale.sunny.service.SmallGroupService;
import kr.co.sunnyvale.sunny.service.UserService;
import kr.co.sunnyvale.sunny.service.UserSmallGroupAccessService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service( value="userSmallGroupAccessService" )
@Transactional
public class UserSmallGroupAccessServiceImpl implements UserSmallGroupAccessService {
	
	@Autowired
	private SmallGroupService smallGroupService;
	
	
	@Autowired
	private SiteService siteService;
	
	@Autowired
	private UserService userService; 
	
	@Autowired
	private UserSmallGroupAccessRepository userSmallGroupAccessRepository;
	
	@Autowired
	private SmallGroupSmallGroupAccessRepository smallGroupSmallGroupAccessRepository;

	@Override
	@Transactional
	public Page<UserSmallGroupAccess> getJoined(Sunny sunny, Long smallGroupId, User user, String searchName, String ordering, Boolean desc, Integer page, int pageSize) {
		
		return userSmallGroupAccessRepository.getUserSmallGroupAccessList(smallGroupId, searchName, ordering, desc, page, pageSize);
	}
	
	@Override
	@Transactional
	public List<UserSmallGroupAccess> getJoined(Sunny sunny, SmallGroup smallGroup, User user, String searchName, String ordering, Boolean desc, Stream stream) {
		
		return userSmallGroupAccessRepository.getUserSmallGroupAccessList(smallGroup, searchName, ordering, desc, stream);
	}
	
	@Override
	@Transactional
	public UserSmallGroupAccess save(Sunny sunny, Long smallGroupId, User user) {
		// TODO: 구현
		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public UserSmallGroupAccess findBySmallGroupAndUser(Long smallGroupId,
			Long userId) {
		return userSmallGroupAccessRepository.getUserSmallGroupAccessUser(smallGroupId, userId);
	}
	
}