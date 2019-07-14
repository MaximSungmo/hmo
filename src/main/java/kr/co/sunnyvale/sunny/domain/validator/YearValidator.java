package kr.co.sunnyvale.sunny.domain.validator;

import java.util.regex.Matcher;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import kr.co.sunnyvale.sunny.domain.validator.constraints.ValidYear;

public class YearValidator implements ConstraintValidator<ValidYear, String> {
	private java.util.regex.Pattern pattern = java.util.regex.Pattern.compile( "[0-2]?[0-9]?[0-9]?[0-9]" );

	public void initialize( ValidYear annotation ) {
	}

	public boolean isValid( String value, ConstraintValidatorContext context ) {
		if (value == null || value.length() == 0 || value.equals(" ")) {
			return true;
		}
		Matcher m = pattern.matcher( value );
		return m.matches();
	}
}
