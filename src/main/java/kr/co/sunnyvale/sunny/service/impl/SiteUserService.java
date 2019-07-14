//package kr.co.sunnyvale.sunny.service.impl;
//
//import java.util.List;
//
//import kr.co.sunnyvale.sunny.domain.Admin;
//import kr.co.sunnyvale.sunny.domain.Site;
//import kr.co.sunnyvale.sunny.domain.SiteInactiveUser;
//import kr.co.sunnyvale.sunny.domain.SiteUser;
//import kr.co.sunnyvale.sunny.domain.extend.Page;
//import kr.co.sunnyvale.sunny.domain.extend.Sunny;
//import kr.co.sunnyvale.sunny.domain.post.UpdateSiteUserFromAdminDTO;
//import kr.co.sunnyvale.sunny.repository.hibernate.AdminRepository;
//import kr.co.sunnyvale.sunny.repository.hibernate.SiteUserRepository;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service( value="siteUserService" )
//@Transactional
//public class SiteUserService {
//	
//	@Autowired
//	private SiteUserRepository siteUserRepository;
//	
//	@Autowired
//	private AdminRepository adminRepository;
//
//	@Transactional
//	public void save(SiteUser siteUser) {
//		siteUserRepository.save(siteUser);
//	}		
//
//	@Transactional
//	public void update(SiteInactiveUser siteInactiveUser) {
//		// TODO Auto-generated method stub
//		
//	}
//		
//	@Transactional(readOnly = true)
//	public SiteUser findBySiteAndUser( Long siteId, Long userId ){
//		return siteUserRepository.findBySiteAndUser( siteId, userId );
//	}
//	
//	@Transactional(readOnly = true)
//	public SiteUser findById(Long siteUserId) {
//		return siteUserRepository.select(siteUserId);
//	}
//
//	@Transactional(readOnly = true)	
//	public Site getUserJoinedSite(Long id) {
//		SiteUser siteUser = siteUserRepository.getUserJoinedSite( id );
//		return siteUser.getSite();
//	}
//
//	@Transactional(readOnly = true)
//	public List<SiteUser> findByEmail(String email) {
//		return siteUserRepository.findListByObject("email", email, null);
//	}
//
//	@Transactional(readOnly = true)
//	public Page<SiteUser> getUserList(Sunny sunny, List<Long> smallGroupIds,
//			Integer queryType, String queryName, Integer[] status, String range, String ordering, Long excludeUserId, Boolean onlyAdmin, Integer page,
//			int pageSize) {
//
//		Page<SiteUser> pagedUser = siteUserRepository.getUserList(sunny, smallGroupIds, queryType, queryName, status, range, ordering, excludeUserId, onlyAdmin, page, pageSize);
//		return pagedUser;
//	}
//
//	@Transactional(readOnly = true)
//	public SiteUser findBySiteAndEmail(Site site, String email) {
//		return siteUserRepository.findBySiteAndEmail( site, email );
//	}
//
//	@Transactional
//	public void update(Long siteUserId,
//			UpdateSiteUserFromAdminDTO updateSiteUserFromAdminDto) {
//		
//		SiteUser siteUser = siteUserRepository.select(siteUserId);
//		
////		if( updateSiteUserFromAdminDto.getJobTitle1() != null ){
//			siteUser.setJobTitle1( updateSiteUserFromAdminDto.getJobTitle1() );
////		}
////		if( updateSiteUserFromAdminDto.getJobTitle2() != null ){
//			siteUser.setJobTitle2( updateSiteUserFromAdminDto.getJobTitle2() );
////		}
////		if( updateSiteUserFromAdminDto.getJobTitle3() != null ){
//			siteUser.setJobTitle3( updateSiteUserFromAdminDto.getJobTitle3() );
////		}
//		
//		siteUser.setStatus( updateSiteUserFromAdminDto.getStatus());
//			
//		
//		// admin true -> false 로 바뀔 때
//		if( (updateSiteUserFromAdminDto.getAdmin() == null || updateSiteUserFromAdminDto.getAdmin() == false ) && siteUser.isAdmin() == true ){
//			siteUser.setAdmin( false );
//			Admin admin = adminRepository.findBySiteAndUser(siteUser.getSite().getId(), siteUser.getUser().getId());
//			if( admin != null )
//				adminRepository.delete(admin);
//		}else if( updateSiteUserFromAdminDto.getAdmin() != null && updateSiteUserFromAdminDto.getAdmin() == true && siteUser.isAdmin() == false ){
//			siteUser.setAdmin( true );
//			Admin admin = new Admin();
//			admin.setUser(siteUser.getUser());
//			admin.setSite(siteUser.getSite());
//			adminRepository.save(admin);
//		}
//		
////		if( updateSiteUserFromAdminDto.getAdminComment() != null ){
//			siteUser.setAdminComment( updateSiteUserFromAdminDto.getAdminComment() );
////		}
//		siteUserRepository.update(siteUser);
//		
//	}
//
//}