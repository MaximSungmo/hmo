package kr.co.sunnyvale.sunny.exception;

public class PageNotFoundException extends SunnyException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1802691170962923483L;

	@Override
	protected String getDefaultMessageSourceString() {
		return "err.pageNotFound";
	}

}
