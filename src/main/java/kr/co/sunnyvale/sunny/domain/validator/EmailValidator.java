package kr.co.sunnyvale.sunny.domain.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import kr.co.sunnyvale.sunny.domain.validator.constraints.ValidEmail;

public class EmailValidator implements ConstraintValidator<ValidEmail, String> {
	private Pattern pattern = Pattern.compile( "(([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5}){1,25})+([;.](([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5}){1,25})+)*" );

	public void initialize( ValidEmail annotation ) {
	}

	public boolean isValid( String value, ConstraintValidatorContext context ) {
		if( value == null || value.length() == 0 ) {
			context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate( "{com.yacamp.user.email.Empty}" ).addConstraintViolation();
 			return false;
		}
		
		Matcher m = pattern.matcher( value );
		return m.matches();
	}
}
