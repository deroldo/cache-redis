package br.com.deroldo.cache.redis.properties;

import java.io.Serializable;

public interface ApplicationProperties extends Serializable{
	
	CacheProperties getCacheProperties();

}
