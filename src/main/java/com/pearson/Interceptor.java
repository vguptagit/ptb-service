package com.pearson;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.pearson.ptb.provider.pi.service.AuthFilter;

@Configuration
public class Interceptor implements WebMvcConfigurer{
	
	
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new AuthFilter());
		
	}

	
}
