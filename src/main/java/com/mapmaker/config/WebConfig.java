package com.mapmaker.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    // 🔹 /uploads/** のリクエストを webapp/uploads/ にマッピング
    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        // 保存先ディレクトリをマッピング
//        registry.addResourceHandler("/static//uploads/**")
//                .addResourceLocations("file:/uploads/"); // 画像の保存場所
    }

    // 🔹 静的ファイルへのフォールバックを有効にする
    @Override
    public void configureDefaultServletHandling(@NonNull DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
}
