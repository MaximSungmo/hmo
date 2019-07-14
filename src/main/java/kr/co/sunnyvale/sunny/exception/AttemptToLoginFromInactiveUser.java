package kr.co.sunnyvale.sunny.exception;

public class AttemptToLoginFromInactiveUser extends SunnyException {

	@Override
	protected String getDefaultMessageSourceString() {
		return "user.inactiveUser";
	}

}
