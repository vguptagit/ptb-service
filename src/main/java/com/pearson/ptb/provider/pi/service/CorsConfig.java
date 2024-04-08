package com.pearson.ptb.provider.pi.service;

import java.util.Arrays;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

	private static final String LOCAL_URL = "http://testbuilder.dev.pearsoncmg.com:3000";
	private static final String DEV_URL = "https://testbuilder.dev.pearsoncmg.com";
	private static final String QA_URL = "https://testbuilder.qa.pearsoncmg.com";
	private static final String STG_URL = "https://testbuilder.STG.pearsoncmg.com";

	@Bean
	public FilterRegistrationBean<CorsFilter> crosFilter() {

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowCredentials(true);
//		configuration.addAllowedOriginPattern("http://testbuilder.dev.pearsoncmg.com:3000/**");
		configuration.setAllowedOriginPatterns(Arrays.asList(LOCAL_URL, DEV_URL, QA_URL, STG_URL));
		configuration.addAllowedHeader("x-authorization");
		configuration.addAllowedHeader("Content-Type");
		configuration.addAllowedHeader("x-requested-with");
		configuration.addAllowedHeader("Accept");
		configuration.addAllowedMethod("GET");
		configuration.addAllowedMethod("POST");
		configuration.addAllowedMethod("DELETE");
		configuration.addAllowedMethod("PUT");
		configuration.addAllowedMethod("OPTIONS");
		configuration.setMaxAge(3600L);
		source.registerCorsConfiguration("/**", configuration);

		FilterRegistrationBean<CorsFilter> filterRegistrationBean = new FilterRegistrationBean<>(
				new CorsFilter(source));
		filterRegistrationBean.setOrder(-100);
		return filterRegistrationBean;

	}
}
