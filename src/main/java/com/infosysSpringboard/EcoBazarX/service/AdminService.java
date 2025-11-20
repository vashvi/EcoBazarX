package com.infosysSpringboard.EcoBazarX.service;

import com.infosysSpringboard.EcoBazarX.dto.AdminStatsDto;
import com.infosysSpringboard.EcoBazarX.model.Products;
import com.infosysSpringboard.EcoBazarX.model.Role;
import com.infosysSpringboard.EcoBazarX.model.Users;
import com.infosysSpringboard.EcoBazarX.repo.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    private final UserRepo userRepo;

    @Autowired
    private CartRepo cartRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CarbonInsightRepository carbonInsightsRepo;


    @Autowired
    private ProductRepo productRepository;


    public AdminService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public List<Users> findAllUsers() {
        return userRepo.findAll();
    }

    public List<Users> findUsersByRole(Role role) {
        return userRepo.findByRole(role);
    }

    public Users changeUserRole(String username, Role newRole) {
        Users user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        user.setRole(newRole);
        return userRepo.save(user);
    }

    @Transactional
    public void deleteUser(Long userId) {

        // delete child rows first
        orderItemRepository.deleteItemsByUserId(userId);
        cartRepository.deleteByUserId(userId);
        orderRepository.deleteByUserId(userId);
        carbonInsightsRepo.deleteByUserId(userId);

        // now safe to delete user
        userRepo.deleteById(userId);
    }




    public Users updateUserDetails(Long userId, Users updatedUser) {
        Users user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setEmail(updatedUser.getEmail());
        user.setPhone(updatedUser.getPhone());
        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        return userRepo.save(user);
    }

    // src/main/java/.../service/AdminService.java
//    @Autowired
//    private UserRepo userRepo;
    @Autowired private ProductRepo productRepo;
    @Autowired private OrderRepository orderRepository;

    public AdminStatsDto getStats() {
        long totalUsers = userRepo.count();
        long totalSellers = userRepo.countByRole(Role.SELLER);
        long totalCustomers = userRepo.countByRole(Role.USER);
        long totalProducts = productRepo.count();
        long totalOrders = orderRepository.count();
        double totalRevenue = orderRepository.sumTotalPrice(); // add a query in repo

        return AdminStatsDto.builder()
                .totalUsers(totalUsers)
                .totalSellers(totalSellers)
                .totalCustomers(totalCustomers)
                .totalProducts(totalProducts)
                .totalOrders(totalOrders)
                .totalRevenue(totalRevenue)
                .build();
    }

    public List<Products> findAllProducts() {
        return productRepository.findAll();
    }


}
