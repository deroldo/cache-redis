package br.com.deroldo.cache.redis.properties.impl;

import br.com.deroldo.cache.redis.properties.CacheProperties;

public class CachePropertiesImpl implements CacheProperties{
	
	private boolean enabled;
	private String host;
	private Integer port;
	private Integer ttl;
	
	public CachePropertiesImpl(boolean enabled, String host, Integer port, Integer ttl) {
		this.enabled = enabled;
		this.host = host;
		this.port = port;
		this.ttl = ttl;
	}

	@Override
	public boolean isEnabled() {
		return this.enabled;
	}

	@Override
	public Integer getTtl() {
		return this.ttl;
	}

	@Override
	public String getHost() {
		return this.host;
	}

	@Override
	public Integer getPort() {
		return this.port;
	}

}
