package br.com.deroldo.cache.redis.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.enterprise.util.Nonbinding;
import javax.interceptor.InterceptorBinding;

@InterceptorBinding
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Cache {
	
	public static final int DEFAULT_METHOD_TTL = -1;
	public static final int DEFAULT_TTL = 300;

	@Nonbinding
	int ttlInSeconds() default DEFAULT_METHOD_TTL;
	@Nonbinding
	boolean eternal() default false;
}
