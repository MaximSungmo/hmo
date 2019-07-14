package kr.co.sunnyvale.sunnycdn.exception;

@SuppressWarnings("serial")
public class ServiceException extends RuntimeException {
	public ServiceException(String msg){
		super(msg);
	}
	
	public ServiceException(Exception ex){
		super(ex);
	}
}
