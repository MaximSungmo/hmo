package kr.co.sunnyvale.sunny.exception;

public class SendEmailException extends SunnyException {

	@Override
	protected String getDefaultMessageSourceString() {
		return "err.sendEmailException";
	}

}
