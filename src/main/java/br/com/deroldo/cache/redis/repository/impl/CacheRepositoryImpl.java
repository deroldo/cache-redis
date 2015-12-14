package br.com.deroldo.cache.redis.repository.impl;

import java.util.Set;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import br.com.deroldo.cache.redis.repository.CacheRepository;

public class CacheRepositoryImpl implements CacheRepository{
	private static final long serialVersionUID = 4240818990409079919L;

	private static final String SUCCESS_STATUS = "OK";
	
	private Logger logger = LoggerFactory.getLogger(CacheRepositoryImpl.class);
	
	@Inject
	private Jedis jedis;
	
	@Override
	public boolean set(String key, String json) {
		boolean recorded = false;
		try {
			recorded = SUCCESS_STATUS.equals(this.jedis.set(key, json));
		} catch (Exception e) {
			this.logger.error("Problems in recording cache - key {} and json {}", key, json, e);
		}
		return recorded;
	}
	
	@Override
	public boolean set(String key, String json, int ttl) {
		boolean recorded = false;
		try {
			recorded = SUCCESS_STATUS.equals(this.jedis.setex(key, ttl, json));
		} catch (Exception e) {
			this.logger.error("Problems in recording cache - key {}, json {} and ttl {}", key, json, ttl, e);
		}
		return recorded;
	}

	@Override
	public String get(String key) {
		String value = null;
		try {
			value = this.jedis.get(key);
		} catch (Exception e) {
			this.logger.error("Problems in reading cache - key {}", e.getMessage(), e);
		}
		return value;
	}

	@Override
	public boolean expire(String key) {
		boolean expired = false;
		try {
			expired = this.jedis.expire(key, 0).intValue() == 1;
		} catch (Exception e) {
			this.logger.error("Problems in expiring cache - key {}", key, e);
		}
		return expired;
	}

	@Override
	public boolean expireAll() {
		boolean allExpired = false;
		try {
			Set<String> keys = this.jedis.keys("*");
			int countAll = keys.size();
			int count = 0;
			for (String key : keys) {
				boolean expire = expire(key);
				if (expire){
					count++;
				}
			}
			allExpired = countAll == count;
		} catch (Exception e) {
			this.logger.error("Problems in get keys - error {}", e.getMessage(), e);
		}
		return allExpired;
	}

}
