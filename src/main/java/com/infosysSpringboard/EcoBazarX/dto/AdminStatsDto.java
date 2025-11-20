
// src/main/java/.../dto/AdminStatsDto.java
package com.infosysSpringboard.EcoBazarX.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminStatsDto {
    private long totalUsers;
    private long totalSellers;
    private long totalCustomers;
    private long totalProducts;
    private long totalOrders;
    private double totalRevenue;
}