package kr.co.sunnyvale.sunny.mobile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.mobile.device.site.CookieSitePreferenceRepository;
import org.springframework.mobile.device.site.SitePreferenceHandler;
import org.springframework.mobile.device.site.StandardSitePreferenceHandler;
import org.springframework.mobile.device.switcher.MobileSitePathUrlFactory;
import org.springframework.mobile.device.switcher.NormalSitePathUrlFactory;
import org.springframework.mobile.device.switcher.SiteUrlFactory;
import org.springframework.mobile.device.switcher.StandardSiteUrlFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class SiteSwitcherHandlerInterceptor extends HandlerInterceptorAdapter {
	private final SiteUrlFactory normalSiteUrlFactory;
	private final SiteUrlFactory mobileSiteUrlFactory;
	private final SitePreferenceHandler sitePreferenceHandler;
	
	/**
	 * Creates a new site switcher.
	 * @param normalSiteUrlFactory the factory for a "normal" site URL e.g. http://app.com
	 * @param mobileSiteUrlFactory the factory for a "mobile" site URL e.g. http://m.app.com
	 * @param sitePreferenceHandler the handler for the user site preference
	 */
	public SiteSwitcherHandlerInterceptor(SiteUrlFactory normalSiteUrlFactory, SiteUrlFactory mobileSiteUrlFactory, SitePreferenceHandler sitePreferenceHandler) {
		this.normalSiteUrlFactory = normalSiteUrlFactory;
		this.mobileSiteUrlFactory = mobileSiteUrlFactory;
		this.sitePreferenceHandler = sitePreferenceHandler;
	}


	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
//		Device device = DeviceUtils.getRequiredCurrentDevice(request);
//		SitePreference sitePreference = sitePreferenceHandler.handleSitePreference(request, response);
//
//		if ( mobileSiteUrlFactory.isRequestForSite(request) == false && device.isMobile() && sitePreference == SitePreference.MOBILE ) {
//			
//			String queryString = request.getQueryString();
//			if( queryString != null && queryString.contains( "mobile" ) == true ){
//				// prevent mobile redirection
//				return true;
//			}
//			
//			String uri = request.getRequestURI();
//			if( "/".equals( uri ) == false ) {
//				// prevent mobile redirection
//				return true;
//			} 
//
//			String referer = request.getHeader( "referer" );
//			if( referer != null && referer.contains( "m." ) == false ) {
//				// prevent mobile redirection
//				return true;
//			} 
//			
//			// m.yacamp.com redirection
//			response.sendRedirect( response.encodeRedirectURL( mobileSiteUrlFactory.createSiteUrl( request ) ) );
//			return false;
//		} 
		
		// mDot set
		String serverName = request.getServerName();
		request.setAttribute( "mDot", new Boolean( serverName.startsWith( "m.") ) );
//TODO: 테스트용도임. 꼭! 지울 것
//		request.setAttribute( "mDot", new Boolean( serverName.startsWith( "m.") || serverName.startsWith( "192.168.0.11") ) );
//		request.setAttribute( "mDot", true );
		return true;
	}

	
	// static factory methods
	/**
	 * Creates a site switcher that redirects to a <code>m.</code> domain for normal site requests that either
	 * originate from a mobile device or indicate a mobile site preference.
	 * Uses a {@link CookieSitePreferenceRepository} that saves a cookie that is shared between the two domains.
	 */
	public static SiteSwitcherHandlerInterceptor mDot(String serverName) {
		return new SiteSwitcherHandlerInterceptor(new StandardSiteUrlFactory(serverName), new StandardSiteUrlFactory("m." + serverName), new StandardSitePreferenceHandler(new CookieSitePreferenceRepository("." + serverName)));
	}

	/**
	 * Creates a site switcher that redirects to a <code>.mobi</code> domain for normal site requests that either
	 * originate from a mobile device or indicate a mobile site preference.
	 * Will strip off the trailing domain name when building the mobile domain
	 * e.g. "app.com" will become "app.mobi" (the .com will be stripped).
	 * Uses a {@link CookieSitePreferenceRepository} that saves a cookie that is shared between the two domains.
	 */
	public static SiteSwitcherHandlerInterceptor dotMobi(String serverName) {
		int lastDot = serverName.lastIndexOf('.');
		return new SiteSwitcherHandlerInterceptor(new StandardSiteUrlFactory(serverName), new StandardSiteUrlFactory(serverName.substring(0, lastDot) + ".mobi"), new StandardSitePreferenceHandler(new CookieSitePreferenceRepository("." + serverName)));
	}
	
	/**
	 * Creates a site switcher that redirects to a path on the current domain for normal site requests that either
	 * originate from a mobile device or indicate a mobile site preference.
	 * Uses a {@link CookieSitePreferenceRepository} that saves a cookie that is stored on the root path.
	 */
	public static SiteSwitcherHandlerInterceptor urlPath(String mobilePath) {
		return new SiteSwitcherHandlerInterceptor(new NormalSitePathUrlFactory(mobilePath), new MobileSitePathUrlFactory(mobilePath), new StandardSitePreferenceHandler(new CookieSitePreferenceRepository()));
	}
	
	/**
	 * Creates a site switcher that redirects to a path on the current domain for normal site requests that either
	 * originate from a mobile device or indicate a mobile site preference.
	 * Allows you to configure a root path for an application. For example, if your app is running at "http://www.app.com/demoapp",
	 * then the root path is "/demoapp".
	 * Uses a {@link CookieSitePreferenceRepository} that saves a cookie that is stored on the root path.
	 */
	public static SiteSwitcherHandlerInterceptor urlPath(String mobilePath, String rootPath) {
		return new SiteSwitcherHandlerInterceptor(new NormalSitePathUrlFactory(mobilePath, rootPath), new MobileSitePathUrlFactory(mobilePath, rootPath), new StandardSitePreferenceHandler(new CookieSitePreferenceRepository()));
	}
}
