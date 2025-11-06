package com.infosysSpringboard.EcoBazarX.service;

import com.infosysSpringboard.EcoBazarX.model.*;
import com.infosysSpringboard.EcoBazarX.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CheckoutService {
    @Autowired
    private CarbonInsightRepository carbonInsightRepository;

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private CartRepo cartRepo;
    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private ProductRepo productRepo;

//    public Order checkout(String username) {
//        Users user = userRepo.findByUsername(username)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        Cart cart = cartRepo.findByUser(user)
//                .orElseThrow(() -> new RuntimeException("Cart not found for user"));
//
//        if (cart.getItems().isEmpty()) {
//            throw new RuntimeException("Cart is empty. Cannot checkout.");
//        }
//
//        List<OrderItem> orderItems = cart.getItems().stream()
//                .map(ci -> OrderItem.builder()
//                        .product(ci.getProduct())
//                        .quantity(ci.getQuantity())
//                        .subtotal(ci.getSubtotal())
//                        .carbonTotal(ci.getCarbonTotal())
//                        .build())
//                .collect(Collectors.toList());
//
//        BigDecimal totalPrice = orderItems.stream()
//                .map(OrderItem::getSubtotal)
//                .reduce(BigDecimal.ZERO, BigDecimal::add);
//
//        BigDecimal totalCarbon = orderItems.stream()
//                .map(OrderItem::getCarbonTotal)
//                .reduce(BigDecimal.ZERO, BigDecimal::add);
//
//        // Create order
//        Order order = Order.builder()
//                .user(user)
//                .items(orderItems)
//                .totalPrice(totalPrice)
//                .totalCarbon(totalCarbon)
//                .status(OrderStatus.PENDING)
//                .build();
//
//        // Link each item to order
//        orderItems.forEach(item -> item.setOrder(order));
//
//        // Save order
//        Order savedOrder = orderRepo.save(order);
//
//        // Clear cart after checkout
//        cart.getItems().clear();
//        cartRepo.save(cart);
//
//        return savedOrder;
//    }
public Order checkout(String username) {
    Users user = userRepo.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

    Cart cart = cartRepo.findByUser(user)
            .orElseThrow(() -> new RuntimeException("Cart not found for user"));

    if (cart.getItems().isEmpty()) {
        throw new RuntimeException("Cart is empty. Cannot checkout.");
    }

    // Convert cart items to order items
    List<OrderItem> orderItems = cart.getItems().stream()
            .map(ci -> OrderItem.builder()
                    .product(ci.getProduct())
                    .quantity(ci.getQuantity())
                    .subtotal(ci.getSubtotal())
                    .carbonTotal(ci.getCarbonTotal())
                    .build())
            .collect(Collectors.toList());

    BigDecimal totalPrice = orderItems.stream()
            .map(OrderItem::getSubtotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal totalCarbon = orderItems.stream()
            .map(OrderItem::getCarbonTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    // ✅ Deduct stock for each product
    for (OrderItem item : orderItems) {
        Products product = item.getProduct();
        int newStock = product.getStockQuantity() - item.getQuantity();

        if (newStock < 0) {
            throw new RuntimeException("Insufficient stock for product: " + product.getName());
        }

        product.setStockQuantity(newStock);
        // Save updated stock
        // Assuming you have a ProductRepo autowired
        productRepo.save(product);
    }

    // Create new order
    Order order = Order.builder()
            .user(user)
            .items(orderItems)
            .totalPrice(totalPrice)
            .totalCarbon(totalCarbon)
            .status(OrderStatus.PENDING)
            .build();

    orderItems.forEach(item -> item.setOrder(order));

    // Save order
    Order savedOrder = orderRepo.save(order);

    // Save CarbonInsight record
    CarbonInsight insight = CarbonInsight.builder()
            .user(user)
            .totalCarbon(totalCarbon)
            .orderCount(1)
            .build();

    carbonInsightRepository.save(insight);

    // ✅ Clear cart after checkout
    cart.getItems().clear();
    cartRepo.save(cart);

    return savedOrder;
}



    public List<Order> getUserOrders(String username) {
        Users user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return orderRepo.findByUser(user);
    }

    public Order updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        return orderRepo.save(order);
    }

    public Optional<Order> getOrderById(Long orderId) {
        return orderRepo.findById(orderId);
    }

    public List<Order> getAllOrders() {
        return orderRepo.findAll();
    }
}
