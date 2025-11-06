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

        String description = product.getDescription();

        if (description == null || description.isBlank()) {
            return ResponseEntity.badRequest().body("Product description is required for carbon estimation.");
        }

        CarbonEstimate estimate = carbonFootprintService.getCarbonEstimate(description);

        product.setCarbonFootprint(BigDecimal.valueOf(estimate.getEstimatedCarbonFootprint()));
        product.setCarbonExplanation(estimate.getExplanation());

        productService.saveProduct(product);

        return ResponseEntity.ok("Product added successfully by " + seller.getUsername());
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

        // Step 1: Get the seller
        Users seller = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Seller not found"));

        // Step 2: Get the order
        Order order = checkoutService.getOrderById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Step 3: Verify that this seller owns at least one product in the order
        boolean ownsProduct = order.getItems().stream()
                .anyMatch(item -> item.getProduct().getPostedBy().getId().equals(seller.getId())); // ✅ FIXED

        if (!ownsProduct) {
            return ResponseEntity.status(403)
                    .body("You are not authorized to update this order — none of your products are in it.");
        }

        // Step 4: Allow the seller to update order status
        Order updatedOrder = checkoutService.updateOrderStatus(orderId, status);

        return ResponseEntity.ok(updatedOrder);
    }


}
