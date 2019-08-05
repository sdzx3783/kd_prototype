package com.kingdee.prototype.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;

@Component("simpleCacheService")
public class SimpleCacheService {

	@Autowired
	private StringRedisTemplate redisTemplate;
	
	public Set<String> keys(String pattern){
		return redisTemplate.keys(pattern);
	}
	
	public boolean expire(String key,long timeout){
		return redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
	}
	
	public long increment(String key,long delta){
		return redisTemplate.opsForValue().increment(key, delta);
	}
	
	public <T> void set(String key,T val){
		if(!StringUtils.isEmpty(val)){
			redisTemplate.opsForValue().set(key, JSON.toJSONString(val));
		}
	}
	
	public <T> void add(String key,T val,long expire){
		if(!StringUtils.isEmpty(val)){
			redisTemplate.opsForValue().set(key, JSON.toJSONString(val),expire,TimeUnit.SECONDS);
		}
	}
	
	public void del(String key){
		redisTemplate.delete(key);
	}
	
	public <T> T get(String key,Class<T> clazz){
		String json = redisTemplate.opsForValue().get(key);
		if(!StringUtils.isEmpty(json)){
			return JSON.parseObject(json, clazz);
		}
		return null;
	}
	
	public <T> void hAdd(String key,String hashKey,T val){
		if(!StringUtils.isEmpty(val)){
			redisTemplate.opsForHash().put(key, hashKey, JSON.toJSONString(val));
		}
	}
	
	public <T> T hGet(String key,String hashKey,Class<T> clazz){
		Object obj = redisTemplate.opsForHash().get(key, hashKey);
		if(obj != null){
			return JSON.parseObject(obj.toString(), clazz);
		}
		return null;
	}
	
	public <T> List<T> hGetArray(String key,String hashKey,Class<T> clazz){
		Object obj = redisTemplate.opsForHash().get(key, hashKey);
		if(obj != null){
			return JSON.parseArray(obj.toString(), clazz);
		}
		return null;
	}

	public <T> Map<String,T> hGetAll(String key,Class<T> clazz){
		Map<Object, Object> map = redisTemplate.opsForHash().entries(key);
		if(map != null){
			Map<String,T> vals = new HashMap<String,T>();
			for (Object k : map.keySet()) {
				String val = (String)map.get(k);
				vals.put(k.toString(),JSON.parseObject(val, clazz));
			}
			return vals;
		}
		return null;
	}
	
	public Set<String> hKeys(String key){
		Set<Object> keys = redisTemplate.opsForHash().keys(key);
		if(keys != null){
			Set<String> vals = new HashSet<String>();
			for (Object k : keys) {
				vals.add(k.toString());
			}
			return vals;
		}
		return Collections.EMPTY_SET;
	}
	
	public <T> List<T> hVals(String key,Class<T> clazz){
		List<Object> objs = redisTemplate.opsForHash().values(key);
		if(objs != null){
			List<T> vals = new ArrayList<T>();
			for (Object obj : objs) {
				vals.add(JSON.parseObject(obj.toString(), clazz));
			}
			return vals;
		}
		return Collections.EMPTY_LIST;
	}
	
	public long hDel(String key,String hashKey){
		return  redisTemplate.opsForHash().delete(key, hashKey);
	}
	
	
	public <T> void sAdd(String key,T val){
		if(!StringUtils.isEmpty(val)){
			redisTemplate.opsForSet().add(key, JSON.toJSONString(val));
		}
	}
	
	public <T> T sGet(String key,Class<T> clazz){
		String json = redisTemplate.opsForSet().pop(key);
		if(!StringUtils.isEmpty(json)){
			return JSON.parseObject(json, clazz); 
		}
		return null;
	}
	
	public <T> long sDel(String key,T val){
		return redisTemplate.opsForSet().remove(key, val);
	}
	
	public <T> void lPush(String key,T val){
		if(!StringUtils.isEmpty(val)){
			redisTemplate.opsForList().leftPush(key, JSON.toJSONString(val));
		}
	}
	
	public <T> void rPush(String key,T val){
		if(!StringUtils.isEmpty(val)){
			redisTemplate.opsForList().rightPush(key, JSON.toJSONString(val));
		}
	}
	
	public <T> T lGet(String key,Class<T> clazz){
		String json = redisTemplate.opsForList().rightPop(key);
		if(!StringUtils.isEmpty(json)){
			return JSON.parseObject(json, clazz); 
		}
		return null;
	}
	
	public long lDelAll(String key){
		return redisTemplate.opsForList().remove(key, 0, -1);
	}
}
