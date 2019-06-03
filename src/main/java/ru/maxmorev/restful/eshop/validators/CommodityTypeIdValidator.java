package ru.maxmorev.restful.eshop.validators;

import org.springframework.beans.factory.annotation.Autowired;
import ru.maxmorev.restful.eshop.annotation.CheckCommodityTypeId;
import ru.maxmorev.restful.eshop.controllers.request.RequestAttributeValue;
import ru.maxmorev.restful.eshop.repos.CommodityTypeRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CommodityTypeIdValidator implements ConstraintValidator<CheckCommodityTypeId, RequestAttributeValue> {

    private CommodityTypeRepository commodityTypeRepository;

    @Autowired public void setCommodityTypeRepository(CommodityTypeRepository commodityTypeRepository) {
        this.commodityTypeRepository = commodityTypeRepository;
    }

    @Override
    public boolean isValid(RequestAttributeValue value, ConstraintValidatorContext context) {
        return commodityTypeRepository.existsById(value.getTypeId());

    }

    @Override
    public void initialize(CheckCommodityTypeId constraintAnnotation) {
        //do nothing
    }
}
