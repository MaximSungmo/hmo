package kr.co.sunnyvale.sunny.exception;

public class UnmatchTypeException extends SunnyException {

	@Override
	protected String getDefaultMessageSourceString() {
		return "err.unmatchType";
	}

}
