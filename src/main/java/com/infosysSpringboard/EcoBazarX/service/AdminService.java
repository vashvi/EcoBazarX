package com.infosysSpringboard.EcoBazarX.service;

import com.infosysSpringboard.EcoBazarX.dto.AdminCategoryDistributionDto;
import com.infosysSpringboard.EcoBazarX.dto.AdminMonthlyTrendDto;
import com.infosysSpringboard.EcoBazarX.dto.AdminStatsDto;
import com.infosysSpringboard.EcoBazarX.model.Products;
import com.infosysSpringboard.EcoBazarX.model.Role;
import com.infosysSpringboard.EcoBazarX.model.Users;
import com.infosysSpringboard.EcoBazarX.repo.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdminService {

    private final UserRepo userRepo;

    @Autowired private CartRepo cartRepository;
    @Autowired private OrderItemRepository orderItemRepository;
    @Autowired private CarbonInsightRepository carbonInsightsRepo;
    @Autowired private ProductRepo productRepo;
    @Autowired private OrderRepository orderRepository;
    @Autowired private UserProfileRepo userProfileRepo;
    @Autowired private ProductRepo sellerRepo;

    // Constructor Injection for userRepo
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

    // ---------- DASHBOARD SUMMARY ----------
    public AdminStatsDto getStats() {

        long totalUsers = userRepo.count();
        long totalSellers = userRepo.countByRole(Role.SELLER);
        long totalCustomers = userRepo.countByRole(Role.USER);
        long totalProducts = productRepo.count();
        long totalOrders = orderRepository.count();
        double totalRevenue = orderRepository.sumTotalPrice();

        // --- ACTIVE in last 30 days ---
        LocalDateTime cutoff = LocalDateTime.now().minusDays(30);

        long activeSellers = orderRepository.countActiveSellersLast30Days(cutoff);
        long activeCustomers = orderRepository.countActiveCustomersLast30Days(cutoff);

        // --- Carbon Saved (sum from UserProfile) ---
        double carbonSaved = userProfileRepo.sumCarbonSaved();  // You must add this query

        return AdminStatsDto.builder()
                .totalUsers(totalUsers)
                .totalSellers(totalSellers)
                .totalCustomers(totalCustomers)
                .totalProducts(totalProducts)
                .totalOrders(totalOrders)
                .totalRevenue(totalRevenue)
                .activeSellers(activeSellers)
                .activeCustomers(activeCustomers)
                .carbonSaved(carbonSaved)
                .build();
    }


    public List<Products> findAllProducts() {
        return productRepo.findAll();
    }

    // ---------- MONTHLY ANALYTICS ----------
    public List<AdminMonthlyTrendDto> getMonthlyTrends() {
        List<Object[]> rows = orderRepository.getMonthlyTrends();

        return rows.stream()
                .map(r -> new AdminMonthlyTrendDto(
                        (String) r[0],
                        r[1] == null ? 0 : ((Number) r[1]).doubleValue(),
                        ((Number) r[2]).longValue(),
                        ((Number) r[3]).longValue()
                ))
                .toList();
    }

    // ---------- CATEGORY ANALYTICS ----------
    public List<AdminCategoryDistributionDto> getCategoryDistribution() {
        List<Object[]> rows = productRepo.getCategoryDistribution();

        return rows.stream()
                .map(r -> new AdminCategoryDistributionDto(
                        (String) r[0],
                        ((Number) r[1]).longValue()
                ))
                .toList();
    }

}
