package com.fabrika.weather_stat.weather_service;

import com.fabrika.weather_stat.data.City;

public interface WeatherService {
    public City getWeather(City city);
}
