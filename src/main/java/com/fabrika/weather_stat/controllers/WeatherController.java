package com.fabrika.weather_stat.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fabrika.weather_stat.base_service.WeatherBaseService;
import com.fabrika.weather_stat.config.H2Config;
import com.fabrika.weather_stat.data.City;
import com.fabrika.weather_stat.data.Weather;
import com.fabrika.weather_stat.weather_service.OpenWeatherMapService;
import com.fabrika.weather_stat.weather_service.WeatherService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@ContextConfiguration(classes = { H2Config.class, WeatherBaseService.class })
public class WeatherController {

    @Autowired
    private WeatherBaseService baseService;

    @RequestMapping(value = "/weather", method = RequestMethod.POST)
    @ResponseBody
    public List<Weather> saveWeather(@RequestBody City[] cities) {
        log.info("Start servlet");

        WeatherService service = new OpenWeatherMapService();

        for (City city : cities) {
            log.info("Start to get weather for city " + city.getName());
            baseService.save(service.getWeather(city));
        }

        return baseService.getAll();
    }

}
