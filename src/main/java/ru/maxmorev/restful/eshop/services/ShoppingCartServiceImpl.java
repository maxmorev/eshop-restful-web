package ru.maxmorev.restful.eshop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.maxmorev.restful.eshop.entities.CommodityBranch;
import ru.maxmorev.restful.eshop.entities.ShoppingCart;
import ru.maxmorev.restful.eshop.entities.ShoppingCartSet;
import ru.maxmorev.restful.eshop.repos.ShoppingCartRepository;
import ru.maxmorev.restful.eshop.repos.ShoppingCartSetRepository;

import java.util.Objects;
import java.util.Optional;

@Service("shoppingCartService")
@Transactional
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private ShoppingCartRepository shoppingCartRepository;
    private ShoppingCartSetRepository shoppingCartSetRepository;

    private CommodityService commodityService;

    @Autowired
    public void setCommodityService(CommodityService commodityService) {
        this.commodityService = commodityService;
    }

    @Autowired
    public void setShoppingCartRepository(ShoppingCartRepository shoppingCartRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
    }
    @Autowired
    public void setShoppingCartSetRepository(ShoppingCartSetRepository shoppingCartSetRepository) {
        this.shoppingCartSetRepository = shoppingCartSetRepository;
    }

    @Override
    public ShoppingCart createEmptyShoppingCart() {
        ShoppingCart newCart = new ShoppingCart();
        shoppingCartRepository.save(newCart);
        return newCart;
    }

    @Override
    @Transactional(readOnly = true)
    public ShoppingCart findShoppingCartById(Long id) {
        Optional<ShoppingCart> cart = shoppingCartRepository.findById(id);
        if(cart.isPresent()){
            return cart.get();
        }
        return null;
    }

    @Override
    public boolean addToShoppingCartSet(ShoppingCartSet shoppingCartSet) {

        if(Objects.isNull(shoppingCartSet.getAmount()) || shoppingCartSet.getAmount()==0){
            throw new IllegalArgumentException("Illegal argument amount="+shoppingCartSet.getAmount());
        }
        CommodityBranch branch = commodityService.findBranchById(shoppingCartSet.getBranchId());
        if(Objects.isNull(branch)){
            throw new IllegalArgumentException("Illegal argument branchId="+shoppingCartSet.getBranchId());
        }
        ShoppingCart cart = this.findShoppingCartById(shoppingCartSet.getShoppingCartId());
        if(Objects.isNull(cart)){
            throw new IllegalArgumentException("Illegal argument shoppingCartId="+shoppingCartSet.getShoppingCartId());
        }

        if( shoppingCartSet.getAmount() > branch.getAmount() ){
            throw new IllegalArgumentException( "amount =" + shoppingCartSet.getAmount() +" Must be less than or equal to the branch.amount=" + branch.getAmount() );
        }
        if(Objects.nonNull(cart.getShoppingSet())) {
            Optional<ShoppingCartSet> setExist = cart.getShoppingSet().stream().filter(set -> set.getBranchId().equals(branch.getId())).findFirst();
            if (setExist.isPresent()) {
                if (setExist.get().getAmount() + shoppingCartSet.getAmount() > branch.getAmount()) {
                    return false;
                }
                ShoppingCartSet set = setExist.get();
                set.setAmount(set.getAmount() + shoppingCartSet.getAmount());
                shoppingCartSetRepository.save(set);
                return true;
            }
        }
        shoppingCartSetRepository.save(shoppingCartSet);
        return true;
    }
}
