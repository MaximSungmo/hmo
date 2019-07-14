package kr.co.sunnyvale.sunnycdn.service.exception;

import kr.co.sunnyvale.sunnycdn.exception.ServiceException;

public class MediaServiceException extends ServiceException {
	private static final long serialVersionUID = -2896618972710575499L;
	
	public MediaServiceException( String msg ){
		super( msg );
	}
	
	public MediaServiceException( Exception ex ){
		super( ex );
	}
}
