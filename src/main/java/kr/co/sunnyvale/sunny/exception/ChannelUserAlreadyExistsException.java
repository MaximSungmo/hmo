package kr.co.sunnyvale.sunny.exception;

public class ChannelUserAlreadyExistsException extends SunnyException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1141791033154003552L;

	@Override
	protected String getDefaultMessageSourceString() {
		return "error.chat.user.exist";
	}

}
