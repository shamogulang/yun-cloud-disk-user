package com.example.cloudiskuser.feign.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfiguration {

    @Bean
    public RequestInterceptor feignInterceptor() {
        return new FeignInterceptor();
    }
}
