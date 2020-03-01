package ru.maxmorev.restful.eshop.services;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.maxmorev.restful.eshop.annotation.CustomerOrderStatus;
import ru.maxmorev.restful.eshop.annotation.PaymentProvider;
import ru.maxmorev.restful.eshop.config.ManagerConfig;
import ru.maxmorev.restful.eshop.config.OrderConfiguration;
import ru.maxmorev.restful.eshop.entities.CommodityBranch;
import ru.maxmorev.restful.eshop.entities.Customer;
import ru.maxmorev.restful.eshop.entities.CustomerOrder;
import ru.maxmorev.restful.eshop.entities.Purchase;
import ru.maxmorev.restful.eshop.entities.ShoppingCart;
import ru.maxmorev.restful.eshop.feignclient.MailService;
import ru.maxmorev.restful.eshop.feignclient.domain.OrderPaymentConfirmedAdminTemplate;
import ru.maxmorev.restful.eshop.feignclient.domain.OrderPaymentConfirmedTemplate;
import ru.maxmorev.restful.eshop.repository.CommodityBranchRepository;
import ru.maxmorev.restful.eshop.repository.CustomerOrderRepository;
import ru.maxmorev.restful.eshop.repository.ShoppingCartRepository;
import ru.maxmorev.restful.eshop.rest.response.CustomerOrderDto;
import ru.maxmorev.restful.eshop.rest.response.OrderGrid;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("orderPurchaseService")
@Transactional
@AllArgsConstructor
public class OrderPurchaseServiceImpl implements OrderPurchaseService {

    private final CustomerService customerService;
    private final CustomerOrderRepository customerOrderRepository;
    private final CommodityBranchRepository commodityBranchRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderConfiguration orderConfiguration;
    private final NotificationService notificationService;

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
        if (!CustomerOrderStatus.AWAITING_PAYMENT.equals(order.getStatus()))
            throw new IllegalArgumentException("Invalid order status");
        order.setStatus(CustomerOrderStatus.PAYMENT_APPROVED);
        order.setPaymentProvider(paymentProvider);
        order.setPaymentID(paymentID);
        //clean shopping cart
        ShoppingCart shoppingCart = order.getCustomer().getShoppingCart();
        if (shoppingCart != null) {
            shoppingCart.getShoppingSet().clear();
            shoppingCartRepository.save(shoppingCart);
        }
        notificationService.orderPaymentConfirmation(
                order.getCustomer().getEmail(),
                order.getCustomer().getFullName(),
                order.getId()
        );
        return customerOrderRepository.save(order);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CustomerOrder> findCustomerOrders(Long customerId) {
        Optional<Customer> customer = customerService.findById(customerId);
        if (customer.isPresent()) {
            return customerOrderRepository.findByCustomerOrderByDateOfCreationDesc(customer.get());
        }
        return Collections.emptyList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<CustomerOrder> findCustomerOrders(Long customerId, CustomerOrderStatus status) {
        Optional<Customer> customer = customerService.findById(customerId);
        if (customer.isPresent()) {
            return customerOrderRepository.findByCustomerAndStatusOrderByDateOfCreationDesc(customer.get(), status);
        }
        return Collections.emptyList();
    }

    @Override
    public Optional<CustomerOrder> findOrder(Long id) {
        return customerOrderRepository.findById(id);
    }

    @Override
    public void cleanExpiredOrders() {
        //DO NOTHING
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MINUTE, -orderConfiguration.getExpiredMinutes());
        Date teenMinutesFromNow = now.getTime();
        List<CustomerOrder> expiredOrders = customerOrderRepository.findExpiredOrdersByStatus(CustomerOrderStatus.AWAITING_PAYMENT, teenMinutesFromNow);
        expiredOrders.forEach(co -> {
            co.getPurchases().forEach(p -> {
                CommodityBranch branch = p.getBranch();
                branch.setAmount(branch.getAmount().intValue() + p.getAmount().intValue());
                commodityBranchRepository.save(branch);
            });
            customerOrderRepository.delete(co);
        });
    }

