package com.home.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.home.interceptor.AdminAuthenticationInterceptor;
import com.home.interceptor.ShareInterceptor;

@Configuration
public class AuthenticationInterceptorConfig implements WebMvcConfigurer {
    @Autowired
    private AdminAuthenticationInterceptor adminAuthenticationInterceptor;
    @Autowired
    private ShareInterceptor share;
    
   
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//	``registry.addInterceptor(adminAuthenticationInterceptor).addPathPatterns("/admin/**");

//	registry.addInterceptor(siteAuthenticationInterceptor).addPathPatterns("/site/**");
    	
//    	registry.addInterceptor(share).addPathPatterns("/**");
    	
    	
    }


}