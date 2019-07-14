package kr.co.sunnyvale.sunnycdn.exception;

@SuppressWarnings("serial")
public class DaoException extends RuntimeException {
	public DaoException(String msg){
		super(msg);
	}
	
	public DaoException(Exception ex){
		super(ex);
	}
}
