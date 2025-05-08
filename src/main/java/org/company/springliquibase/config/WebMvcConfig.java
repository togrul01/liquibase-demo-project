package org.company.springliquibase.config;

import lombok.RequiredArgsConstructor;
import org.company.springliquibase.interceptor.CardResponseInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final CardResponseInterceptor cardResponseInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(cardResponseInterceptor)
                .addPathPatterns("/v1/cards/**");

    }
} 