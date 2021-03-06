package com.oldlie.learn.spring.security.dynamicurl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.MultipartConfigElement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@EnableSwagger2
@SpringBootApplication
@EnableTransactionManagement
@Slf4j
public class DynamicUrlApplication {

	public static void main(String[] args) {
		SpringApplication.run(DynamicUrlApplication.class, args);
		log.info("start...");

	}

	/**
	 * https://blog.csdn.net/wanping321/article/details/79532918
	 *
	 * SpringBoot jpa 使用懒加载时，报异常：session失效
	 * 在方法上加@Transactional 注解，失败
	 * 在application.yml 文件加上jpa.properties.open-in-view: true 失败
	 * 在ResourceServerApplication.java 启动文件中加上下面这个Bean
	 *
	 * 总结：
	 * 要解决no session 问题需要：
	 * 配置文件中加jpa.properties.open-in-view: true同时在启动文件中加@Bean
	 *
	 * @return OpenEntityManagerInViewFilter
	 */
	@Bean
	public OpenEntityManagerInViewFilter openEntityManagerInViewFilter() {
		return new OpenEntityManagerInViewFilter();
	}

	/**
	 * 密码编码器
	 *
	 * @return BCryptPasswordEncoder
	 */
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * 跨域设置
	 *
	 * @return FilterRegistrationBean
	 */
	@Bean
	@SuppressWarnings("unchecked")
	public FilterRegistrationBean simpleCorsFilter() {
		List<String> origins = new ArrayList<>();
		origins.add("http://localhost:4200");
		origins.add("http://localhost:4201");
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.setAllowedOrigins(origins);
		config.setAllowedMethods(Collections.singletonList("*"));
		config.setAllowedHeaders(Collections.singletonList("*"));
		source.registerCorsConfiguration("/**", config);
		FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
		bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return bean;
	}

	@Value("${fileUploadTemp}")
	private String location;

	@Bean
	MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		System.out.println(location);
		factory.setLocation(location);
		return factory.createMultipartConfig();
	}
}
