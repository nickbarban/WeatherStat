package com.fabrika.weather_stat.base_service;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fabrika.weather_stat.data.City;
import com.fabrika.weather_stat.repository.WeatherRepository;

@Slf4j
@Service
@Transactional
public class WeatherBaseService {

    @Autowired
    private WeatherRepository repository;

    public City save(City city) {
        log.info("Save weather " + city);
        return repository.saveAndFlush(city);
    }

    public List<City> getAll() {
        log.info("Get all weather");
        return repository.findAll();
    }
}
