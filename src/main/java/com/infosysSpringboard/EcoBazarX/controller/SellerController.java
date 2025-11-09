package com.infosysSpringboard.EcoBazarX.controller;

import com.infosysSpringboard.EcoBazarX.model.*;
import com.infosysSpringboard.EcoBazarX.repo.UserRepo;
import com.infosysSpringboard.EcoBazarX.service.CarbonFootprintService;
import com.infosysSpringboard.EcoBazarX.service.CheckoutService;
import com.infosysSpringboard.EcoBazarX.service.JWTService;
import com.infosysSpringboard.EcoBazarX.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/seller")
public class SellerController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private CheckoutService checkoutService;

    @Autowired
    private CarbonFootprintService carbonFootprintService;


    @PostMapping("/add-product")
    public ResponseEntity<String> addProduct(@RequestBody Products product, HttpServletRequest request) {
        String username = extractUsernameFromJWT(request);

        Users seller = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

        product.setPostedBy(seller);

        // --- Require structured carbon details ---
        if (product.getCarbonDetails() == null) {
            return ResponseEntity.badRequest().body("❌ Carbon details are required to estimate the carbon footprint.");
        }

        // --- 1️⃣ Get AI-based carbon footprint ---
        CarbonEstimate estimate = carbonFootprintService.getCarbonEstimateFromDetails(product.getCarbonDetails());

        // --- 2️⃣ Assign footprint data ---
        product.setCarbonFootprint(BigDecimal.valueOf(estimate.getEstimatedCarbonFootprint()));
        product.setCarbonExplanation(estimate.getExplanation());

        // --- 3️⃣ Assign eco score ---
        double cf = estimate.getEstimatedCarbonFootprint();
        int carbonPoints;
        if (cf <= 1) carbonPoints = 50;
        else if (cf <= 3) carbonPoints = 30;
        else if (cf <= 5) carbonPoints = 20;
        else if (cf <= 10) carbonPoints = 10;
        else carbonPoints = 5;

        product.setCarbonPoints(carbonPoints);

        // --- 4️⃣ Save ---
        productService.saveProduct(product);

        return ResponseEntity.ok("✅ Product added successfully by " + seller.getUsername() +
                " | Carbon footprint: " + cf + " kg CO₂e | Points: " + carbonPoints);
    }





    @GetMapping("/my-products")
    public ResponseEntity<List<Products>> getMyProducts(HttpServletRequest request) {
        String username = extractUsernameFromJWT(request);

        Users seller = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

        List<Products> products = productService.findProductsBySeller(seller);
        return ResponseEntity.ok(products);
    }

    @PutMapping("/update-product/{productId}")
    public ResponseEntity<String> updateProduct(@PathVariable Long productId, @RequestBody Products updatedProduct) {
        productService.updateProduct(productId, updatedProduct);
        return ResponseEntity.ok("Product updated successfully!");
    }

    @DeleteMapping("/delete-product/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok("Product deleted successfully!");
    }

    private String extractUsernameFromJWT(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7);
        return jwtService.extractUsername(token);
    }

    @PutMapping("/update-status/{orderId}")
    public ResponseEntity<?> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam OrderStatus status,
            HttpServletRequest request) {

        String username = extractUsernameFromJWT(request);

        Users seller = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Seller not found"));

        Order order = checkoutService.getOrderById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        boolean ownsProduct = order.getItems().stream()
                .anyMatch(item -> item.getProduct().getPostedBy().getId().equals(seller.getId()));

        if (!ownsProduct) {
            return ResponseEntity.status(403)
                    .body("You are not authorized to update this order — none of your products are in it.");
        }

        Order updatedOrder = checkoutService.updateOrderStatus(orderId, status);

        return ResponseEntity.ok(updatedOrder);
    }


    @GetMapping("/my-orders")
    public ResponseEntity<List<Order>> getOrdersForSeller(HttpServletRequest request) {
        String username = extractUsernameFromJWT(request);
        Users seller = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Seller not found"));

        List<Order> sellerOrders = checkoutService.getOrdersBySeller(seller);

        // Filter out other sellers’ products from each order
        sellerOrders.forEach(order ->
                order.setItems(order.getItems().stream()
                        .filter(item -> item.getProduct().getPostedBy().getId().equals(seller.getId()))
                        .collect(Collectors.toList()))
        );

        return ResponseEntity.ok(sellerOrders);
    }





}
