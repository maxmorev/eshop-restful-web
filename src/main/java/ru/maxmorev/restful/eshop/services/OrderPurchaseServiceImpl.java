package ru.maxmorev.restful.eshop.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.maxmorev.restful.eshop.annotation.CustomerOrderStatus;
import ru.maxmorev.restful.eshop.annotation.PaymentProvider;
import ru.maxmorev.restful.eshop.config.OrderConfiguration;
import ru.maxmorev.restful.eshop.entities.CommodityBranch;
import ru.maxmorev.restful.eshop.entities.Customer;
import ru.maxmorev.restful.eshop.entities.CustomerOrder;
import ru.maxmorev.restful.eshop.entities.Purchase;
import ru.maxmorev.restful.eshop.entities.ShoppingCart;
import ru.maxmorev.restful.eshop.repository.CommodityBranchRepository;
import ru.maxmorev.restful.eshop.repository.CustomerOrderRepository;
import ru.maxmorev.restful.eshop.repository.PurchaseRepository;
import ru.maxmorev.restful.eshop.repository.ShoppingCartRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service("orderPurchaseService")
@Transactional
@AllArgsConstructor
public class OrderPurchaseServiceImpl implements OrderPurchaseService {

    private CustomerOrderRepository customerOrderRepository;
    private PurchaseRepository purchaseRepository;
    private CommodityBranchRepository commodityBranchRepository;
    private ShoppingCartRepository shoppingCartRepository;
    private OrderConfiguration orderConfiguration;

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

    @Override
    public void cleanExpiredOrders() {
        //DO NOTHING
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MINUTE, -orderConfiguration.getOrderExpiredMinutes());
        Date teenMinutesFromNow = now.getTime();
        List<CustomerOrder> expiredOrders = customerOrderRepository.findExpiredOrdersByStatus(CustomerOrderStatus.AWAITING_PAYMENT, teenMinutesFromNow);
        expiredOrders.forEach(co -> {
            co.getPurchases().forEach(p -> {
                CommodityBranch branch = p.getBranch();
                branch.setAmount( branch.getAmount().intValue() + p.getAmount().intValue() );
                commodityBranchRepository.save(branch);
            });
            customerOrderRepository.delete(co);
        });
    }
}
