package kr.co.sunnyvale.sunny.domain.validator;

import java.util.regex.Matcher;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import kr.co.sunnyvale.sunny.domain.validator.constraints.ValidSex;

public class SexValidator implements ConstraintValidator<ValidSex, String> {
	private java.util.regex.Pattern pattern = java.util.regex.Pattern.compile( "male|female|none" );

	public void initialize( ValidSex annotation ) {
	}

	public boolean isValid( String value, ConstraintValidatorContext context ) {
		if (value == null || value.length() == 0 || value.equals(" ")) {
			return true;
		}
		Matcher m = pattern.matcher( value );
		return m.matches();
	}
}
