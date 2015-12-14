package br.com.deroldo.cache.redis.jedis;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class JedisProducer implements Serializable{
	private static final long serialVersionUID = 4343981501680979787L;
	
	@Inject
    private JedisPool jedisPool;
	
	@Produces @RequestScoped
    public Jedis getJedis(){
        return this.jedisPool.getResource();
    }
 
    public void returnResource(@Disposes Jedis jedis){
    	this.jedisPool.returnResourceObject(jedis);
    }

}
