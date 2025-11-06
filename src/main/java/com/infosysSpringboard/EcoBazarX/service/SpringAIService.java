package com.infosysSpringboard.EcoBazarX.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class SpringAIService {

    private final ChatClient chatClient;

    public SpringAIService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public String getCarbonEstimate(String description) {
        String prompt = """
                You are a sustainability and carbon footprint expert.
                Estimate the carbon footprint (in kg CO2e) of this product based on its materials, manufacturing, and transportation.
                Respond in pure JSON only, formatted as:
                {
                  "estimatedCarbonFootprint": number,
                  "explanation": "short reason"
                }

                Product Description: "%s"
                """.formatted(description);

        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }
}
