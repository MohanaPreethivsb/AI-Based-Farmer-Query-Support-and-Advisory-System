package com.farmadvisory.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/tip")
public class TipController {

    private static final String[] TIPS = {
            "Water crops early in the morning to reduce evaporation.",
            "Remove weeds regularly to improve crop growth.",
            "Test soil before applying fertilizers.",
            "Inspect leaves every morning for pest attacks.",
            "Use drip irrigation to save water.",
            "Rotate crops every season to improve soil fertility.",
            "Avoid overwatering your crops.",
            "Apply organic compost whenever possible.",
            "Monitor weather before spraying pesticides.",
            "Harvest crops at the correct maturity stage."
    };

    @GetMapping("/daily-tip")
    public ResponseEntity<Map<String, Object>> getDailyTip() {
        int index = LocalDate.now().getDayOfMonth() % TIPS.length;
        return ResponseEntity.ok(Map.of(
                "success", true,
                "tip", TIPS[index]
        ));
    }
}
