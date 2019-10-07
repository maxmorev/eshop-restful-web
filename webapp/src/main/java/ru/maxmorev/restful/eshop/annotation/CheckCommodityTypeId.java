package ru.maxmorev.restful.eshop.annotation;

import ru.maxmorev.restful.eshop.validators.CommodityTypeIdValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Constraint(validatedBy = CommodityTypeIdValidator.class)
@Documented
public @interface CheckCommodityTypeId {
    String message() default "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
