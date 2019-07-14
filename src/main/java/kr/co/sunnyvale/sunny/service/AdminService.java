package kr.co.sunnyvale.sunny.service;



public interface AdminService {

	public void generateDefaults();

//	public void removeSiteFromAdminId(Long userId);

	public void removeSite(Long siteId);

	public void syncMenus();

	public void syncTags();
	
}
