package kr.co.sunnyvale.sunny.domain.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import kr.co.sunnyvale.sunny.domain.validator.constraints.ValidName;

public class NameValidator implements ConstraintValidator<ValidName, String> {

	private	Pattern pattern = Pattern.compile( "^\\S[^/\\*\\[\\]\\\\{(!#%&$@\\+`\\^:;\\?|=\\-_<>,~')}]{0,20}\\S$" );
	
	public void initialize( ValidName annotation ) {
	}

	public boolean isValid( String value, ConstraintValidatorContext context ) {
		if( value == null || value.length() == 0 ) {
			context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate( "{user.name.Empty}" ).addConstraintViolation();
 			return false;
		}
		Matcher m = pattern.matcher( value );
		return m.matches();
	}
}
