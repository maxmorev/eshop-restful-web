package ru.maxmorev.restful.eshop.validation;

import org.springframework.beans.factory.annotation.Autowired;
import ru.maxmorev.restful.eshop.repository.CommodityBranchRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CheckBranchIdValidator implements ConstraintValidator<CheckBranchId, Long> {

    private final CommodityBranchRepository cbr;

    public CheckBranchIdValidator(@Autowired CommodityBranchRepository repo){
        this.cbr=repo;
    }

    @Override
    public void initialize(CheckBranchId constraintAnnotation) {
        //do nothing
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        return cbr.existsById(value);
    }
}
