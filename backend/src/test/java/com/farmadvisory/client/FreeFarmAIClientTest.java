package com.farmadvisory.client;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FreeFarmAIClientTest {

    private final FreeFarmAIClient client = new FreeFarmAIClient();

    @Test
    void cottonIrrigationQuestionOnlyGivesIrrigationAdvice() {
        String response = client.getAIResponse("How often should I irrigate cotton in clay soil?");

        assertThat(response).contains("7 to 10 days");
        assertThat(response).contains("irrigate");
        assertThat(response).doesNotContain("top 3 suitable crops");
    }

    @Test
    void fertilizerQuestionOnlyGivesFertilizerAdvice() {
        String response = client.getAIResponse("How much urea and NPK should I use?");

        assertThat(response).contains("balanced NPK");
        assertThat(response).contains("split");
        assertThat(response).doesNotContain("Irrigate");
        assertThat(response).doesNotContain("top 3 suitable crops");
    }

    @Test
    void pestQuestionOnlyGivesPestAdvice() {
        String response = client.getAIResponse("There are insects and pest damage on my crop");

        assertThat(response).contains("Integrated Pest Management");
        assertThat(response).contains("Step 1:");
        assertThat(response).contains("Step 2:");
        assertThat(response).contains("Step 3:");
        assertThat(response).contains("Step 4:");
        assertThat(response).doesNotContain("balanced NPK");
        assertThat(response).doesNotContain("top 3 suitable crops");
    }

    @Test
    void cropRecommendationRequiresExplicitCropIntent() {
        String response = client.getAIResponse("Which crop is suitable for sandy soil?");

        assertThat(response).contains("top 3 suitable crops");
        assertThat(response).doesNotContain("Irrigate");
    }

    @Test
    void responseUsesRequiredSectionFormat() {
        String response = client.getAIResponse("How often should I water cotton?");

        assertThat(response).contains("\uD83C\uDF3E Answer (direct solution)");
        assertThat(response).contains("\uD83D\uDCCC Scientific Reasoning (agronomy explanation)");
        assertThat(response).contains("\uD83D\uDCA1 ICAR Recommendation (field practice)");
        assertThat(response).contains("\u26A0\uFE0F Advisory Notes (warnings + best practices)");
    }

    @Test
    void irrigationIntentDoesNotSwitchToCropRecommendationWhenCropWordsAppear() {
        String response = client.getAIResponse("How much water should I give cotton crop in black soil?");

        assertThat(response).contains("7 to 10 days");
        assertThat(response).doesNotContain("top 3 suitable crops");
    }
}
