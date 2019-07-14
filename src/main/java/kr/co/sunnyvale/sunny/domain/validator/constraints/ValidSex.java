package kr.co.sunnyvale.sunny.domain.validator.constraints;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import kr.co.sunnyvale.sunny.domain.validator.SexValidator;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SexValidator.class)
public @interface ValidSex {
	String message() default "{kr.co.sunnyvale.yacamp.domain.validator.ValidSex.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}