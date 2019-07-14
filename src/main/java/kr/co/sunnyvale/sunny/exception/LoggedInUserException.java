package kr.co.sunnyvale.sunny.exception;

public class LoggedInUserException extends SunnyException {

	@Override
	protected String getDefaultMessageSourceString() {

		return "user.LoggedIn";
	}

}
