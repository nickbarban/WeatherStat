package com.fabrika.weather_stat.redis;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import com.fabrika.weather_stat.data.City;
import com.google.common.base.Preconditions;

@Slf4j
public final class RedisDAO {

	/**
	 * Provides add and get operations with Redis cache
	 * Can check if element of cache is expired
	 */
	
	// timeout in seconds
	public int DATA_LIFE_TIME = 0;
	
	// if cache contains not expired weather for asked city so method returns new weather object from saved data
	// else it returns null
	public synchronized City checkWeather(City city) {
		Preconditions.checkNotNull(city);
		String key = "weather:" + city.getId() + city.getCountry();
		City result = null;
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
	public synchronized String addNewWeather(City city) {
		Preconditions.checkNotNull(city);
		Preconditions.checkNotNull(city.getName());
		Preconditions.checkNotNull(city.getCountry());
		Preconditions.checkNotNull(city.getTemp());
		Preconditions.checkArgument(city.getName().length() > 0);
		Preconditions.checkArgument(city.getCountry().length() > 0);
		Map<String, String> values = new HashMap();
		values.put("name", city.getName());
		values.put("country", city.getCountry());
		values.put("temp", city.getTemp().toString());
		values.put("time", String.valueOf(System.currentTimeMillis()));
		String key = "weather:" + city.getId();
		String addResult =  RedisService.add(key, values);
		if (addResult.equalsIgnoreCase("OK")) {
			if (RedisService.setExpire(key, DATA_LIFE_TIME) != 1) {
				addResult = "NOK";
			}
		}		
		log.info(RedisDAO.class.getName() + 
				": add new weather to REDIS hash: " + key + 
				" ; result: " + addResult);
		return addResult;
	}
	
	// if cache containes weather with specified city so method gets data from cache 
	// ans creates new weather object form the data
	// if cache does not contains weather with specified city so method returns null
	private City getWeather(City city) {
		Preconditions.checkNotNull(city);
		Preconditions.checkNotNull(city.getName());
		Preconditions.checkNotNull(city.getCountry());
		Preconditions.checkArgument(city.getId() != 0);
		String key = "weather:" + city.getId();
		Map<String, String> values = RedisService.getAll(key);
		//City result = null;
		if (values != null && values.size() > 0) {
			try {
				city.setTemp(BigDecimal.valueOf(Double.parseDouble(values.get("temp"))));
				city.setTemp(BigDecimal.valueOf(Double.parseDouble(values.get("time"))));
			} catch (NumberFormatException e) {
				city = null;
			}
		}
		log.info(RedisDAO.class.getName() + 
				": get weather from REDIS hash: " + key + 
				" ; result: " + city);
		return city;
	}
	
	

}
