package kr.co.sunnyvale.sunny.exception;

public class NotCorrectPasswordException extends SunnyException {

	@Override
	protected String getDefaultMessageSourceString() {
		return "err.notcorrectPassword";
	}

}
