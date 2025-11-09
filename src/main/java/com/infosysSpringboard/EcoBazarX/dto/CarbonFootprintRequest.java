package com.infosysSpringboard.EcoBazarX.dto;


import com.infosysSpringboard.EcoBazarX.model.CarbonDetails;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarbonFootprintRequest {
    private String productName;
    private String category;
    private String description;
    private CarbonDetails carbonDetails; // embedded carbon-related parameters
}

