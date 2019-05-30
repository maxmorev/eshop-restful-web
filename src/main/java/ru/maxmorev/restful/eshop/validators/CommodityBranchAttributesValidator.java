package ru.maxmorev.restful.eshop.validators;

import org.springframework.beans.factory.annotation.Autowired;
import ru.maxmorev.restful.eshop.annotation.CheckCommodityBranchAttributes;
import ru.maxmorev.restful.eshop.controllers.request.RequestCommodity;
import ru.maxmorev.restful.eshop.entities.Commodity;
import ru.maxmorev.restful.eshop.entities.CommodityBranch;
import ru.maxmorev.restful.eshop.repos.CommodityRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.*;

public class CommodityBranchAttributesValidator implements ConstraintValidator<CheckCommodityBranchAttributes, RequestCommodity> {

    private CommodityRepository commodityRepository;

    @Autowired
    public void setCommodityRepository(CommodityRepository commodityRepository) {
        this.commodityRepository = commodityRepository;
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
        Optional<Commodity> commodityExist = commodityRepository.findByNameAndTypeId(value.getName(), value.getTypeId());
        if(commodityExist.isPresent()) {
            /*
            TODO check: if there is a branch with identical set of properties
            if exist - send message, else create branch
            */
            List<Long> values = value.getPropertyValues();
            //Collections.sort(values);
            List<CommodityBranch> branches = commodityExist.get().getBranches();
            boolean eq;
            for(CommodityBranch branch: branches){
                if(values.size()==branch.getPropertySet().size()){
                    //check values
                    Set<Long> branchValSet = new HashSet<>();
                    branch.getPropertySet().forEach(propertySet->branchValSet.add(propertySet.getAttributeValueId()));
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

    }
}