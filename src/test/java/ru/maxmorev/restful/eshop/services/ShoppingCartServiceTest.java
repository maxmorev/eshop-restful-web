package ru.maxmorev.restful.eshop.services;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;
import ru.maxmorev.restful.eshop.config.ServiceConfig;
import ru.maxmorev.restful.eshop.config.ServiceTestConfig;
import ru.maxmorev.restful.eshop.entities.ShoppingCart;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ActiveProfiles("test")
@SpringJUnitConfig(classes = {ServiceTestConfig.class, ServiceConfig.class})
@DisplayName("Integration Shopping Cart Service Test")
public class ShoppingCartServiceTest {

    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private CommodityService commodityService;
    @Autowired
    private CustomerService customerService;
    @PersistenceContext
    private EntityManager em;

    @Test
    @Transactional
    @DisplayName("should create empty shopping cart")
    @SqlGroup({
            @Sql(value = "classpath:db/purchase/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/purchase/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void createEmptyShoppingCart() {
        ShoppingCart sc = shoppingCartService.createEmptyShoppingCart();
        em.flush();
        assertNotNull(sc.getId());
    }

    @Test
    @Transactional
    @DisplayName("should find shopping cart by id")
    @SqlGroup({
            @Sql(value = "classpath:db/purchase/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/purchase/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void findShoppingCartByIdTest() {
        assertTrue(shoppingCartService.findShoppingCartById(11L).isPresent());
    }

    @Test
    @Transactional
    @DisplayName("should decrement amount from set for branch")
    @SqlGroup({
            @Sql(value = "classpath:db/purchase/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/purchase/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void decrementBranchFromShoppingCartSetTest() {

        shoppingCartService
                .findShoppingCartById(11L).ifPresent(shoppingCart -> {
            shoppingCart.getShoppingSet().forEach(shoppingCartSet -> {
                assertEquals(2, shoppingCartSet.getAmount().intValue());
                ShoppingCart res = shoppingCartService
                        .removeBranchFromShoppingCart(
                                shoppingCartSet.getBranch().getId(),
                                shoppingCart.getId(),
                                1);
                em.flush();
                res.getShoppingSet()
                        .stream()
                        .filter(
                                s -> s
                                        .getBranch()
                                        .getId()
                                        .equals(shoppingCartSet.getBranch().getId()))
                        .findFirst()
                        .ifPresent(scs -> {
                            assertEquals(1, scs.getAmount().intValue());
                        });

            });
        });


    }

    @Test
    @Transactional
    @DisplayName("should remove all set from shopping cart while decrement total amount for branch in set")
    @SqlGroup({
            @Sql(value = "classpath:db/purchase/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/purchase/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void removeSetFromShoppingCartTest() {
        shoppingCartService
                .findShoppingCartById(11L)
                .map(sc -> sc.getShoppingSet())
                .get()
                .forEach(scs -> {
                    assertNotNull(scs.getAmount());
                    log.info("scs amount={}", scs.getAmount());
                    shoppingCartService
                            .removeBranchFromShoppingCart(
                                    scs.getBranch().getId(),
                                    scs.getShoppingCart().getId(),
                                    scs.getAmount());
                    em.flush();
                    assertTrue(shoppingCartService
                            .findShoppingCartById(11L).map(ShoppingCart::getShoppingSet).get().isEmpty());

                });

    }

    @Test
    @Transactional
    @DisplayName("should update shopping cart")
    @SqlGroup({
            @Sql(value = "classpath:db/purchase/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/purchase/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void updateTest() {
        ShoppingCart sc = shoppingCartService
                .findShoppingCartById(11L).get();
        assertEquals(2, sc.getItemsAmount());
        sc.getShoppingSet().clear();
        shoppingCartService.update(sc);
        em.flush();
        assertTrue(shoppingCartService.findShoppingCartById(11L).map(ShoppingCart::getShoppingSet).get().isEmpty());
    }


    @Test
    @Transactional
    @DisplayName("should add amount for branch to shopping cart set")
    @SqlGroup({
            @Sql(value = "classpath:db/purchase/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/purchase/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void addBranchToShoppingCartTest() {
        shoppingCartService
                .findShoppingCartById(11L).ifPresent(shoppingCart -> {
            shoppingCart.getShoppingSet().forEach(shoppingCartSet -> {
                assertEquals(2, shoppingCartSet.getAmount().intValue());
                assertEquals(5, shoppingCartSet.getBranch().getAmount().intValue());
                ShoppingCart res = shoppingCartService
                        .addBranchToShoppingCart(
                                shoppingCartSet.getBranch().getId(),
                                shoppingCart.getId(),
                                1);
                em.flush();
                res.getShoppingSet()
                        .stream()
                        .filter(
                                s -> s
                                        .getBranch()
                                        .getId()
                                        .equals(shoppingCartSet.getBranch().getId()))
                        .findFirst()
                        .ifPresent(scs -> {
                            assertEquals(3, scs.getAmount().intValue());
                        });

            });
        });
    }

    /**
     * should ignore adding amount for branch to shopping cart set
     * because sum(amount + added amount) > branch total amount )
     */
    @Test
    @Transactional
    @DisplayName("should ignore adding amount for branch to shopping cart set")
    @SqlGroup({
            @Sql(value = "classpath:db/purchase/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/purchase/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void addIngnoreBranchToShoppingCartTest() {
        shoppingCartService
                .findShoppingCartById(11L).ifPresent(shoppingCart -> {
            shoppingCart.getShoppingSet().forEach(shoppingCartSet -> {
                assertEquals(2, shoppingCartSet.getAmount().intValue());
                assertEquals(5, shoppingCartSet.getBranch().getAmount().intValue());
                ShoppingCart res = shoppingCartService
                        .addBranchToShoppingCart(
                                shoppingCartSet.getBranch().getId(),
                                shoppingCart.getId(),
                                4);
                em.flush();
                res.getShoppingSet()
                        .stream()
                        .filter(
                                s -> s
                                        .getBranch()
                                        .getId()
                                        .equals(shoppingCartSet.getBranch().getId()))
                        .findFirst()
                        .ifPresent(scs -> {
                            assertEquals(2, scs.getAmount().intValue());
                        });

            });
        });
    }

    @Test
    @Transactional
    @DisplayName("should check if branch amount is valid in shopping cart")
    @SqlGroup({
            @Sql(value = "classpath:db/purchase/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/purchase/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void checkAvailability() {
        shoppingCartService
                .findShoppingCartById(11L)
                .ifPresent(shoppingCart ->
                        assertFalse(shoppingCartService
                                .checkAvailabilityByBranches(shoppingCart)
                                .getShoppingSet()
                                .isEmpty()));
        /* there is an empty branch in the basket */
        Optional<ShoppingCart> sc = shoppingCartService
                .findShoppingCartById(22L);
        sc.ifPresent(shoppingCart -> {
            /* check if shopping cart is not empty */
            assertFalse(shoppingCart.getShoppingSet().isEmpty());
            /* after checking the shopping cart will become empty */
            shoppingCartService.checkAvailabilityByBranches(shoppingCart);
        });
        em.flush();
        shoppingCartService
                .findShoppingCartById(22L)
                .ifPresent(shoppingCart -> assertTrue(shoppingCart.getShoppingSet().isEmpty()));
    }

    @Test
    @Transactional
    @DisplayName("should merge cart from cookie to customer's shopping cart")
    @SqlGroup({
            @Sql(value = "classpath:db/purchase/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/purchase/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void mergeCartFromCookieWithCustomer() {
        customerService
                .findByEmail("test@titsonfire.store")
                .ifPresent(customer -> {
                    assertEquals(2, customer.getShoppingCart().getItemsAmount());
                    ShoppingCart sc = shoppingCartService
                            .mergeCartFromCookieWithCustomer(
                                    shoppingCartService
                                            .findShoppingCartById(17L)
                                            .get(),
                                    customer);
                    em.flush();
                    assertEquals(3, shoppingCartService
                            .findShoppingCartById(11L)
                            .get()
                            .getItemsAmount());
                });
        assertTrue(shoppingCartService.findShoppingCartById(17L).isEmpty());
    }

}
