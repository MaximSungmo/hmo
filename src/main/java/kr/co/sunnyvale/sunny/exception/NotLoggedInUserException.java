package kr.co.sunnyvale.sunny.exception;

public class NotLoggedInUserException extends SunnyException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -54501178476361193L;

	@Override
	protected String getDefaultMessageSourceString() {
		return "user.NotLoggedIn";
	}


}
