package ru.maxmorev.restful.eshop.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.maxmorev.restful.eshop.rest.request.RequestCommodity;
import ru.maxmorev.restful.eshop.entities.Commodity;
import ru.maxmorev.restful.eshop.entities.CommodityBranch;
import ru.maxmorev.restful.eshop.repository.CommodityRepository;
import ru.maxmorev.restful.eshop.repository.CommodityTypeRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.*;

public class CommodityBranchAttributesValidator implements ConstraintValidator<CheckCommodityBranchAttributes, RequestCommodity> {

    private static final Logger logger = LoggerFactory.getLogger(CommodityBranchAttributesValidator.class);

    private CommodityRepository commodityRepository;
    private CommodityTypeRepository commodityTypeRepository;

    @Autowired
    public void setCommodityRepository(CommodityRepository commodityRepository) {
        this.commodityRepository = commodityRepository;
    }
    @Autowired
    public void setCommodityTypeRepository(CommodityTypeRepository commodityTypeRepository) {
        this.commodityTypeRepository = commodityTypeRepository;
    }

    /**
     * In accordance with the business logic we check:
     * if there is already a commodity and a branch with an identical set of attributes -
     * this is considered to be an error of adding a branch to the commodity set of branches
     * - as a result :
     * validation method returns false
     * Otherwise, the method returns true
     * @param value
     * @param context
     * @return
     */
    @Override
    public boolean isValid(RequestCommodity value, ConstraintValidatorContext context) {
        Optional<Commodity> commodityExist = commodityRepository.findByNameAndType(value.getName(), commodityTypeRepository.findById(value.getTypeId()).get() );
        if(commodityExist.isPresent()) {
            /*
            if there is a branch with identical set of properties
            if exist - send message, else create branch
            */
            List<Long> values = value.getPropertyValues();
            //Collections.sort(values);
            List<CommodityBranch> branches = commodityExist.get().getBranches();
            boolean eq;
             //check only if add new branch

                for(CommodityBranch branch: branches){
                    if(values.size()==branch.getAttributeSet().size() && (value.getBranchId()==null?true:!value.getBranchId().equals(branch.getId()))){
                        //check values
                        Set<Long> branchValSet = new HashSet<>();
                        branch.getAttributeSet().forEach(propertySet->branchValSet.add(propertySet.getAttributeValue().getId()));
                        eq = true;
                        for(Long v:values){
                            if(branchValSet.add(v)){
                                eq = false;
                                break;
                            }
                        }
                        if(eq){ //there is a branch with identical set of attributes values
                            return false;
                        }

                    }
                }

        }
        return true;
    }

    @Override
    public void initialize(CheckCommodityBranchAttributes constraintAnnotation) {
        //do nothing
    }
}
