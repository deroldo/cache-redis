package br.com.deroldo.cache.redis.jedis;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import br.com.deroldo.cache.redis.properties.CacheProperties;
import redis.clients.jedis.JedisPool;

public class JedisFactory implements Serializable{
	private static final long serialVersionUID = -4037943715293182401L;

	@Inject
	private CacheProperties cacheProperties;
	
	@Produces @ApplicationScoped
    public JedisPool getJedisPool(){
        return new JedisPool(new GenericObjectPoolConfig(), this.cacheProperties.getHost(), this.cacheProperties.getPort(), this.cacheProperties.getTtl());
    }
 
    public void detroy(@Disposes JedisPool jedisPool){
        jedisPool.destroy();
    }

}
