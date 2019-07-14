package kr.co.sunnyvale.sunny.exception;

public class NoExistsTokenException extends SunnyException {

	@Override
	protected String getDefaultMessageSourceString() {
		return "error.authToken.NoExists";
	}

}
