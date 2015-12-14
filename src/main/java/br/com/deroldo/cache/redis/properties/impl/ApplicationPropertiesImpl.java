package br.com.deroldo.cache.redis.properties.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.deroldo.cache.redis.annotation.Cache;
import br.com.deroldo.cache.redis.interceptor.CacheInterceptor;
import br.com.deroldo.cache.redis.properties.ApplicationProperties;
import br.com.deroldo.cache.redis.properties.CacheProperties;

public class ApplicationPropertiesImpl implements ApplicationProperties{
	private static final long serialVersionUID = 1201137569909686445L;
	
	private Logger logger = LoggerFactory.getLogger(ApplicationPropertiesImpl.class);

	@Override
	public CacheProperties getCacheProperties() {
		CacheProperties cacheProperties = null;
		
		Properties properties = new Properties();
		
		try {
			properties.load(CacheInterceptor.class.getClassLoader().getResourceAsStream(CacheProperties.CACHE_REDIS_PROPERTIES_NAME));
			
			Object externalPathPropertie = properties.get(CacheProperties.CACHE_REDIS_PROPERTIES_EXTERNAL_PATH);
			
			if (externalPathPropertie != null){
				String externalPath = String.valueOf(externalPathPropertie);
				if (!externalPath.endsWith("/")){
					externalPath += "/";
				}
				File externalPropertiesFile = new File(externalPath + CacheProperties.CACHE_REDIS_PROPERTIES_NAME);
				properties.load(new FileInputStream(externalPropertiesFile));
			}
			
			Boolean enabled = Boolean.valueOf(String.valueOf(properties.get(CacheProperties.CACHE_REDIS_ENABLED_KEY)));
			String host = String.valueOf(properties.get(CacheProperties.CACHE_REDIS_HOST_KEY));
			Integer port = Integer.valueOf(String.valueOf(properties.get(CacheProperties.CACHE_REDIS_PORT_KEY)));
			
			Integer applicationTtl = null;
			
			try {
				applicationTtl = Integer.valueOf(String.valueOf(properties.get(CacheProperties.CACHE_REDIS_APPLICATION_TTL_KEY)));
			} catch (Exception e) {
				applicationTtl = Cache.DEFAULT_METHOD_TTL;
				this.logger.info(String.format("The %s key not defined", CacheProperties.CACHE_REDIS_APPLICATION_TTL_KEY));
			}
			
			cacheProperties = new CachePropertiesImpl(enabled, host, port, applicationTtl);
		} catch (NumberFormatException e) {
			this.logger.info(String.format("The %s properties can be a number", CacheProperties.CACHE_REDIS_APPLICATION_TTL_KEY));
		} catch (IOException e) {
			this.logger.info(String.format("The %s not found on application resources", CacheProperties.CACHE_REDIS_PROPERTIES_NAME));
		} catch (Exception e) {
			this.logger.error(String.format("The properties keys are needs: %s, %s, %s",
					CacheProperties.CACHE_REDIS_ENABLED_KEY,
					CacheProperties.CACHE_REDIS_HOST_KEY,
					CacheProperties.CACHE_REDIS_PORT_KEY));
		}
		
		if (cacheProperties == null){
			cacheProperties = new CachePropertiesImpl(false, null, null, null);
		}
		
		return cacheProperties;
	}

}
