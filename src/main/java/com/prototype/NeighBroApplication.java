package com.prototype;

import com.prototype.security.CorsFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class NeighBroApplication {

	public static void main(String[] args) {
		SpringApplication.run(NeighBroApplication.class, args);
	}

	/*
	// Doesn't work because of Spring Security
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurerAdapter() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("*//**").allowedOrigins("*").allowedMethods("GET", "PUT", "POST", "DELETE");
			}
		};
	}*/

	@Bean
	public FilterRegistrationBean corsFilter() {
		/*UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOrigin("*");
		config.addAllowedHeader("*");
		config.addAllowedMethod("OPTIONS");
		config.addAllowedMethod("HEAD");
		config.addAllowedMethod("GET");
		config.addAllowedMethod("PUT");
		config.addAllowedMethod("POST");
		config.addAllowedMethod("DELETE");
		config.addAllowedMethod("PATCH");
		source.registerCorsConfiguration("*//**", config);*/
		//FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
		FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter());
		bean.setOrder(0);
		return bean;
	}
}
