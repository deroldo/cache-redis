package br.com.deroldo.cache.redis.repository;

import java.io.Serializable;

public interface CacheRepository extends Serializable{
	
	boolean set(String key, String json);
	boolean set(String key, String json, int ttl);
	String get(String key);
	boolean expire(String key);
	boolean expireAll();

}
