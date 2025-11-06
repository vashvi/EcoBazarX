package com.infosysSpringboard.EcoBazarX.dto;

import com.infosysSpringboard.EcoBazarX.model.CartItem;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartResponse {
    private List<CartItem> items;
    private double subtotal;
    private double totalCarbonKg;
}
