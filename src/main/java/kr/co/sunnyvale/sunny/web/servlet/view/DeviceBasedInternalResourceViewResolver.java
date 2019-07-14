package kr.co.sunnyvale.sunny.web.servlet.view;

import java.util.Locale;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.view.AbstractUrlBasedView;
import org.springframework.web.servlet.view.InternalResourceView;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

public class DeviceBasedInternalResourceViewResolver extends InternalResourceViewResolver {
	@Override
	protected AbstractUrlBasedView buildView( String viewName ) throws Exception {
		
		RequestAttributes attributes = RequestContextHolder.currentRequestAttributes();
		Boolean isMDot = (Boolean) attributes.getAttribute("mDot", 0);
		
		viewName = ( null != isMDot && isMDot ) ? getMobileViewName( viewName ) :  getDesktopViewName( viewName );
		return  (InternalResourceView) super.buildView( viewName );
	}

	@Override
	protected Object getCacheKey(String viewName, Locale locale) {
		
		RequestAttributes attributes = RequestContextHolder.currentRequestAttributes();
		Boolean isMDot = (Boolean) attributes.getAttribute("mDot", 0);
		
		return super.getCacheKey( ( null != isMDot && isMDot ) ? getMobileViewName( viewName ) : getDesktopViewName( viewName ), locale );
	}
	
	private String getMobileViewName( String viewName ) {
//		System.out.println( ":============> /mobile" + viewName );
		return ( "/mobile" + viewName );
	}
	
	private String getDesktopViewName( String viewName ) {
//		System.out.println( ":============> /desktop" + viewName );
		return ( "/desktop" + viewName );
	}	

}
