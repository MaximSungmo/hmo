package kr.co.sunnyvale.sunny.exception;

public class NotValidSignupFormException extends SunnyException {

	@Override
	protected String getDefaultMessageSourceString() {
	
		return "err.notValidSignupForm";
	}

}
