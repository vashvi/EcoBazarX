package com.infosysSpringboard.EcoBazarX.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "cart_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    @JsonIgnore
    private Cart cart;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "productId", nullable = false)
    private Products product;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "subtotal", precision = 10, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "carbon_total", precision = 10, scale = 3)
    private BigDecimal carbonTotal;

    @PrePersist
    @PreUpdate
    public void calculateTotals() {
        if (product != null && quantity != null) {
            this.subtotal = product.getPrice().multiply(BigDecimal.valueOf(quantity));
            this.carbonTotal = product.getCarbonFootprint().multiply(BigDecimal.valueOf(quantity));
        }
    }
}
