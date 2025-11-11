package com.infosysSpringboard.EcoBazarX.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CarbonEstimate {
    private double estimatedCarbonFootprint;
    private String explanation;

    public double getEstimatedCarbonFootprint() {
        return estimatedCarbonFootprint;
    }

    public void setEstimatedCarbonFootprint(double estimatedCarbonFootprint) {
        this.estimatedCarbonFootprint = estimatedCarbonFootprint;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
}
