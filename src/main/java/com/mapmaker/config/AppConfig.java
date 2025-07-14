package com.mapmaker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@Configuration
public class AppConfig {

    @Bean
    public MultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(5242880); // 5MB
        multipartResolver.setMaxUploadSizePerFile(2097152); // 2MB per file (任意)
        multipartResolver.setDefaultEncoding("UTF-8");
        return multipartResolver;
    }
}
