package ru.maxmorev.restful.eshop.validators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.maxmorev.restful.eshop.annotation.CheckAttributeValueDuplicationForType;
import ru.maxmorev.restful.eshop.controllers.request.RequestAttributeValue;
import ru.maxmorev.restful.eshop.entities.CommodityAttribute;
import ru.maxmorev.restful.eshop.entities.CommodityAttributeValue;
import ru.maxmorev.restful.eshop.entities.CommodityType;
import ru.maxmorev.restful.eshop.repos.CommodityTypeRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class CheckAttributeValueDuplicationForTypeValidator implements ConstraintValidator<CheckAttributeValueDuplicationForType, RequestAttributeValue> {

    private static final Logger logger = LoggerFactory.getLogger(CheckAttributeValueDuplicationForTypeValidator.class);
    private CommodityTypeRepository commodityTypeRepository;

    @Autowired
    public void setCommodityTypeRepository(CommodityTypeRepository commodityTypeRepository) {
        this.commodityTypeRepository = commodityTypeRepository;
    }

    @Override
    public boolean isValid(RequestAttributeValue value, ConstraintValidatorContext context) {
        CommodityType type = commodityTypeRepository.findById(value.getTypeId()).get();
        logger.info("CHECK TYPE attributes: " + type);
        if(!Objects.isNull(type)){

            for( CommodityAttribute attribute: type.getAttributes()){
                if(attribute.getName().equals(value.getName())){
                    for(CommodityAttributeValue attributeValue: attribute.getValues()){
                        if(attributeValue.getValue().toString().equals(value.getValue())){
                            return false;
                        }
                    }
                }
            }

        }
        return true;
    }

    @Override
    public void initialize(CheckAttributeValueDuplicationForType constraintAnnotation) {
        //do nothing
    }
}
