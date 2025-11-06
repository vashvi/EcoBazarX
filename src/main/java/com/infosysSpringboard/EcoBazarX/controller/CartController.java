package com.infosysSpringboard.EcoBazarX.controller;

import com.infosysSpringboard.EcoBazarX.model.Cart;
import com.infosysSpringboard.EcoBazarX.service.CartService;
import com.infosysSpringboard.EcoBazarX.service.JWTService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired private CartService cartService;
    @Autowired private JWTService jwtService;

    private String extractUsername(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing Authorization header");
        }
        return jwtService.extractUsername(authHeader.substring(7));
    }

    @PostMapping("/add/{productId}/{quantity}")
    public ResponseEntity<Cart> addToCart(
            HttpServletRequest request,
            @PathVariable Long productId,
            @PathVariable Integer quantity) {
        String username = extractUsername(request);
        return ResponseEntity.ok(cartService.addToCart(username, productId, quantity));
    }

    @GetMapping("/view")
    public ResponseEntity<Cart> viewCart(HttpServletRequest request) {
        String username = extractUsername(request);
        return ResponseEntity.ok(cartService.viewCart(username));
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<Cart> removeFromCart(
            HttpServletRequest request,
            @PathVariable Long productId) {
        String username = extractUsername(request);
        return ResponseEntity.ok(cartService.removeFromCart(username, productId));
    }

    @DeleteMapping("/clear")
    public ResponseEntity<String> clearCart(HttpServletRequest request) {
        String username = extractUsername(request);
        cartService.clearCart(username);
        return ResponseEntity.ok("Cart cleared successfully for user: " + username);
    }

    @PutMapping("/update/{productId}/{quantity}")
    public ResponseEntity<Cart> updateCartItemQuantity(
            HttpServletRequest request,
            @PathVariable Long productId,
            @PathVariable Integer quantity) {

        String username = extractUsername(request);
        return ResponseEntity.ok(cartService.updateItemQuantity(username, productId, quantity));
    }

}
