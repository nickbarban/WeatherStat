package com.fabrika.weather_stat.controllers;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

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
import com.fabrika.weather_stat.weather_service.OpenWeatherMapService;
import com.fabrika.weather_stat.weather_service.WeatherService;
import com.fabrika.weather_stat.redis.RedisDAO;

@Slf4j
@Controller
@ContextConfiguration(classes = { H2Config.class, WeatherBaseService.class })
public class WeatherController {

    @Autowired
    private WeatherBaseService baseService;
    
    private RedisDAO redisDAO = new RedisDAO();

    @SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/weather", method = RequestMethod.POST)
    @ResponseBody
    public List<City> saveWeatherToBaseService(@RequestBody City[] cities) {
        log.info("Start servlet");

        WeatherService service = new OpenWeatherMapService();

        for (City city : cities) {
            log.info("Start to get weather for city " + city.getName());
            baseService.save(service.getWeather(city));
        }

        return baseService.getAll();
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/weather", method = RequestMethod.POST)
    @ResponseBody
    public List<City> saveWeatherToCache(@RequestBody City[] cities) {
        log.info("Start servlet");

        redisDAO.DATA_LIFE_TIME = 30;

        for (City city : cities) {
            log.info("Start to get weather from redis server for city " + city.getName());
            city = redisDAO.checkWeather(city);
        }        

        return new LinkedList(Arrays.asList(cities));
    }

}
