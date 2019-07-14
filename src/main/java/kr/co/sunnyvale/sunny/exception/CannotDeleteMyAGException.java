package kr.co.sunnyvale.sunny.exception;


public class CannotDeleteMyAGException extends SunnyException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4111019318529575635L;

	/**
	 * 
	 */

	@Override
	protected String getDefaultMessageSourceString() {
		return "error.accessGroup.deleteMyAG";
	}
}
