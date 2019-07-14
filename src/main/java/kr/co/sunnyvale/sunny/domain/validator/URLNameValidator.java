package kr.co.sunnyvale.sunny.domain.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import kr.co.sunnyvale.sunny.domain.validator.constraints.ValidURLName;
import kr.co.sunnyvale.sunny.util.KeywordUtil;

public class URLNameValidator implements ConstraintValidator< ValidURLName, String > {
	
	private Pattern pattern = java.util.regex.Pattern.compile( "[\\.A-Za-z0-9]{3,12}" );
	
	public void initialize( ValidURLName annotation ) {
	}

	public boolean isValid( String value, ConstraintValidatorContext context ) {
		// 없으면, 발리데이션 생략
		if ( value == null || value.length() == 0 ) {
			return true;
		}
		
		Matcher m = pattern.matcher( value );
		if( m.matches() == false ) {
			return false;
		}
		
		if( KeywordUtil.blockUrlNames.contains(value) ){
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate( "{com.yacamp.user.urlName.block}").addConstraintViolation();
			return false;
		}
		
		// numeric not permit
		try {
			Long.parseLong( value );
		} catch ( NumberFormatException e ) {
		    // s is not numeric
			return true;
		}
		
		return true;
	}
}
