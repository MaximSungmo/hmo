package kr.co.sunnyvale.sunnycdn.service.exception;

import kr.co.sunnyvale.sunnycdn.exception.ServiceException;

public class ExifServiceException extends ServiceException {
	private static final long serialVersionUID = -2896618972710575499L;
	
	public ExifServiceException( String msg ){
		super( msg );
	}
	
	public ExifServiceException( Exception ex ){
		super( ex );
	}
}
