package kr.co.sunnyvale.sunny.service.impl;

import java.util.List;

import kr.co.sunnyvale.sunny.domain.Menu;
import kr.co.sunnyvale.sunny.domain.SiteMenu;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.repository.hibernate.MenuRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.SiteMenuRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service( value="menuService" )
@Transactional
public class MenuService {
	
	@Autowired
	private MenuRepository menuRepository;
	
	@Autowired
	private SiteMenuRepository siteMenuRepository;
	
	@Autowired
	private MessageSource messageSource;

	@Transactional(readOnly = true)
	public List<SiteMenu> getSiteMenus(Sunny sunny) {

		List<SiteMenu> siteMenus = siteMenuRepository.getSiteMenus( sunny.getSite().getId() );
		return siteMenus; 
		
	}

	@Transactional(readOnly = true)
	@Cacheable(	value="sunnyMenuCache", key="#siteId" )
	public List<Menu> findBySite(Long siteId) {
		return menuRepository.findBySite( siteId );
	}
	
	@CacheEvict( value = "sunnyMenuCache", key="#siteId")
	public void evictSiteMenu( Long siteId ){
		
	}

	@Transactional
	public void remove(Integer menuId) {
		Menu menu = menuRepository.select(menuId);
		menuRepository.delete(menu);
	}

	@Transactional
	public void updateChanged(Menu menu) {
		Menu persistentMenu = menuRepository.select(menu.getId());
		
		persistentMenu.setName( menu.getName() );
		persistentMenu.setAbsoluteName( menu.getAbsoluteName() );
		persistentMenu.setDescription( menu.getDescription() );
		persistentMenu.setIconHtml( menu.getIconHtml() );
		persistentMenu.setExtraHtml( menu.getExtraHtml() );
		persistentMenu.setOrdering( menu.getOrdering() );
		persistentMenu.setRelativeHref( menu.getRelativeHref() );
		persistentMenu.setType( menu.getType() );
		
	}

	@Transactional
	public void save(Menu menu) {
		menuRepository.save(menu);
	}
	

}