    @Override
    public CustomerOrder setOrderStatus(Long id, CustomerOrderStatus status) {
        CustomerOrder order = findOrder(id).orElseThrow(() -> new IllegalArgumentException("Invalid order id"));
        order.setStatus(status);
        return customerOrderRepository.save(order);
    }


    @Override
    @Transactional(readOnly = true)
    public Page<CustomerOrder> findAllOrdersByPage(Pageable pageable) {
        return customerOrderRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerOrder> findAllOrdersByPageAndStatus(Pageable pageable, CustomerOrderStatus status) {
        return customerOrderRepository.findByStatus(pageable, status);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerOrder> findAllOrdersByPageAndStatusNot(Pageable pageable, CustomerOrderStatus status) {
        return customerOrderRepository.findByStatusNot(pageable, status);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CustomerOrder> findOrder(Long id, Long customerId) {
        return customerOrderRepository.findByIdAndCustomerId(id, customerId);
    }

    private void moveItemsFromOrderToBranch(CustomerOrder o) {
        o.getPurchases().forEach(purchase -> {
            CommodityBranch branch = purchase.getBranch();
            branch.setAmount(branch.getAmount().intValue() + purchase.getAmount().intValue());
            commodityBranchRepository.save(branch);
        });
    }

    @Override
    public void cancelOrderByCustomer(Long orderId) {
        //move elements back to branch
        CustomerOrder order = customerOrderRepository
                .findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        moveItemsFromOrderToBranch(order);
        //set order status to canceled
        if (CustomerOrderStatus.AWAITING_PAYMENT.equals(order.getStatus())) {
            customerOrderRepository.delete(order);
            return;
        }
        if (CustomerOrderStatus.PAYMENT_APPROVED.equals(order.getStatus())) {
            order.setStatus(CustomerOrderStatus.CANCELED_BY_CUSTOMER);
            customerOrderRepository.save(order);
            return;
        }
        throw new RuntimeException("Implement logic with other OrderStatus");
    }

    @Override
    public List<CustomerOrderDto> findOrderListForCustomer(Long customerId) {
        return customerOrderRepository
                .findByCustomerIdAndStatusNotOrderByDateOfCreationDesc(
                        customerId,
                        CustomerOrderStatus.AWAITING_PAYMENT)
                .stream()
                .map(CustomerOrderDto::forCusrtomer)
                .collect(Collectors.toList());
    }

    private PageRequest getPageRequeset(Integer page,
                                        Integer rows,
                                        String sortBy,
                                        String order) {
        Sort sort = null;
        String orderBy = sortBy;
        if (orderBy != null && orderBy.equals("dateOfCreation")) {
            orderBy = "dateOfCreation";
        } else {
            orderBy = "id";
        }
        if (Objects.isNull(order)) {
            order = "desc";
        }
        if (Objects.isNull(page)) {
            page = 1;
        }
        if (Objects.isNull(rows)) {
            rows = 10;
        }

        if (orderBy != null && order != null) {
            if (order.equals("desc")) {
                sort = Sort.by(Sort.Direction.DESC, orderBy);
            } else
                sort = Sort.by(Sort.Direction.ASC, orderBy);
        }
        // Constructs page request for current page
        // Note: page number for Spring Data JPA starts with 0, while jqGrid starts with 1
        PageRequest pageRequest = null;
        if (sort != null) {
            pageRequest = PageRequest.of(page - 1, rows, sort);
        } else {
            pageRequest = PageRequest.of(page - 1, rows);
        }
        return pageRequest;
    }

    @Override
    public OrderGrid getOrdersForAdmin(Integer page, Integer rows, String sortBy, String order) {
        return new OrderGrid(findAllOrdersByPageAndStatusNot(
                getPageRequeset(page,
                        rows,
                        sortBy,
                        order),
                CustomerOrderStatus.AWAITING_PAYMENT));
    }

}
