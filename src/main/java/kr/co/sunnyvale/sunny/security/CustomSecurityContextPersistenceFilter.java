package kr.co.sunnyvale.sunny.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.security.web.context.SecurityContextRepository;

public class CustomSecurityContextPersistenceFilter extends
		SecurityContextPersistenceFilter {

	@Override
	public void setForceEagerSessionCreation(boolean forceEagerSessionCreation) {
		System.out.println("생성합시다");
		// TODO Auto-generated method stub
		super.setForceEagerSessionCreation(forceEagerSessionCreation);
	}


	public CustomSecurityContextPersistenceFilter(){
		super(new CustomHttpSessionSecurityContextRepository());
		System.out.println("생성합시다");
	}
	
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

		System.out.println("필터 들어갑니다잉");
		super.doFilter(req, res, chain);
    }
}
