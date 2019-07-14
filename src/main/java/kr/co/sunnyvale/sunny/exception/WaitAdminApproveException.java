package kr.co.sunnyvale.sunny.exception;

public class WaitAdminApproveException extends SunnyException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3794061733891378218L;

	@Override
	protected String getDefaultMessageSourceString() {
		return "error.inactiveUser.waitAdminApprove";
	}

}
