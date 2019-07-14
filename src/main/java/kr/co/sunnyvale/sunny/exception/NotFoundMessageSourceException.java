package kr.co.sunnyvale.sunny.exception;

public class NotFoundMessageSourceException extends SunnyException {

	private static final long serialVersionUID = 1884549593894671635L;

	@Override
	protected String getDefaultMessageSourceString() {
		return "err.notFoundMessageSource";
	}

}
