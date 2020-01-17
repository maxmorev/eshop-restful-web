package ru.maxmorev.restful.eshop.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.maxmorev.restful.eshop.rest.request.RequestAttributeValue;
import ru.maxmorev.restful.eshop.entities.CommodityAttribute;
import ru.maxmorev.restful.eshop.entities.CommodityType;
import ru.maxmorev.restful.eshop.repository.CommodityAttributeRepository;
import ru.maxmorev.restful.eshop.repository.CommodityTypeRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;
import java.util.Optional;

public class CheckAttributeValueDuplicationForTypeValidator implements ConstraintValidator<CheckAttributeValueDuplicationForType, RequestAttributeValue> {

    private static final Logger logger = LoggerFactory.getLogger(CheckAttributeValueDuplicationForTypeValidator.class);
    private CommodityTypeRepository commodityTypeRepository;
    private CommodityAttributeRepository commodityAttributeRepository;

    @Autowired public void setCommodityTypeRepository(CommodityTypeRepository commodityTypeRepository) {
        this.commodityTypeRepository = commodityTypeRepository;
    }

    @Autowired public void setCommodityAttributeRepository(CommodityAttributeRepository commodityAttributeRepository) {
        this.commodityAttributeRepository = commodityAttributeRepository;
    }

    @Override
    public boolean isValid(RequestAttributeValue value, ConstraintValidatorContext context) {
        CommodityType type = commodityTypeRepository.findById(value.getTypeId()).get();
        logger.info("CHECK TYPE attributes: " + type);
        if(!Objects.isNull(type)){
            Optional<CommodityAttribute> ca = commodityAttributeRepository.findByNameAndCommodityType(value.getName(), type);
            if(ca.isPresent()){
                CommodityAttribute attribute = ca.get();
                if(attribute.getValues().stream().anyMatch(v->v.getValue().toString().equals(value.getValue()))){
                    return false;
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
