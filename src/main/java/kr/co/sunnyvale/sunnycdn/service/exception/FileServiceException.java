package kr.co.sunnyvale.sunnycdn.service.exception;

import kr.co.sunnyvale.sunnycdn.exception.ServiceException;

public class FileServiceException extends ServiceException {
	private static final long serialVersionUID = -2896618972710575499L;
	
	public FileServiceException( String msg ){
		super( msg );
	}
	
	public FileServiceException( Exception ex ){
		super( ex );
	}
}
