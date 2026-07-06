package com.farmadvisory.client;

import com.farmadvisory.dto.WeatherResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class WeatherClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${weather.api-key:}")
    private String apiKey;

    private static final String WEATHER_API_URL =
            "https://api.openweathermap.org/data/2.5/weather";

    public WeatherClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public WeatherResponse getWeather(Double latitude, Double longitude) {
        try {
            if (apiKey == null || apiKey.isEmpty()) {
                log.warn("Weather API key not configured");
                return getFallbackWeather();
            }

            String url = String.format(
                    "%s?lat=%f&lon=%f&appid=%s&units=metric",
                    WEATHER_API_URL,
                    latitude,
                    longitude,
                    apiKey
            );

            System.out.println("Latitude : " + latitude);
            System.out.println("Longitude: " + longitude);
            System.out.println("API Key  : " + apiKey);
            System.out.println("URL      : " + url);

            String response = restTemplate.getForObject(url, String.class);

            System.out.println("Response : " + response);

            return parseWeatherResponse(response);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error fetching weather data", e);
            return getFallbackWeather();
        }
    }

    private WeatherResponse parseWeatherResponse(String response) throws Exception {
        JsonNode root = objectMapper.readTree(response);

        return WeatherResponse.builder()
                .city(root.path("name").asText())
                .temperature(root.path("main").path("temp").asDouble())
                .humidity(root.path("main").path("humidity").asInt())
                .condition(root.path("weather").get(0).path("description").asText())
                .build();
    }

    private WeatherResponse getFallbackWeather() {
        return WeatherResponse.builder()
                .city("Local area")
                .temperature(0.0)
                .humidity(0)
                .condition("Weather unavailable")
                .build();
    }
}
