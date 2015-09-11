package com.fabrika.weather_stat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fabrika.weather_stat.data.Weather;

@Repository
@Transactional
public interface WeatherRepository extends JpaRepository<Weather, Integer> {

}
