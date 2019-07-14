package kr.co.sunnyvale.sunny.exception;


public class SiteNotFoundException extends SunnyException {

	private static final long serialVersionUID = 5258312775914230827L;

	@Override
	protected String getDefaultMessageSourceString() {
		return "err.siteNotFound";
	}
}
