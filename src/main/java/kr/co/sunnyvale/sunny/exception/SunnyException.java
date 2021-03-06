package kr.co.sunnyvale.sunny.exception;

import java.util.Locale;

import javax.annotation.Resource;

import kr.co.sunnyvale.sunny.util.LocaleUtils;

import org.springframework.context.support.ResourceBundleMessageSource;

@Resource
public abstract class SunnyException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8025008063977728641L;

	private ResourceBundleMessageSource messageSource;
	
	public SunnyException(){
		super();
		messageSource = new ResourceBundleMessageSource();
		messageSource.setBasenames("messages_ko");
		this.setMessageSourceString(getDefaultMessageSourceString());
	}

	public SunnyException(String messageSourceString){
		super();
		messageSource = new ResourceBundleMessageSource();
		messageSource.setBasenames("messages_ko");
		this.setMessageSourceString(messageSourceString);
	}
	
	/**
	 * 국제화를 지원하기 위한 메소드. 무조건 구현해서 현재 Exception 메시지의 원본 소스를 넣으세요
	 * 
	 * @param messageSourceString
	 * @author mook
	 */
	protected abstract String getDefaultMessageSourceString();

	
	private String messageSourceString;
	
	@Override
	public String getMessage(){
		if( messageSource == null ){
			System.out.println("DI 가 안됐어 ㅠ.ㅠ");
		}
		return messageSource.getMessage(getMessageSourceString(), null, Locale.KOREAN);
	}
	
	@Override
	public String getLocalizedMessage() {
		if( messageSource == null ){
			System.out.println("DI 가 안됐어 ㅠ.ㅠ");
		}
		return messageSource.getMessage(getMessageSourceString(), null, LocaleUtils.getLocale());
	}
	
	public String getMessageSourceString(){
		return this.messageSourceString;
	}

	protected void setMessageSourceString( String messageSourceString ){
		this.messageSourceString = messageSourceString;
	}

}
