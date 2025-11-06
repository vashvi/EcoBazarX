package com.infosysSpringboard.EcoBazarX.service;

import com.fasterxml.jackson.databind.ObjectMapper;
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

    public CarbonEstimate getCarbonEstimate(String productDescription) {
        String prompt = """
            Calculate the carbon footprint for the product description given and 
            Return ONLY JSON (no markdown, no extra text):
            {
              "estimatedCarbonFootprint": number,
              "explanation": "text"
            }
            Product Description: %s
            """.formatted(productDescription);

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
            // Step 1: Remove markdown fences like ```json ... ```
            response = response.replaceAll("```json", "")
                    .replaceAll("```", "")
                    .trim();

            // Step 2: Fix common typos (like "explanthon" → "explanation")
            response = response.replaceAll("\"explanthon\"", "\"explanation\"");

            // Step 3: Extract JSON part
            int start = response.indexOf("{");
            int end = response.lastIndexOf("}") + 1;
            if (start != -1 && end != -1 && end > start) {
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
