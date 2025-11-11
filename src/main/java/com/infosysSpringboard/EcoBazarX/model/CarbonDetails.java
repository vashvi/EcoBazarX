package com.infosysSpringboard.EcoBazarX.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarbonDetails {

    // 1. Product Info
    private String productName;
    private String volume;
    private String weight;

    // 2. Material Composition
    @Column(columnDefinition = "TEXT")
    private String materialComposition;

    // 3. Manufacturing
    private String manufacturingLocation;
    private String electricityType;
    private String manufacturingEnergyUsed;

    // 4. Packaging
    @Column(columnDefinition = "TEXT")
    private String packagingDetails;

    // 5. Transport
    private String shippingMode;
    private Double seaFreightDistance;
    private Double truckDistance;

    // 6. Usage Phase
    private String lifespan;
    private String powerUsage;

    // 7. End-of-Life
    private Double recyclabilityRate;
    private Double biodegradabilityRate;
}
