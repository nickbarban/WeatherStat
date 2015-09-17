package com.fabrika.weather_stat.redis;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import com.fabrika.weather_stat.data.City;
import com.fabrika.weather_stat.data.Weather;
import com.fabrika.weather_stat.weather_service.OpenWeatherMapService;
import com.google.common.base.Preconditions;

@Slf4j
public final class CacheRedisServiceImpl {

	/**
	 * Provides add and get operations with Redis cache
	 * Can check if element of cache is expired
	 */
	
	// timeout in seconds
	public static int DATA_LIFE_TIME = 30;
	
	// if cache contains not expired weather for asked city so method returns new weather object from saved data
	// else it returns null
	public static Weather checkWeather(City city) {
		Preconditions.checkNotNull(city);
		String key = "weather:" + city.getName() + city.getCountry();
		Weather result = null;
		if (RedisService.isExpired(key) > 0) {
			result = getWeather(city);
		} 
		return result;		
	}

	// add new weather to cache.
	// if such weather exists in cache it will be overwritten
	// defines new timeout for adding weather
	// returns OK if adding and setting timeout is successful
	// returns NOK if adding and setting timeout is UNsuccessful
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static String addNewWeather(Weather weather) {
		Preconditions.checkNotNull(weather);
		Preconditions.checkNotNull(weather.getCity());
		Preconditions.checkNotNull(weather.getCity().getName());
		Preconditions.checkNotNull(weather.getCity().getCountry());
		Preconditions.checkNotNull(weather.getTemp());
		Preconditions.checkArgument(weather.getCity().getName().length() > 0);
		Preconditions.checkArgument(weather.getCity().getCountry().length() > 0);
		Map<String, String> values = new HashMap();
		values.put("temp", weather.getTemp().toString());
		String key = "weather:" + weather.getCity().getName() + weather.getCity().getCountry();
		String addResult =  RedisService.add(key, values);
		if (addResult.equalsIgnoreCase("OK")) {
			if (RedisService.setExpire(key, DATA_LIFE_TIME) != 1) {
				addResult = "NOK";
			}
		}		
		log.info(CacheRedisServiceImpl.class.getName() + 
				": add new weather to REDIS hash: " + key + 
				" ; result: " + addResult);
		return addResult;
	}
	
	// if cache containes weather with specified city so method gets data from cache 
	// ans creates new weather object form the data
	// if cache does not contains weather with specified city so method returns null
	private static Weather getWeather(City city) {
		Preconditions.checkNotNull(city);
		Preconditions.checkNotNull(city.getName());
		Preconditions.checkNotNull(city.getCountry());
		String key = "weather:" + city.getName() + city.getCountry();
		Map<String, String> values = RedisService.getAll(key);
		Weather result = null;
		if (values != null && values.size() > 0) {
			result = new Weather();
			result.setCity(city);
			try {
				result.setTemp(BigDecimal.valueOf(Double.parseDouble(values.get("temp"))));
			} catch (NumberFormatException e) {
				result = null;
			}
		}
		log.info(CacheRedisServiceImpl.class.getName() + 
				": get weather from REDIS hash: " + key + 
				" ; result: " + result);
		return result;
	}
	
	

}
