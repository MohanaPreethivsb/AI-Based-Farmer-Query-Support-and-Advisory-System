package com.farmadvisory.controller;

import com.farmadvisory.dto.WeatherResponse;
import com.farmadvisory.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping
    public ResponseEntity<WeatherResponse> getWeather(
            @RequestParam(value = "lat") Double latitude,
            @RequestParam(value = "lon") Double longitude) {
        WeatherResponse weatherResponse = weatherService.getWeather(latitude, longitude);
        return ResponseEntity.ok(weatherResponse);
    }
}
