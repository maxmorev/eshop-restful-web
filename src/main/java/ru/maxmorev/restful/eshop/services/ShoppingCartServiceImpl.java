package ru.maxmorev.restful.eshop.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.maxmorev.restful.eshop.config.ShoppingCartConfig;
import ru.maxmorev.restful.eshop.entities.CommodityBranch;
import ru.maxmorev.restful.eshop.entities.Customer;
import ru.maxmorev.restful.eshop.entities.ShoppingCart;
import ru.maxmorev.restful.eshop.entities.ShoppingCartSet;
import ru.maxmorev.restful.eshop.repository.CustomerRepository;
import ru.maxmorev.restful.eshop.repository.ShoppingCartRepository;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service("shoppingCartService")
@Transactional
@AllArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartConfig config;
    private final ShoppingCartRepository shoppingCartRepository;
    private final CommodityService commodityService;
    private final CustomerRepository customerRepository;

    @Override
    public ShoppingCart createEmptyShoppingCart() {
        ShoppingCart newCart = new ShoppingCart();
        shoppingCartRepository.save(newCart);
        return newCart;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ShoppingCart> findShoppingCartById(Long id) {
        return shoppingCartRepository.findById(id);
    }

    private void isValidShoppingCartSet(ShoppingCartSet shoppingCartSet) {
        if (Objects.isNull(shoppingCartSet)) {
            throw new IllegalArgumentException("Illegal argument: ShoppingCartSet is null");
        }
        if (Objects.isNull(shoppingCartSet.getAmount()) || shoppingCartSet.getAmount() <= 0) {
            throw new IllegalArgumentException("Illegal argument amount=" + shoppingCartSet.getAmount());
        }

        if (Objects.isNull(shoppingCartSet.getBranch())) {
            throw new IllegalArgumentException("Illegal argument branch=" + shoppingCartSet.getBranch());
        }

        if (Objects.isNull(shoppingCartSet.getShoppingCart())) {
            throw new IllegalArgumentException("Illegal argument shoppingCart=" + shoppingCartSet.getShoppingCart());
        }
    }

    //@Override
    protected ShoppingCart addToShoppingCartSet(ShoppingCartSet shoppingCartSet, Integer amount) {

        isValidShoppingCartSet(shoppingCartSet);
        log.info("======================================");
        log.info("addToShoppingCartSet : " + shoppingCartSet);
        CommodityBranch branch = shoppingCartSet.getBranch();
        ShoppingCart cart = shoppingCartSet.getShoppingCart();

        if (Objects.isNull(shoppingCartSet.getId())) {
            //adding new set
            if (shoppingCartSet.getAmount() > branch.getAmount()) {
                return cart;
            }
            cart.getShoppingSet().add(shoppingCartSet);
        } else {
            if (shoppingCartSet.getAmount() + amount > branch.getAmount()) {
                return cart;
            }
            shoppingCartSet.setAmount(shoppingCartSet.getAmount() + amount);
        }

        shoppingCartRepository.save(cart);
        return cart;
    }

    @Override
    public ShoppingCart addBranchToShoppingCart(Long branchId, Long shoppingCartId, Integer amount) {
        if (branchId == null) throw new IllegalArgumentException("branchId can not be null");
        if (amount == null) throw new IllegalArgumentException("amount can not be null");
        if (shoppingCartId == null) throw new IllegalArgumentException("shoppingCartId can not be null");

        ShoppingCart shoppingCart = this.findShoppingCartById(shoppingCartId)
                .orElseThrow(() -> new IllegalArgumentException("Cant find shopping cart by id"));
        if (config.getMaxItemsAmount() == shoppingCart.getItemsAmount()
                || (shoppingCart.getItemsAmount() + amount) > config.getMaxItemsAmount())
            return shoppingCart;
        return shoppingCart
                .getShoppingSet()
                .stream()
                .filter(scs -> scs.getBranch().getId().equals(branchId))
                .findFirst()
                .map(scs -> addToShoppingCartSet(scs, amount))
                .orElseGet(() -> addToShoppingCartSet(
                        ShoppingCartSet.builder()
                                .amount(amount)
                                .branch(commodityService
                                        .findBranchById(branchId)
                                        .orElseThrow(() -> new IllegalArgumentException("Cant find branch by id")))
                                .shoppingCart(shoppingCart)
                                .build(),
                        amount));
    }

    @Override
    public ShoppingCart removeBranchFromShoppingCart(Long branchId, Long shoppingCartId, Integer amount) {
        if (branchId == null) throw new IllegalArgumentException("branchId can not be null");
        if (amount == null) throw new IllegalArgumentException("amount can not be null");
        if (shoppingCartId == null) throw new IllegalArgumentException("shoppingCartId can not be null");

        ShoppingCart cart = findShoppingCartById(shoppingCartId).orElseThrow(() -> new IllegalArgumentException("Shopping Cart not found"));
        cart
                .getShoppingSet()
                .stream()
                .filter(scs -> scs.getBranch().getId().equals(branchId))
                .findFirst()
                .ifPresent(shoppingCartSet -> {
                    if (shoppingCartSet.getAmount() - amount <= 0) {
                        cart.getShoppingSet().remove(shoppingCartSet);
                    } else {
                        shoppingCartSet.setAmount(shoppingCartSet.getAmount() - amount);
                    }
                    shoppingCartRepository.save(cart);
                });
        return cart;
    }

//    @Override
//    @Transactional(readOnly = true)
//    public Optional<ShoppingCartSet> findByBranchAndShoppingCart(CommodityBranch branch, ShoppingCart cart) {
//        return shoppingCartSetRepository.findByBranchAndShoppingCart(branch, cart);
//    }

    @Override
    public ShoppingCart update(ShoppingCart sc) {
        return shoppingCartRepository.save(sc);
    }


    public ShoppingCart mergeFromTo(ShoppingCart from, ShoppingCart to) {
        if (from != null && to != null && !Objects.equals(from, to)) {
            for (ShoppingCartSet set : from.getShoppingSet()) {
                this.addBranchToShoppingCart(set.getBranch().getId(), to.getId(), set.getAmount());
            }
            shoppingCartRepository.delete(from);
        }
        return to;
    }

    @Override
    public ShoppingCart mergeCartFromCookieWithCustomer(ShoppingCart sc, Customer customer) {
        if (
                Objects.nonNull(customer.getShoppingCart())
                        && !Objects.equals(customer.getShoppingCart(), sc)
        ) {
            //merge cart from cookie to customer cart
            log.info("merging cart");
            sc = mergeFromTo(sc, customer.getShoppingCart());
            log.info("update cookie");

        }
        if (Objects.isNull(customer.getShoppingCartId())) {
            customer.setShoppingCart(sc);
            customerRepository.save(customer);
        }
        return checkAvailabilityByBranches(sc);
    }

    @Override
    public ShoppingCart checkAvailabilityByBranches(ShoppingCart sc) {
        Set<ShoppingCartSet> removeFromCart = new HashSet<>();
        for (ShoppingCartSet set : sc.getShoppingSet()) {
            if (set.getBranch().getAmount() == 0) {
                //remove set from shopping cart
                removeFromCart.add(set);
            } else {
                if (set.getBranch().getAmount() < set.getAmount()) {
                    set.setAmount(set.getBranch().getAmount());
                }
            }
        }
        for (ShoppingCartSet remove : removeFromCart) {
            sc.getShoppingSet().remove(remove);
        }
        return shoppingCartRepository.save(sc);
    }
}
