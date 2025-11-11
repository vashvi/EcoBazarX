package com.infosysSpringboard.EcoBazarX.service;

import com.infosysSpringboard.EcoBazarX.model.Products;
import com.infosysSpringboard.EcoBazarX.model.UserProfile;
import com.infosysSpringboard.EcoBazarX.model.Users;
import com.infosysSpringboard.EcoBazarX.repo.ProductRepo;
import com.infosysSpringboard.EcoBazarX.repo.UserProfileRepo;
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

    @Autowired
    private UserProfileRepo userProfileRepo;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);


    public Users register(Users user) {

        user.setPassword(encoder.encode(user.getPassword()));


        Users savedUser = repo.save(user);

        UserProfile profile = UserProfile.builder()
                .user(savedUser)
                .carbonPoints(0)
                .carbonSaved(0.0)
                .badge("Bronze")       // optional default badge
                .build();

        userProfileRepo.save(profile);

        return savedUser;

    }


    public String verify(Users user) {
        try {
            // Support login by either username or email: if username not provided, try to find it by email
            if ((user.getUsername() == null || user.getUsername().isBlank()) && user.getEmail() != null) {
                java.util.Optional<Users> found = repo.findByEmail(user.getEmail());
                if (found.isPresent()) {
                    user.setUsername(found.get().getUsername());
                } else {
                    return "Login failed: user not found";
                }
            }
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
