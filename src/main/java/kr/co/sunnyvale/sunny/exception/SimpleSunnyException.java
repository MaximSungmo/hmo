package kr.co.sunnyvale.sunny.exception;

public class SimpleSunnyException extends SunnyException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9104425422238253732L;

	
	
	public SimpleSunnyException() {
		super();
	}

	public SimpleSunnyException(String messageSourceString) {
		super(messageSourceString);
	}

	@Override
	protected String getDefaultMessageSourceString() {
		return "err.UnknownError";
	}

}
