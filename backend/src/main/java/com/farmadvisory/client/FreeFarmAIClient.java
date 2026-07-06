package com.farmadvisory.client;

import org.springframework.stereotype.Component;

@Component
public class FreeFarmAIClient {

    public String getAIResponse(String question) {

        String q = question == null ? "" : question.toLowerCase();

        return switch (classifyIntent(q)) {
            case IRRIGATION -> irrigationAdvice(q);
            case FERTILIZER -> fertilizerAdvice();
            case PEST_CONTROL -> pestAdvice(q);
            case CROP_RECOMMENDATION -> cropSelectionAdvice(q);
            case GENERAL_FARMING -> generalAdvice(q);
        };
    }

    private String cropSelectionAdvice(String q) {

        if (q.contains("clay") || q.contains("heavy rain") || q.contains("high rainfall") || q.contains("waterlogging")) {
            return section(
                    "For clay soil or high-rainfall conditions, the top 3 suitable crops are paddy, maize, and pulses.",
                    "Clay soil holds water for a long time. Paddy benefits from standing water, maize can adapt if drainage is fair, and pulses can improve soil nitrogen after the wet crop cycle.",
                    "Choose paddy if water is reliable, maize if drainage is moderate, and pulses if you want to improve soil fertility with lower input cost.",
                    "Avoid cotton in waterlogged fields because excess water can damage roots."
            );
        }

        if (q.contains("sandy") || q.contains("low rainfall") || q.contains("drought") || q.contains("dry")) {
            return section(
                    "For sandy soil or dry conditions, the top 3 suitable crops are millets, pulses, and groundnut.",
                    "Sandy soil loses water quickly, so crops with low water demand perform better. Millets tolerate drought, pulses need less water, and groundnut suits well-drained light soils.",
                    "Prefer millets where irrigation is limited, pulses for soil improvement, and groundnut when the soil is loose and drainage is good.",
                    "Use mulching to reduce moisture loss from sandy soil."
            );
        }

        if (q.contains("black soil") || q.contains("cotton")) {
            return section(
                    "For black soil, the top 3 suitable crops are cotton, soybean, and pulses.",
                    "Black soil stores moisture well and supports deep-rooted crops. Cotton grows well when waterlogging is avoided, soybean suits monsoon conditions, and pulses help maintain nitrogen.",
                    "Choose cotton only where drainage is good. Use soybean or pulses when you want shorter-duration and lower-risk options.",
                    "Do not over-irrigate black soil because it can become sticky and poorly aerated."
            );
        }

        return section(
                "For general Indian farming conditions, the top 3 crop choices are maize, pulses, and millets.",
                "Maize adapts to many soil types, pulses improve soil nitrogen, and millets handle heat and low rainfall better than many cereals.",
                "Select maize if rainfall is moderate, pulses if soil fertility is low, and millets if water is limited.",
                "Millets are drought resistant and need less water. Match the crop with local season, soil type, and irrigation availability."
        );
    }

    private String fertilizerAdvice() {
        return section(
                "Use balanced NPK and apply nitrogen in split doses instead of giving all urea at one time.",
                "Split nitrogen reduces nutrient loss and helps the crop use fertilizer steadily. Balanced NPK supports root growth, leaf growth, flowering, and yield.",
                "Apply organic manure before sowing, give basal NPK as per soil test or local recommendation, and split urea into 2 to 3 doses during crop growth.",
                "Avoid excess urea because it can make plants soft, increase pest risk, and reduce long-term soil health."
        );
    }

    private String pestAdvice(String q) {
        if (q.contains("disease") || q.contains("fungus") || q.contains("blight") || q.contains("rot")) {
            return section(
                    "Follow IPM for disease control: inspect the field, remove infected parts, improve aeration, and spray only if disease is severe.",
                    "Diseases spread faster when infected leaves, stems, or fruits remain in humid fields. Correct identification avoids wrong chemical use and protects beneficial organisms.",
                    "Step 1: Monitor the crop and note symptoms. Step 2: Remove infected parts. Step 3: Use neem-based or suitable biological support where applicable. Step 4: Use fungicide or bactericide only in severe cases as per label dose.",
                    "Do not spray blindly. Avoid spraying before rain, keep the recommended harvest interval, and prevent waterlogging because poor root oxygen increases crop stress."
            );
        }

        return section(
                "Use Integrated Pest Management first; chemical pesticide should be the last option when infestation is severe.",
                "Early monitoring stops pest spread before economic damage. Neem oil, biological control, and conservation of predators reduce pest pressure while protecting useful insects.",
                "Step 1: Inspect leaves, shoots, flowers, and lower canopy regularly. Step 2: Remove egg masses and heavily infested plant parts. Step 3: Use neem oil or biological control and protect natural predators. Step 4: Apply a recommended chemical pesticide only if pest load remains severe.",
                "Avoid excess urea because soft, lush growth can increase pest attacks. Spray in the evening, follow label dose, and avoid repeated use of the same chemical group."
        );
    }

