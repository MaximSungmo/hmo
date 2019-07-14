package kr.co.sunnyvale.sunny.exception;


public class AlreadyJoinedException extends SunnyException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1298065307982040723L;

	@Override
	protected String getDefaultMessageSourceString() {
		return "error.evaluate.already";
	}
	

}
