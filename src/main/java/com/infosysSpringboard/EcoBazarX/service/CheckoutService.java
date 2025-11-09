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

    @Autowired
    private UserProfileRepo userProfileRepo;


    public Order checkout(String username) {
        Users user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepo.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found for user"));

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty. Cannot checkout.");
        }

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

        // ✅ Reduce stock
        for (OrderItem item : orderItems) {
            Products product = item.getProduct();
            int newStock = product.getStockQuantity() - item.getQuantity();

            if (newStock < 0) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }

            product.setStockQuantity(newStock);
            productRepo.save(product);
        }

        // ✅ Save order
        Order order = Order.builder()
                .user(user)
                .items(orderItems)
                .totalPrice(totalPrice)
                .totalCarbon(totalCarbon)
                .status(OrderStatus.PENDING)
                .build();

        orderItems.forEach(item -> item.setOrder(order));
        Order savedOrder = orderRepo.save(order);

        // ✅ Save carbon insight
        CarbonInsight insight = CarbonInsight.builder()
                .user(user)
                .totalCarbon(totalCarbon)
                .orderCount(1)
                .build();

        carbonInsightRepository.save(insight);

        // ✅ ✅ Award carbon points to user
        UserProfile profile = userProfileRepo.findByUser(user)
                .orElseGet(() -> {
                    UserProfile newProfile = UserProfile.builder()
                            .user(user)
                            .carbonPoints(0)
                            .carbonSaved(0.0)
                            .badge("Bronze")
                            .build();
                    return userProfileRepo.save(newProfile);
                });

        int earnedPoints = orderItems.stream()
                .mapToInt(item -> item.getProduct().getCarbonPoints())
                .sum();

        double savedCarbon = orderItems.stream()
                .mapToDouble(item -> item.getProduct().getCarbonFootprint().doubleValue())
                .sum();

        profile.setCarbonPoints(profile.getCarbonPoints() + earnedPoints);
        profile.setCarbonSaved(profile.getCarbonSaved() + savedCarbon);

        if (profile.getCarbonPoints() >= 1000) profile.setBadge("Platinum");
        else if (profile.getCarbonPoints() >= 500) profile.setBadge("Gold");
        else if (profile.getCarbonPoints() >= 200) profile.setBadge("Silver");
        else profile.setBadge("Bronze");

        userProfileRepo.save(profile);

        // ✅ Clear cart
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

    public List<Order> getOrdersBySeller(Users seller) {
        return orderRepo.findOrdersBySeller(seller);
    }



}
