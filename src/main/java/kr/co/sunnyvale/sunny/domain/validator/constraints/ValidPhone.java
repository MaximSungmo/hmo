package kr.co.sunnyvale.sunny.domain.validator.constraints;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import kr.co.sunnyvale.sunny.domain.validator.PhonedValidator;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhonedValidator.class)
public @interface ValidPhone {
	String message() default "{com.yacamp.user.phone.NotValid}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
