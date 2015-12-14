package br.com.deroldo.cache.redis.jedis;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import redis.clients.jedis.JedisPool;
import br.com.deroldo.cache.redis.properties.ApplicationProperties;
import br.com.deroldo.cache.redis.properties.CacheProperties;

public class JedisFactory implements Serializable{
	private static final long serialVersionUID = -4037943715293182401L;

	@Inject
	private ApplicationProperties applicationProperties;
	
	@Produces @ApplicationScoped
    public JedisPool getJedisPool(){
		CacheProperties cacheProperties = this.applicationProperties.getCacheProperties();
        return new JedisPool(new GenericObjectPoolConfig(), cacheProperties.getHost(), cacheProperties.getPort(), cacheProperties.getTtl());
    }
 
    public void detroy(@Disposes JedisPool jedisPool){
        jedisPool.destroy();
    }

}
