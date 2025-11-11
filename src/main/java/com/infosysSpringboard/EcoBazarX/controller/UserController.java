package com.infosysSpringboard.EcoBazarX.controller;

import com.infosysSpringboard.EcoBazarX.model.Products;
import com.infosysSpringboard.EcoBazarX.model.Users;
import com.infosysSpringboard.EcoBazarX.repo.UserRepo;
import com.infosysSpringboard.EcoBazarX.service.JWTService;
import com.infosysSpringboard.EcoBazarX.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private UserService service;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private UserRepo userRepo;

    @PostMapping("/register")
    public Users register(@RequestBody Users user){
        return service.register(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Users user){
        System.out.println("Login attempt with: " + user);
        try {
            String result = service.verify(user);
            
            // Parse the response
            if (result.contains("Login successful")) {
                // Extract token from the response
                String token = result.substring(result.indexOf(": ") + 2);

                // Fetch full user record from DB to return canonical data
                Users dbUser = null;
                if (user.getUsername() != null) {
                    dbUser = userRepo.findByUsername(user.getUsername()).orElse(null);
                } else if (user.getEmail() != null) {
                    dbUser = userRepo.findByEmail(user.getEmail()).orElse(null);
                }

                // Create response object
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Login successful");
                response.put("token", token);
                response.put("user", dbUser != null ? dbUser : user);

                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", result);
                
                return ResponseEntity.status(401).body(response);
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Login failed: " + e.getMessage());
            
            return ResponseEntity.status(401).body(response);
        }
    }

    @GetMapping("/all-products")
    public ResponseEntity<List<Products>> findAllProducts(){
        List<Products> products = service.findAllProducts();
        return ResponseEntity.ok(products);
    }

//    @GetMapping("/my-cart")
//    public ResponseEntity<List<Products>> getMyProducts(HttpServletRequest request) {
//        String username = extractUsernameFromJWT(request);
//
//        Users user = userRepo.findByUsername(username)
//                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
//
//        List<Products> products = service.findProductsByUser(user);
//        return ResponseEntity.ok(products);
//    }

//    private String extractUsernameFromJWT(HttpServletRequest request) {
//        String authHeader = request.getHeader("Authorization");
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            throw new RuntimeException("Missing or invalid Authorization header");
//        }
//
//        String token = authHeader.substring(7);
//        return jwtService.extractUsername(token);
//    }
}
