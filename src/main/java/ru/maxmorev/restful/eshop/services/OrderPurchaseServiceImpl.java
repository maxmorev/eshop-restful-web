package ru.maxmorev.restful.eshop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.maxmorev.restful.eshop.annotation.CustomerOrderStatus;
import ru.maxmorev.restful.eshop.annotation.PaymentProvider;
import ru.maxmorev.restful.eshop.entities.*;
import ru.maxmorev.restful.eshop.repos.CommodityBranchRepository;
import ru.maxmorev.restful.eshop.repos.CustomerOrderRepository;
import ru.maxmorev.restful.eshop.repos.PurchaseRepository;
import ru.maxmorev.restful.eshop.repos.ShoppingCartRepository;

import java.util.List;
import java.util.Optional;

@Service("orderPurchaseService")
@Transactional
public class OrderPurchaseServiceImpl implements OrderPurchaseService {

    private CustomerOrderRepository customerOrderRepository;
    private PurchaseRepository purchaseRepository;
    private CommodityBranchRepository commodityBranchRepository;
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    public void setCustomerOrderRepository(CustomerOrderRepository customerOrderRepository) {
        this.customerOrderRepository = customerOrderRepository;
    }

    @Autowired
    public void setPurchaseRepository(PurchaseRepository purchaseRepository) {
        this.purchaseRepository = purchaseRepository;
    }

    @Autowired
    public void setCommodityBranchRepository(CommodityBranchRepository commodityBranchRepository) {
        this.commodityBranchRepository = commodityBranchRepository;
    }

    @Autowired
    public void setShoppingCartRepository(ShoppingCartRepository shoppingCartRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
    }

    @Override
    public CustomerOrder createOrderFor(Customer customer) {
        CustomerOrder customerOrder = new CustomerOrder();
        customerOrder.setCustomer(customer);
        customerOrder.setStatus(CustomerOrderStatus.AWAITING_PAYMENT);
        final CustomerOrder newOrder = customerOrderRepository.save(customerOrder);
        customer.getShoppingCart().getShoppingSet().forEach(shoppingCartSet -> {
            CommodityBranch changeBranch = shoppingCartSet.getBranch();
            if (changeBranch.getAmount() - shoppingCartSet.getAmount() < 0)
                throw new IllegalArgumentException("Amount of commodities is not available for purchase");
            changeBranch.setAmount(changeBranch.getAmount() - shoppingCartSet.getAmount());
            commodityBranchRepository.save(changeBranch);
            Purchase purchase = new Purchase(shoppingCartSet.getBranch(), newOrder, shoppingCartSet.getAmount());
            newOrder.getPurchases().add(purchase);
        });
        return customerOrderRepository.save(newOrder);
    }

    @Override
    public CustomerOrder confirmPaymentOrder(CustomerOrder order, PaymentProvider paymentProvider, String paymentID) {
        order.setStatus(CustomerOrderStatus.PAYMENT_APPROVED);
        order.setPaymentProvider(paymentProvider);
        order.setPaymentID(paymentID);
        //clean shopping cart
        ShoppingCart shoppingCart = order.getCustomer().getShoppingCart();
        if (shoppingCart != null) {
            shoppingCart.getShoppingSet().clear();
            shoppingCartRepository.save(shoppingCart);
        }
        return customerOrderRepository.save(order);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CustomerOrder> findCustomerOrders(Customer customer) {
        return customerOrderRepository.findByCustomer(customer);
    }

    @Override
    public Optional<CustomerOrder> findOrder(Long id) {
        return customerOrderRepository.findById(id);
    }
}
