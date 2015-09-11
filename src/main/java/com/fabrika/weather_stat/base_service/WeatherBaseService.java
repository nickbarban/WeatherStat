package com.fabrika.weather_stat.base_service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fabrika.weather_stat.data.Weather;
import com.fabrika.weather_stat.repository.WeatherRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class WeatherBaseService {

    @Autowired
    private WeatherRepository repository;

    public Weather save(Weather weather) {
        log.info("Save weather " + weather);
        return repository.saveAndFlush(weather);
    }

    public List<Weather> getAll() {
        log.info("Get all weather");
        return repository.findAll();
    }
}
