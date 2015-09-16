package com.fabrika.weather_stat.weather_service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import org.springframework.web.client.RestTemplate;

import com.fabrika.weather_stat.data.City;
import com.fabrika.weather_stat.data.Weather;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OpenWeatherMapService implements WeatherService {

    private static String getURL(City city) {
        return String.format("http://api.openweathermap.org/data/2.5/weather?q=%s,%s&units=metric", city.getName(), city.getCountry());
    }

    @Override
    public Weather getWeather(City city) {

        RestTemplate restTemplate = new RestTemplate();
        String jsonResult = restTemplate.getForObject(getURL(city), String.class);
        Map<String, Object> userData = getJsonMap(jsonResult);

        BigDecimal temp = findTemp(userData);

        Weather weather = new Weather();
        weather.setCity(city);
        weather.setTemp(temp);
        return weather;
    }

    private BigDecimal findTemp(Map<String, Object> userData) {
        @SuppressWarnings("unchecked")
        Object obj = ((Map<String, ?>) userData.get("main")).get("temp");
        if (obj instanceof Integer) {
            return new BigDecimal((int) obj).setScale(2, RoundingMode.HALF_UP);
        }
        return new BigDecimal((double) obj).setScale(2, RoundingMode.HALF_UP);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getJsonMap(String jsonResult) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(jsonResult, Map.class);
        } catch (IOException exception) {
            log.warn("Can`t map json file", exception);
            throw new RuntimeException("Can`t map json file");
        }
    }

}
