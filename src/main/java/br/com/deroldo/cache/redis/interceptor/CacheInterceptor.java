package br.com.deroldo.cache.redis.interceptor;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.apache.commons.lang3.StringUtils;

import br.com.deroldo.cache.redis.annotation.Cache;
import br.com.deroldo.cache.redis.domain.Envelope;
import br.com.deroldo.cache.redis.properties.ApplicationProperties;
import br.com.deroldo.cache.redis.properties.CacheProperties;
import br.com.deroldo.cache.redis.repository.CacheRepository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Interceptor @Cache
public class CacheInterceptor implements Serializable {
	private static final long serialVersionUID = -583626753049504126L;

	@Inject	
	private ApplicationProperties applicationProperties;
	
	@Inject
	private CacheRepository cacheRepository;
	
	private Gson gson;
	
	public CacheInterceptor(){
		this.gson = new GsonBuilder().registerTypeAdapter(Class.class, new ClassTypeAdapter()).create();
	}

	@AroundInvoke
	public Object intercept(final InvocationContext context) throws Exception {
		Object object = null;
		
		CacheProperties properties = this.applicationProperties.getCacheProperties();
		
		if (properties.isEnabled()){
			Integer applicationTtl = properties.getTtl();
			
			final String key = getKey(context.getMethod(), context.getParameters());
			final String json = this.cacheRepository.get(key);
			
			if (StringUtils.isNotBlank(json)){
				final Envelope envelope = this.gson.fromJson(json, Envelope.class);
				final Class<?> type = envelope.getTypeOfJson();
	
				object = this.gson.fromJson(envelope.getJson(), type);
			}
			
			if (object == null){
				object = context.proceed();
				
				final Envelope envelope = new Envelope(this.gson.toJson(object), object.getClass());
				
				if (isEternal(context.getMethod())){
					this.cacheRepository.set(key, this.gson.toJson(envelope));
					
				} else {
					int ttl = getTtl(context.getMethod());
					
					if (ttl == Cache.DEFAULT_METHOD_TTL && applicationTtl != null){
						ttl = applicationTtl;
					} else {
						ttl = Cache.DEFAULT_TTL;
					}
					
					this.cacheRepository.set(key, this.gson.toJson(envelope), ttl);
				}
			}
			
		} else {
			object = context.proceed();
		}
		
		return object;
	}
	
	private boolean isEternal(Method method){
		return method.getDeclaredAnnotation(Cache.class).eternal();
	}

	private int getTtl(Method method) {
		return method.getDeclaredAnnotation(Cache.class).ttlInSeconds();
	}
	
	private String getKey(final Method method, final Object[] parameters) {
		return method.getDeclaringClass().getSimpleName() + method.getName() + Arrays.toString(parameters);
	}

}