    private String irrigationAdvice(String q) {

        if (q.contains("cotton")) {
            return section(
                    "For cotton, irrigate every 7 to 10 days, adjusting for rainfall, soil type, and crop stage.",
                    "Cotton needs steady moisture, especially during flowering and boll formation. Clay soil holds water longer, while sandy soil dries faster.",
                    "In clay or black soil, keep the interval closer to 10 days if soil is moist. In sandy soil or hot weather, irrigate closer to 7 days with lighter watering.",
                    "Do not irrigate just before heavy rain. Avoid waterlogging because cotton roots can suffer when soil stays wet."
            );
        }

        if (q.contains("sandy")) {
            return section(
                    "Sandy soil needs lighter and more frequent irrigation.",
                    "Sandy soil drains quickly, so water moves below the root zone faster than crops can use it.",
                    "Irrigate in smaller amounts and use mulching to hold moisture.",
                    "Avoid large single irrigations because much of the water may be wasted."
            );
        }

        if (q.contains("clay") || q.contains("black soil")) {
            return section(
                    "Clay soil needs less frequent irrigation because it holds water for a longer time.",
                    "Water stays near the root zone longer in clay soil, so frequent irrigation can reduce air around roots and increase disease risk.",
                    "Check soil moisture before watering and irrigate only when the upper soil starts drying. Increase care during flowering or fruiting stages.",
                    "Avoid overwatering and skip irrigation when good rainfall is expected."
            );
        }

        return section(
                "Irrigate according to soil moisture, crop stage, and weather.",
                "Clay soil stores water longer, while sandy soil dries quickly. Overwatering can reduce oxygen near roots and increase disease risk.",
                "Give lighter, more frequent irrigation in sandy soil and less frequent irrigation in clay soil.",
                "Avoid irrigation just before heavy rain and check soil moisture before watering."
        );
    }

    private String generalAdvice(String q) {

        if (q.contains("nitrogen") || q.contains("fertility")) {
            return section(
                    "Improve soil fertility with compost, farmyard manure, and a soil test-based nutrient plan.",
                    "Organic matter improves soil structure and supports useful microbes. A soil test shows which nutrients are low.",
                    "Add compost or farmyard manure before sowing and use the soil test report to decide fertilizer quantity.",
                    "Do not guess fertilizer quantity every season. Wrong doses can waste money and reduce soil health."
            );
        }

        return section(
                "Healthy soil needs balanced nutrients, organic matter, drainage, and proper moisture.",
                "Roots grow better when soil has air spaces, useful microbes, and enough but not excess water.",
                "Add compost, rotate crops, avoid excess urea, and prevent waterlogging.",
                "Test soil pH and nutrients once per season if possible."
        );
    }

    private Intent classifyIntent(String q) {
        if (isIrrigationIntent(q)) {
            return Intent.IRRIGATION;
        }

        if (containsAny(q, "fertilizer", "fertiliser", "urea", "npk", "nutrient", "nutrients", "manure", "compost", "nitrogen", "phosphorus", "potassium")) {
            return Intent.FERTILIZER;
        }

        if (containsAny(q, "pest", "insect", "borer", "disease", "fungus", "fungal", "blight", "wilt", "rot", "aphid", "whitefly", "caterpillar", "damage")) {
            return Intent.PEST_CONTROL;
        }

        if (isCropRecommendationIntent(q)) {
            return Intent.CROP_RECOMMENDATION;
        }

        return Intent.GENERAL_FARMING;
    }

    private boolean isIrrigationIntent(String q) {
        return containsAny(q, "irrigation", "irrigate", "watering", "water my crop", "water the crop", "water schedule", "water interval", "how often water", "how much water") ||
                ((q.contains("water") || q.contains("rain") || q.contains("rainfall")) &&
                        containsAny(q, "schedule", "frequency", "interval", "how often", "when should", "how much", "give", "apply"));
    }

    private boolean isCropRecommendationIntent(String q) {
        return containsAny(q, "which crop", "what crop", "suitable crop", "recommend crop", "crop recommendation", "crop to grow", "best crop", "grow in", "grow on", "plant in", "sow in");
    }

    private boolean containsAny(String q, String... keywords) {
        for (String keyword : keywords) {
            if (q.contains(keyword)) {
                return true;
            }
        }
        return false;
    }

    private String section(String answer, String reasoning, String recommendation, String tips) {
        return "\uD83C\uDF3E Answer (direct solution)\n" +
                answer + "\n\n" +
                "\uD83D\uDCCC Scientific Reasoning (agronomy explanation)\n" +
                reasoning + "\n\n" +
                "\uD83D\uDCA1 ICAR Recommendation (field practice)\n" +
                recommendation + "\n\n" +
                "\u26A0\uFE0F Advisory Notes (warnings + best practices)\n" +
                tips;
    }

    private enum Intent {
        IRRIGATION,
        FERTILIZER,
        PEST_CONTROL,
        CROP_RECOMMENDATION,
        GENERAL_FARMING
    }
}
