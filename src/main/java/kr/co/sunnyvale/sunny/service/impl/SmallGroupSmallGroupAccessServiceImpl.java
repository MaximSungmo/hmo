package kr.co.sunnyvale.sunny.service.impl;

import kr.co.sunnyvale.sunny.domain.SmallGroup;
import kr.co.sunnyvale.sunny.domain.SmallGroupSmallGroupAccess;
import kr.co.sunnyvale.sunny.domain.extend.Page;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.exception.SimpleSunnyException;
import kr.co.sunnyvale.sunny.repository.hibernate.SmallGroupInactiveUserRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.SmallGroupRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.SmallGroupSmallGroupAccessRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.StoryRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.UserSmallGroupAccessRepository;
import kr.co.sunnyvale.sunny.service.SiteService;
import kr.co.sunnyvale.sunny.service.SmallGroupSmallGroupAccessService;
import kr.co.sunnyvale.sunny.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service( value="smallGroupSmallGroupAccessService" )
@Transactional
public class SmallGroupSmallGroupAccessServiceImpl implements SmallGroupSmallGroupAccessService {
	
	@Autowired
	private SmallGroupRepository smallGroupRepository;

	@Autowired
	private SmallGroupSmallGroupAccessRepository smallGroupSmallGroupAccessRepository;
	
	@Autowired
	private StoryRepository storyRepository;
	
	@Autowired
	private SiteService siteService;
	
	@Autowired
	private UserService userService; 
	
	@Autowired
	private UserSmallGroupAccessRepository userSmallGroupAccessRepository;
	

	@Autowired
	private SmallGroupInactiveUserRepository smallGroupInactiveUserRepository;


	@Override
	public Page<SmallGroupSmallGroupAccess> getPagedAccessibleList(Sunny sunny,
			Long smallGroupId,  String queryName, String ordering, Boolean desc, Integer page, int pageSize) {
		
		return smallGroupSmallGroupAccessRepository.getPagedAccessibleList(sunny, smallGroupId, queryName, ordering, desc, page, pageSize );
	}


	@Override
	@Transactional
	public void save(Sunny sunny, SmallGroup smallGroup, SmallGroup accessSmallGroup,
			boolean checkExist) {
		
		
		if( checkExist == true ){
			SmallGroupSmallGroupAccess access = smallGroupSmallGroupAccessRepository.getAccess(sunny, smallGroup.getId(), accessSmallGroup.getId());
			
			if( access != null )
				return ;
		}
		
		SmallGroupSmallGroupAccess access = new SmallGroupSmallGroupAccess();
		
		access.setSmallGroup(smallGroup);
		access.setAccessSmallGroup(accessSmallGroup);
		
		
		smallGroupSmallGroupAccessRepository.save(access);
		
	}


	@Override
	@Transactional
	public void updatePermission(Long smallGroupSmallGroupId) {

		SmallGroupSmallGroupAccess access = smallGroupSmallGroupAccessRepository.select(smallGroupSmallGroupId);
		
		if( access == null )
			throw new SimpleSunnyException();
		
		
		smallGroupSmallGroupAccessRepository.update(access);
		
	}


	@Override
	@Transactional
	public void remove(Long smallGroupSmallGroupId) {
		SmallGroupSmallGroupAccess access = smallGroupSmallGroupAccessRepository.select(smallGroupSmallGroupId);
		smallGroupSmallGroupAccessRepository.delete(access);
		
	} 
}