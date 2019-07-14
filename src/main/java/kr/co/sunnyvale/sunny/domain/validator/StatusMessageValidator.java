package kr.co.sunnyvale.sunny.domain.validator;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import kr.co.sunnyvale.sunny.domain.validator.constraints.ValidStatusMessage;

public class StatusMessageValidator implements ConstraintValidator<ValidStatusMessage, String> {
	
	private Pattern pattern = Pattern.compile( "[.*]{0,60}" );
	
	public void initialize( ValidStatusMessage annotation ) {
	}

	public boolean isValid( String value, ConstraintValidatorContext context ) {
		if( value == null ){
			return true;
		}
		if( value != null && value.length() > 60 ) {
			context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate( "{com.yacamp.user.statusMessage.toolong}" ).addConstraintViolation();
 			return false;
		}
		
		//Matcher m = pattern.matcher( value );
		//return m.matches();
		return true;
	}

}
