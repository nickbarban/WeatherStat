package com.fabrika.weather_stat.weather_service;

import com.fabrika.weather_stat.data.City;
import com.fabrika.weather_stat.data.Weather;

public interface WeatherService {
    public Weather getWeather(City city);
}
