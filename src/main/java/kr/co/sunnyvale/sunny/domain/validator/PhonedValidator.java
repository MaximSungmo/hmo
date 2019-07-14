package kr.co.sunnyvale.sunny.domain.validator;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import kr.co.sunnyvale.sunny.domain.validator.constraints.ValidPhone;

public class PhonedValidator implements ConstraintValidator<ValidPhone, String> {
	
	private Pattern pattern = Pattern.compile( "^\\S[\\s0-9-]{7,13}\\S$" );
	
	public void initialize( ValidPhone annotation ) {
	}

	public boolean isValid( String value, ConstraintValidatorContext context ) {
		if( value == null || value.length() == 0 ) {
			return true;
		}

//		if(pattern.matcher( value ).matches() == false){
//			context.disableDefaultConstraintViolation();
//            context.buildConstraintViolationWithTemplate( "{com.yacamp.user.phone.Empty}" ).addConstraintViolation();
// 			return false;
//		}
//		
//		// 번호내 공백문자, - 제거  
//		value = value.replaceAll( "\\s", "" ).replaceAll( "-", "" );
//		int length = value.length();
//		if( length < 9 || length > 11 ){
//			return false;
//		}
		
		return true;
	}
}
