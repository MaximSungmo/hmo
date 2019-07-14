package kr.co.sunnyvale.sunny.exception;

public class NoExistsUserException extends SunnyException {

	@Override
	protected String getDefaultMessageSourceString() {
		return "user.email.NoExists";
	}

}
