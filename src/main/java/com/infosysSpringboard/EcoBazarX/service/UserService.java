package com.infosysSpringboard.EcoBazarX.service;

import com.infosysSpringboard.EcoBazarX.model.Products;
import com.infosysSpringboard.EcoBazarX.model.Users;
import com.infosysSpringboard.EcoBazarX.repo.ProductRepo;
import com.infosysSpringboard.EcoBazarX.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepo repo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private JWTService service;

    @Autowired
    private AuthenticationManager authManager;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);


    public Users register(Users user) {

        user.setPassword(encoder.encode(user.getPassword()));


        return repo.save(user);
    }


    public String verify(Users user) {
        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
            );

            if (authentication.isAuthenticated()) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();

                String token = service.generateToken(userDetails);
                return "Login successful!\nGenerated Token: " + token;
            }
            else {
                return "Login failed!";
            }
        } catch (AuthenticationException e) {
            return "Login failed: " + e.getMessage();
        }
    }

    public List<Products> findAllProducts() {
        return productRepo.findAll();
    }

}
