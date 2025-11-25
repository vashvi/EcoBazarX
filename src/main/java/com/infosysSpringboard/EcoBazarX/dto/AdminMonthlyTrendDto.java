package com.infosysSpringboard.EcoBazarX.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminMonthlyTrendDto {
    private String month;
    private double revenue;
    private long orders;
    private long newUsers;
}
