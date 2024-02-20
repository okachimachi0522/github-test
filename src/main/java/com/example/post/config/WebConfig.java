package com.example.post.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.post.filter.LogFilter;
import com.example.post.filter.LoginCheckFilter;
import com.example.post.interceptor.LogInterceptor;
import com.example.post.interceptor.LoginCheckInterceptor;

import jakarta.servlet.Filter;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	
//	@Bean
	public FilterRegistrationBean<Filter> logFilter() {
		FilterRegistrationBean<Filter> filterRegistrationBean = 
				new FilterRegistrationBean<>();
		// 사용할 필터를 지정한다.
		filterRegistrationBean.setFilter(new LogFilter());
		// 필터의 순서를 지정한다.
		filterRegistrationBean.setOrder(1);
		// 필터를 적용할 URL 패턴을 지정한다.
		filterRegistrationBean.addUrlPatterns("/*");
		
		return filterRegistrationBean;
	}
	
//	@Bean
	public FilterRegistrationBean<Filter> loginCheckFilter() {
		FilterRegistrationBean<Filter> filterRegistrationBean = 
				new FilterRegistrationBean<>();
		// 사용할 필터를 지정한다.
		filterRegistrationBean.setFilter(new LoginCheckFilter());
		// 필터의 순서를 지정한다.
		filterRegistrationBean.setOrder(2);
		// 필터를 적용할 URL 패턴을 지정한다.
		filterRegistrationBean.addUrlPatterns("/*");
		
		return filterRegistrationBean;
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LogInterceptor())
		.order(1)
		.addPathPatterns("/**");
		
		registry.addInterceptor(new LoginCheckInterceptor())
		.order(2)
		.addPathPatterns("/**")
		.excludePathPatterns("/", 
							"/users/register",
							"/users/login",
							"/users/logout",
							"/posts",
							"/error");
	}
	
}











