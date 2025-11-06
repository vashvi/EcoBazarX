package com.infosysSpringboard.EcoBazarX.service;


import com.infosysSpringboard.EcoBazarX.model.Order;
import com.infosysSpringboard.EcoBazarX.repo.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CarbonReportService {

    @Autowired
    private OrderRepository orderRepo;

    // Total carbon footprint for a user
    public BigDecimal getUserTotalCarbon(String username) {
        return orderRepo.findByUserUsername(username).stream()
                .map(Order::getTotalCarbon)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Monthly carbon summary for a user
    public Map<String, BigDecimal> getMonthlyUserCarbon(String username) {
        List<Order> orders = orderRepo.findByUserUsername(username);
        return orders.stream()
                .collect(Collectors.groupingBy(
                        o -> o.getCreatedAt().getMonth().toString(),
                        Collectors.reducing(BigDecimal.ZERO, Order::getTotalCarbon, BigDecimal::add)
                ));
    }

    // Global report for admin
    public BigDecimal getPlatformTotalCarbon() {
        return orderRepo.findAll().stream()
                .map(Order::getTotalCarbon)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Recent eco-friendly products (low footprint)
    public List<Order> getRecentLowCarbonOrders(String username) {
        return orderRepo.findByUserUsername(username).stream()
                .filter(o -> o.getTotalCarbon().compareTo(new BigDecimal("10.0")) < 0)
                .toList();
    }
}

