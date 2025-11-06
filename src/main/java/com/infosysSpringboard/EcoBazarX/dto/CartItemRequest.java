package com.infosysSpringboard.EcoBazarX.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemRequest {
    private Long productId;
    private int quantity;
}
