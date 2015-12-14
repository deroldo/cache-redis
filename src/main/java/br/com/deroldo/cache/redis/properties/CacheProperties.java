package br.com.deroldo.cache.redis.properties;

public interface CacheProperties {
	
	String CACHE_REDIS_PROPERTIES_NAME = "cache-redis.properties";
	String CACHE_REDIS_PROPERTIES_EXTERNAL_PATH = "cache.redis.external.path";
	
	String CACHE_REDIS_ENABLED_KEY = "cache.redis.enabled";
	String CACHE_REDIS_HOST_KEY = "cache.redis.host";
	String CACHE_REDIS_PORT_KEY = "cache.redis.port";
	String CACHE_REDIS_APPLICATION_TTL_KEY = "cache.redis.application.ttl";
	
	boolean isEnabled();
	Integer getTtl();
	String getHost();
	Integer getPort();

}
