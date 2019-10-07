package ru.maxmorev.restful.eshop.annotation;

import ru.maxmorev.restful.eshop.validators.CheckAttributeValueDuplicationForTypeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Constraint(validatedBy = CheckAttributeValueDuplicationForTypeValidator.class)
@Documented
public @interface CheckAttributeValueDuplicationForType {
    String message() default "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
