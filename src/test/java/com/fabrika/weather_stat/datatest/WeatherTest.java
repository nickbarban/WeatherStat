package com.fabrika.weather_stat.datatest;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.fabrika.weather_stat.base_service.WeatherBaseService;
import com.fabrika.weather_stat.config.H2Config;
import com.fabrika.weather_stat.data.City;
import com.fabrika.weather_stat.data.Weather;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@DirtiesContext
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { H2Config.class, WeatherBaseService.class })
@WebAppConfiguration
public class WeatherTest {
    @Resource
    private EntityManagerFactory emf;
    protected EntityManager em;

    @Autowired
    private WeatherBaseService service;

    @Before
    public void setUp() throws Exception {
        log.info("Init entity manager");
        em = emf.createEntityManager();
    }

    private Weather saveTestWeather() {
        City city = new City();
        city.setName("Odessa");
        city.setCountry("UA");
        Weather weather = new Weather();
        weather.setCity(city);
        weather.setTemp(new BigDecimal(19.38));
        //weather.setTime(System.currentTimeMillis());
        return service.save(weather);
    }

    @Test
    @Transactional
    public void saveWetherTest() throws Exception {

        Weather weather = saveTestWeather();
        log.info("Save city " + weather.toString());
        //assertNotNull(weather.getId());
        assertNotNull(weather.getCity());
        assertNotNull(weather.getTemp());
    }

    @Test
    @Transactional
    public void getAllWeather() {
        int size = service.getAll().size();
        log.info("Table size " + size);
        saveTestWeather();
        List<Weather> weathers = service.getAll();
        log.info("Table size " + weathers.size());
        assertNotEquals(size, weathers.size());
    }
}
