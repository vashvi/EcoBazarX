package com.infosysSpringboard.EcoBazarX.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infosysSpringboard.EcoBazarX.model.CarbonDetails;
import com.infosysSpringboard.EcoBazarX.model.CarbonEstimate;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.stereotype.Service;

@Service
public class CarbonFootprintService {

    private final ChatClient chatClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public CarbonFootprintService(OllamaChatModel chatModel) {
        this.chatClient = ChatClient.create(chatModel);
    }

    public CarbonEstimate getCarbonEstimateFromDetails(CarbonDetails details) {
        String prompt = """
                You are an environmental impact estimation expert.
                Based on the provided product data, estimate the total carbon footprint (in kilograms of CO₂ equivalent).
                Include manufacturing, energy use, material type, and shipping impact.
                
                Respond ONLY with **valid JSON** (no markdown, no comments, no explanations).
                The JSON must look exactly like this:
                {
                  "estimatedCarbonFootprint": 2.85,
                  "explanation": "Manufacturing and long shipping distance are the major contributors."
                }

                Do not write placeholders like 'number' or add any comment. Return only valid JSON.

                Product Data:
                Product Name: %s
                Volume: %s
                Weight: %s
                Material Composition: %s
                Manufacturing Location: %s
                Electricity Type: %s
                Manufacturing Energy Used: %s
                Packaging Details: %s
                Shipping Mode: %s
                Sea Freight Distance (km): %s
                Truck Distance (km): %s
                Lifespan: %s
                Power Usage: %s
                Recyclability Rate: %s
                Biodegradability Rate: %s
                """.formatted(
                details.getProductName(),
                details.getVolume(),
                details.getWeight(),
                details.getMaterialComposition(),
                details.getManufacturingLocation(),
                details.getElectricityType(),
                details.getManufacturingEnergyUsed(),
                details.getPackagingDetails(),
                details.getShippingMode(),
                details.getSeaFreightDistance(),
                details.getTruckDistance(),
                details.getLifespan(),
                details.getPowerUsage(),
                details.getRecyclabilityRate(),
                details.getBiodegradabilityRate()
        );

        String response = chatClient.prompt()
                .user(prompt)
                .options(OllamaOptions.builder()
                        .temperature(0.0)
                        .topP(0.0)
                        .build())
                .call()
                .content();

        System.out.println("🔍 Raw AI Response:\n" + response);

        try {
            // Remove markdown or comments
            response = response
                    .replaceAll("```json", "")
                    .replaceAll("```", "")
                    .replaceAll("//.*", "")
                    .replaceAll("\\bnumber\\b", "0")  // replace rogue placeholder
                    .trim();

            // Extract JSON
            int start = response.indexOf("{");
            int end = response.lastIndexOf("}") + 1;
            if (start != -1 && end > start) {
                String jsonPart = response.substring(start, end);
                System.out.println("🧩 Cleaned JSON:\n" + jsonPart);
                return objectMapper.readValue(jsonPart, CarbonEstimate.class);
            } else {
                throw new RuntimeException("No valid JSON found in response");
            }

        } catch (Exception e) {
            System.err.println("⚠️ Failed to parse AI response: " + e.getMessage());
            CarbonEstimate fallback = new CarbonEstimate();
            fallback.setEstimatedCarbonFootprint(0.0);
            fallback.setExplanation("Failed to parse AI response");
            return fallback;
        }
    }
}
