package kr.co.sunnyvale.sunny.service.impl;

import java.util.List;

import kr.co.sunnyvale.sunny.domain.Site;
import kr.co.sunnyvale.sunny.domain.SiteInactiveUser;
import kr.co.sunnyvale.sunny.domain.extend.Page;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.repository.hibernate.SiteInactiveUserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service( value="siteInactiveUserService" )
@Transactional
public class SiteInactiveUserService {
	
	@Autowired
	private SiteInactiveUserRepository siteInactiveUserRepository;

	@Transactional
	public void save(SiteInactiveUser siteInactiveUser) {
		siteInactiveUserRepository.save(siteInactiveUser);
	}		

	@Transactional
	public void update(SiteInactiveUser siteInactiveUser) {
		siteInactiveUserRepository.update(siteInactiveUser);
	}

	
		
	@Transactional(readOnly = true)
	public Page<SiteInactiveUser> getSiteInactiveUsers(Sunny sunny, Integer status, Integer type,
			Integer page, int pageSize) {
		return siteInactiveUserRepository.getSiteInactiveUsers( sunny, status, type, page, pageSize );
	}

	@Transactional(readOnly = true)
	public SiteInactiveUser findById(Long siteInactiveUserId) {
		return siteInactiveUserRepository.select(siteInactiveUserId);
	}

	@Transactional(readOnly = true)
	public SiteInactiveUser findBySiteAndUser(Long id, Long userId) {
		return siteInactiveUserRepository.findBySiteAndUser( id, userId );
	}

	@Transactional(readOnly = true)
	public SiteInactiveUser findByEmail(Site site, String email) {
		return siteInactiveUserRepository.findByEmail( site, email );
	}

	@Transactional(readOnly = true)
	public SiteInactiveUser findByEmailAndType(Site site, String email, Integer type) {
		return siteInactiveUserRepository.findByEmailAndType( site, email, type);
	}
	
	@Transactional
	public void delete(Long siteInactiveUserId) {
		SiteInactiveUser siteInactiveUser = siteInactiveUserRepository.select(siteInactiveUserId);
		siteInactiveUserRepository.delete(siteInactiveUser);
	}

	@Transactional(readOnly = true)
	public SiteInactiveUser findAdminByEmail(String email) {
		return siteInactiveUserRepository.findAdminByEmail( email );
	}

}