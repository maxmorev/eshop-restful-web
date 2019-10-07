package ru.maxmorev.restful.eshop.annotation;

import ru.maxmorev.restful.eshop.validators.CommodityBranchAttributesValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Constraint(validatedBy = CommodityBranchAttributesValidator.class)
@Documented
public @interface CheckCommodityBranchAttributes {
    String message() default "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}