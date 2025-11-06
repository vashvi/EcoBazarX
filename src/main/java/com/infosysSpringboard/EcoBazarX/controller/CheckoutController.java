package com.infosysSpringboard.EcoBazarX.controller;

import com.infosysSpringboard.EcoBazarX.model.Order;
import com.infosysSpringboard.EcoBazarX.model.OrderStatus;
import com.infosysSpringboard.EcoBazarX.service.CheckoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {

    @Autowired
    private CheckoutService checkoutService;

    @PostMapping("/add-checkout")
    public ResponseEntity<Order> checkout(Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(checkoutService.checkout(username));
    }

    @GetMapping("/my-orders")
    public ResponseEntity<List<Order>> getMyOrders(Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(checkoutService.getUserOrders(username));
    }

    @PutMapping("/status/{orderId}")
    public ResponseEntity<Order> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam OrderStatus status) {
        return ResponseEntity.ok(checkoutService.updateOrderStatus(orderId, status));
    }


}
