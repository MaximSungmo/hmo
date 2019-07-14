package kr.co.sunnyvale.sunny.domain.validator;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import kr.co.sunnyvale.sunny.domain.validator.constraints.ValidPassword;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

	private Pattern patternType = Pattern.compile( "[^\\s]{4,}" );																	
	private Pattern patternUpperCase = Pattern.compile( "[A-Z]" );																	
	private Pattern patternLowerCase = Pattern.compile( "[a-z]" );														
	private Pattern patternLetters = Pattern.compile( "[a-zA-Z]" );														
	private Pattern patternNumbers = Pattern.compile( "[0-9]" );														
	private Pattern patternLeastOneNumber = Pattern.compile( "\\d+" );										
	private Pattern patternLeastThreeNumbers = Pattern.compile( "(.*[0-9].*[0-9].*[0-9])" );										
	private Pattern patternLeastOneSpecialCharacter = Pattern.compile( ".[!,@,#,$,%,^,&,*,?,_,~]" );								
	private Pattern patternLeastTwoSpecialCharacter = Pattern.compile( "(.*[!,@,#,$,%,^,&,*,?,_,~].*[!,@,#,$,%,^,&,*,?,_,~])" );	
	private Pattern patternComboCase = Pattern.compile( "([a-z].*[A-Z])|([A-Z].*[a-z])" );
	private Pattern patternComboLetterNumberSpecialCharacter = Pattern.compile( "([a-zA-Z0-9].*[!,@,#,$,%,^,&,*,?,_,~])|([!,@,#,$,%,^,&,*,?,_,~].*[a-zA-Z0-9])" );
	
	
	public void initialize( ValidPassword annotation ) {
	}

	public boolean isValid( String value, ConstraintValidatorContext context ) {
		
		
		if( value == null || value.isEmpty() ){
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate( "{user.password.Empty}" ).addConstraintViolation();
			return false;
		}
		if( value.length() < 4 || patternType.matcher( value ).matches() == false ) {
 			return false;
		}
//		
		return true;
		//////////////////////////////////////////// 아래는 추후 하기로 하고 우선 4글자 넘는지만 검사
		
		
//		if( patternType.matcher( value ).matches() == false ) {
//			context.disableDefaultConstraintViolation();
//            context.buildConstraintViolationWithTemplate( "{com.yacamp.user.password.NotValid}" ).addConstraintViolation();
// 			return false;
//		}
//		
		
		
		//
		// test password
		// Copyright (c) 2006 Steve Moitozo <god at zilla dot us>
		//
//		int score = 0;
//		int length = value.length();
//		
//		if( length < 7 ) { //length 6 or less
//			score += 3;							
//		} else if ( length < 8 ) { // length between 6 and 7
//			score += 6;				
//		} else if ( length < 15 ) { // length between 8 and 15
//			score += 12;				
//		} else {
//			score += 18;				
//		}
//
//		if (  patternLowerCase.matcher( value ).find() == true ) { // [verified] at least one upper case letter
//			score += 1;
//		}
//		
//		if ( patternUpperCase.matcher( value ).find() == true ) { // [verified] at least one lower case letter
//			score += 5;
//		}
//		
//		// NUMBERS
//		if ( patternLeastOneNumber.matcher( value ).find() == true ) { // [verified] at least one number
//			score += 5;
//		}
//		if ( patternLeastThreeNumbers.matcher( value ).find() == true ) { // [verified] at least three numbers
//			score += 5;
//		}
//		// SPECIAL CHAR
//		if ( patternLeastOneSpecialCharacter.matcher( value ).find() == true ) { // [verified] at least one special character
//			score += 5;
//		}
//		if ( patternLeastTwoSpecialCharacter.matcher( value ).find() == true ) { // [verified] at least two special characters
//			score += 5;
//		}
//		// COMBOS
//		if ( patternComboCase.matcher( value ).find() == true ) { // [verified] both upper and lower case
//			score += 2;
//		}
//		if ( patternLetters.matcher( value ).find() == true && patternNumbers.matcher( value ).find() ) { // [verified] both letters and numbers
//			score += 2;
//		}
//		if ( patternComboLetterNumberSpecialCharacter.matcher( value ).find() == true ) { // [verified] letters, numbers, and special characters
//			score += 2;
//		}
//
//		
//		context.disableDefaultConstraintViolation();
//
//		if ( score < 16 ) {
//            context.buildConstraintViolationWithTemplate( "{com.yacamp.user.email.SecureLevel1}" ).addConstraintViolation();
//			return false;
//		} else if ( score < 25 ){
//            context.buildConstraintViolationWithTemplate( "{com.yacamp.user.email.SecureLevel2}" ).addConstraintViolation();
//			return false;
//		} else if ( score < 35 ) {
//            context.buildConstraintViolationWithTemplate( "{com.yacamp.user.email.SecureLevel3}" ).addConstraintViolation();
//		} else if ( score < 45 ) {
//            context.buildConstraintViolationWithTemplate( "{com.yacamp.user.email.SecureLevel4}" ).addConstraintViolation();
//		} else {
//            context.buildConstraintViolationWithTemplate( "{com.yacamp.user.email.SecureLevel5}" ).addConstraintViolation();
//		}		
//		
//		return true;
	}
}
