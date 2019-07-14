package kr.co.sunnyvale.sunnycdn.service.exception;

import kr.co.sunnyvale.sunnycdn.exception.ServiceException;

public class RetouchServiceException extends ServiceException {
	private static final long serialVersionUID = -2896618972710575499L;
	
	public RetouchServiceException( String msg ){
		super( msg );
	}
	
	public RetouchServiceException( Exception ex ){
		super( ex );
	}
}
