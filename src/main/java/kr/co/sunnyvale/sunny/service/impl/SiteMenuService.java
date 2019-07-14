package kr.co.sunnyvale.sunny.service.impl;

import java.util.List;

import kr.co.sunnyvale.sunny.domain.SiteMenu;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.domain.post.MovePutDTO;
import kr.co.sunnyvale.sunny.exception.SimpleSunnyException;
import kr.co.sunnyvale.sunny.repository.hibernate.MenuRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.SiteMenuRepository;

import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service( value="siteMenuService" )
@Transactional
public class SiteMenuService {
	
	@Autowired
	private MenuRepository menuRepository;
	
	@Autowired
	private MenuService menuService; 
	
	@Autowired
	private SiteMenuRepository siteMenuRepository;
	
	@Autowired
	private MessageSource messageSource;

	public List<SiteMenu> getSiteMenus(Sunny sunny) {

		
		List<SiteMenu> siteMenus = siteMenuRepository.getSiteMenus( sunny.getSite().getId() );
		
		return siteMenus; 
		
	}

	@Transactional
	public void move(MovePutDTO movePutDTO) {
		
		Integer beforePrevOrdering = null;
		if( movePutDTO.getBeforePrevId() != null ){
			SiteMenu beforePrevSiteMenu = siteMenuRepository.select(movePutDTO.getBeforePrevId());
			beforePrevOrdering = beforePrevSiteMenu.getOrdering();
		}
		
		Integer targetPrevOrdering = null;
		if( movePutDTO.getTargetPrevId() != null ){
			SiteMenu targetPrevSiteMenu = siteMenuRepository.select(movePutDTO.getTargetPrevId());
			targetPrevOrdering = targetPrevSiteMenu.getOrdering();
		}
		
		
		int changedOrdering = siteMenuRepository.move(movePutDTO.getSiteId(), movePutDTO.getId(), beforePrevOrdering, targetPrevOrdering);
		
		
		siteMenuRepository.changeOrdering( movePutDTO.getId(), changedOrdering );
		menuService.evictSiteMenu(movePutDTO.getSiteId());
	}

	@Transactional
	public void changeVisible(Long siteMenuId, Boolean isVisible) {
		SiteMenu siteMenu = siteMenuRepository.select(siteMenuId);
		siteMenu.setVisible(isVisible);
		siteMenuRepository.update(siteMenu);
	}

}