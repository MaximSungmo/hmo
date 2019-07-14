package kr.co.sunnyvale.sunny.exception;

public class FeelFailException extends SunnyException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4352074196467666693L;


	@Override
	protected String getDefaultMessageSourceString() {
		return "error.evaluate.fail";
	}

}
