package ru.maxmorev.restful.eshop.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.maxmorev.restful.eshop.controllers.CommodityAttributeController;
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

    private static final Logger logger = LoggerFactory.getLogger(ShoppingCartServiceImpl.class);

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

    private void isValidShoppingCartSet(ShoppingCartSet shoppingCartSet){
        if(Objects.isNull(shoppingCartSet)){
            throw new IllegalArgumentException("Illegal argument: ShoppingCartSet is null");
        }
        if(Objects.isNull(shoppingCartSet.getAmount()) || shoppingCartSet.getAmount()<=0){
            throw new IllegalArgumentException("Illegal argument amount="+shoppingCartSet.getAmount());
        }

        if(Objects.isNull(shoppingCartSet.getBranch())){
            throw new IllegalArgumentException("Illegal argument branch="+shoppingCartSet.getBranch());
        }

        if(Objects.isNull(shoppingCartSet.getShoppingCart())){
            throw new IllegalArgumentException("Illegal argument shoppingCart="+shoppingCartSet.getShoppingCart());
        }
    }

    @Override
    public ShoppingCart addToShoppingCartSet(ShoppingCartSet shoppingCartSet, Integer amount) {

        isValidShoppingCartSet(shoppingCartSet);
        logger.info("======================================");
        logger.info("addToShoppingCartSet : " + shoppingCartSet);
        CommodityBranch branch = shoppingCartSet.getBranch();
        ShoppingCart cart = shoppingCartSet.getShoppingCart();

        if( shoppingCartSet.getAmount()+amount > branch.getAmount() ){
            return cart;
            //throw new IllegalArgumentException( "amount =" + shoppingCartSet.getAmount() +" Must be less than or equal to the branch.amount=" + branch.getAmount() );
        }
        shoppingCartSet.setAmount(shoppingCartSet.getAmount() + amount);
        shoppingCartRepository.save(cart);
        return cart;
    }

    @Override
    public ShoppingCart removeFromShoppingCartSet(ShoppingCartSet shoppingCartSet, Integer amount) {
        isValidShoppingCartSet(shoppingCartSet);
        logger.info("======================================");
        logger.info("removeFromShoppingCartSet : "+ shoppingCartSet);
        CommodityBranch branch = shoppingCartSet.getBranch();
        ShoppingCart cart = shoppingCartSet.getShoppingCart();

        //Optional<ShoppingCartSet> setExist = shoppingCartSetRepository.findByBranchAndShoppingCart(branch, cart);
        if (shoppingCartSet.getAmount() - amount <=0) {
            //remove set from cart
            cart.getShoppingSet().remove(shoppingCartSet);
        }else{
            shoppingCartSet.setAmount(shoppingCartSet.getAmount() - amount);
        }
        //update set cart
        shoppingCartRepository.save(cart);
        return cart;
    }

    @Override
    public ShoppingCartSet findByBranchAndShoppingCart(CommodityBranch branch, ShoppingCart cart) {
        Optional<ShoppingCartSet> oSCS = shoppingCartSetRepository.findByBranchAndShoppingCart(branch, cart);
        if(oSCS.isPresent()){
            return  oSCS.get();
        }else {
            return null;
        }
    }
}
