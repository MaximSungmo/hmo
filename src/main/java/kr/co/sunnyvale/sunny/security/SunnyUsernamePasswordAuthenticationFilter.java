package kr.co.sunnyvale.sunny.security;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * {SpringSecurity}
 * 
 * <p>
 * 이 필터는 로그인 처리 URL에 대한 감시를 한다. Spring Securoty에서는 이 URL에 대한 실제 구현은 필요없다 (가상 URL)
 * 메서드 requiresAuthentication가 이 역활을 하며, 보통 기본 필터 구현체가 이 역활을 충분히 하며 주입시 파라미터(어트리)세팅으로
 * 로그인 URL를 지정해준다.
 * 
 * <p>
 * 하지만, 기본 필터는 로그인처리 URL를 하나만 지원하기 때문에 여러개를 지원하기 위해 상속하여 재구현 
 *  
 * <p>
 * 멀티 태넌트 ( 멀티도메인네임 - 멀티데이터소스) 지원을 위해 수정 
 *  
 * 
 * @see org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter#requiresAuthentication(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
 * @author kickscar
 *
 */
public class SunnyUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private List<String> filterProcessesUrls = null;
    
	@Override
	protected boolean requiresAuthentication( HttpServletRequest request, HttpServletResponse response ) {
		
//		MultiDomainSessionFactoryBean.setDomainName( request.getServerName() );
		
		if( filterProcessesUrls == null || filterProcessesUrls.size() == 0 ) {
			return super.requiresAuthentication( request, response );
		}
		
        String uri = request.getRequestURI();
        int pathParamIndex = uri.indexOf(';');

        if (pathParamIndex > 0) {
            uri = uri.substring( 0, pathParamIndex );
        }

        String contextPath = request.getContextPath();
        
        for( String filterProcessesUrl : filterProcessesUrls ) {
        	if( "".equals( contextPath ) && uri.endsWith( filterProcessesUrl ) ) {
        		return true ;
        	}
        	
        	if( uri.endsWith( contextPath + filterProcessesUrl ) ) {
        		return true;
        	}
        }

        return super.requiresAuthentication( request, response );
	}
    
    public List<String> getFilterProcessesUrls() {
		return filterProcessesUrls;
	}

    public void setFilterProcessesUrls(List<String> filterProcessesUrls) {
		this.filterProcessesUrls = filterProcessesUrls;
	}
}
