package kr.co.sunnyvale.sunny.service.impl;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import kr.co.sunnyvale.sunny.domain.ExceptionLog;
import kr.co.sunnyvale.sunny.domain.Site;
import kr.co.sunnyvale.sunny.domain.extend.SecurityUser;
import kr.co.sunnyvale.sunny.repository.hibernate.ExceptionLogRepository;
import kr.co.sunnyvale.sunny.service.ExceptionLogService;
import kr.co.sunnyvale.sunny.service.SiteService;
import kr.co.sunnyvale.sunny.util.RequestUtils;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service( value="exceptionLogService" )
@Transactional
public class ExceptionLogServiceImpl implements ExceptionLogService {

	@Autowired
	private ExceptionLogRepository exceptionLogRepository;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private SiteService siteService;
	
	@Transactional
	public void save(ExceptionLog exceptionLog) {
		exceptionLogRepository.save(exceptionLog);
	}
	
	@Transactional(readOnly = true)
	public ExceptionLog select(Long logId) {
		return exceptionLogRepository.select(logId);
	}

	@Transactional
	public void delete(ExceptionLog loginLog) {
		exceptionLogRepository.delete(loginLog);
	}

	@Transactional
	public void save(Site site, Exception ex, HttpServletRequest request) throws IOException {
		
		ExceptionLog exceptionLog = new ExceptionLog();

		//Site site = siteService.getSiteFromDomain( RequestUtils.getCurrentServerName() );
		//exceptionLog.setSiteId(site.getId());
		
		if( SecurityContextHolder.getContext().getAuthentication() != null ){
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (principal != null && principal.getClass() != String.class) {
				SecurityUser securityUser = (SecurityUser) principal;
				exceptionLog.setUserId(securityUser.getUserId());
			} 
		}
		
		
		exceptionLog.setRequestDump(RequestUtils.getRequestDump(request));
		exceptionLog.setExceptionMessage(ex.getMessage());
		exceptionLog.setStackTrace( ExceptionUtils.getStackTrace(ex));
		
		exceptionLogRepository.save(exceptionLog);
		
	}

}
