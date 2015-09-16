package com.fabrika.weather_stat.redis;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import redis.clients.jedis.Jedis;

import com.fabrika.weather_stat.data.City;
import com.google.common.base.Preconditions;

@Slf4j
public class RedisService {

	/**
	 * Provides base operations with Redis cache  
	 */
	// server's link
	public static String HOST = "localhost";
	
	// redis' String type can store not more 512 megabytes (512 * 1024 * 1024 * 8)
	private static final long MAX_KEY_SIZE = 4294967296l;
	
	// method adds values to redis hash (map) 
	// String key = "className" + ":" + "objectIDOrName"
	// Map<String, String>values = map of fields (String key = "fieldName", String value = "fieldValue")
	// returns "OK" if successful
	// returns "NOK" if UNsuccessful
	public static final String add(String key, Map<String, String>values) {
		Preconditions.checkNotNull(key);
		Preconditions.checkArgument((key.toCharArray().length * Character.SIZE) < MAX_KEY_SIZE);
		Preconditions.checkNotNull(values);
		Preconditions.checkArgument(values.size() > 0);
		Jedis jedis = new Jedis(HOST);
		String result = null;
		try {
			result = jedis.hmset(key, values);
		} catch (Exception e) {
			log.warn("Can't add to REDIS hash: " + key + " " + values, e);
			result = "NOK";
		} 
		jedis.close();
		return result;				
	}
	
	// String key = "className" + ":" + "objectIDOrName"
	// returns all values of specified object
	// returns null if there is no connection 
	public static final Map<String, String> getAll(String key) {
		Preconditions.checkNotNull(key);
		Preconditions.checkArgument((key.toCharArray().length * Character.SIZE) < MAX_KEY_SIZE);
		Jedis jedis = new Jedis(HOST);
		Map<String, String> result = null;
		try {
			result = jedis.hgetAll(key);
		} catch (Exception e) {
			log.warn("Can't get all fields of REDIS hash: " + key, e);
		}
		jedis.close();
		return result;
	}
	
	// String key = "className" + ":" + "objectIDOrName"
	// returns all values of specified object
	// returns null if no connection
	public static final String getValue(String key, String fieldName) {
		Preconditions.checkNotNull(key);
		Preconditions.checkArgument((key.toCharArray().length * Character.SIZE) < MAX_KEY_SIZE);
		Jedis jedis = new Jedis(HOST);
		String result;
		try {
			result = jedis.hget(key, fieldName);
		} catch (Exception e) {
			log.warn("Can't get field: " + fieldName + " of REDIS hash: " + key, e);
			result = null;
		}
		jedis.close();
		return result;
	}
	
	// sets timeout for element
	// returns 1 if timeout is set for key
	// returns 0 if key does not exist
	// returns -1 if there is no connection
	public static final int setExpire(String key, int seconds) {
		Preconditions.checkNotNull(key);
		Preconditions.checkArgument((key.toCharArray().length * Character.SIZE) < MAX_KEY_SIZE);
		Preconditions.checkArgument(seconds >= 0);
		Jedis jedis = new Jedis(HOST);
		long result;
		try {
			result = jedis.expire(key, seconds);
		} catch (Exception e) {
			log.warn("Can't set expire of REDIS hash: " + key, e);
			result = -1;
		}
		jedis.close();
		return (int) result;
	}
	
	// checks expire timeout for specified element
	// returns -1 if key does not have expire timeout
	// returns -2 if key does not exist
	// returns -3 if there is no connection
	public static final int isExpired(String key){
		Preconditions.checkNotNull(key);
		Preconditions.checkArgument((key.toCharArray().length * Character.SIZE) < MAX_KEY_SIZE);
		Jedis jedis = new Jedis(HOST);
		long result;
		try {
			result = jedis.ttl(key);
		} catch (Exception e) {
			log.warn("Can't get expire timeout of REDIS hash: " + key, e);
			result = -3;
		}
		jedis.close();
		return (int) result;
	}
	
	

}
