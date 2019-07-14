package kr.co.sunnyvale.sunny.exception;

public class FriendException extends SunnyException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5386302708155159146L;

	public FriendException(String dataSource){
		super(dataSource);
	}
	
	public FriendException() {
		super();
	}

	@Override
	protected String getDefaultMessageSourceString() {
		return "error.friendRequest";
	}

}
