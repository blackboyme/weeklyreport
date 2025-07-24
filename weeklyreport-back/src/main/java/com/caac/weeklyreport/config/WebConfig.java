package com.caac.weeklyreport.config;

import com.caac.weeklyreport.interceptor.TokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private TokenInterceptor tokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/**/health/**")
                .excludePathPatterns("/**/users/**")
                .excludePathPatterns("/**/swagger-resources/**",
                        "/**/webjars/**",
                        "/**/favicon.ico",
                        "/**/v3/**",
                        "/**/doc.html/**");
    }
}
