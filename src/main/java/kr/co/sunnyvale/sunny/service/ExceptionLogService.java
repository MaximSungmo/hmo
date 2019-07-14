package kr.co.sunnyvale.sunny.service;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import kr.co.sunnyvale.sunny.domain.ExceptionLog;
import kr.co.sunnyvale.sunny.domain.Site;

public interface ExceptionLogService {
	public void save(ExceptionLog exceptionLog);
	
	public ExceptionLog select(Long logId);
	
	public void delete(ExceptionLog loginLog);
	
	
	public void save(Site site, Exception ex, HttpServletRequest request) throws IOException;
	
}
