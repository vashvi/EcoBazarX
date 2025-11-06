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
    public String login(@RequestBody Users user){
        System.out.println(user);
        return service.verify(user);
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
