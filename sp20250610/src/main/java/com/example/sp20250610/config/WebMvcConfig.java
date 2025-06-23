package com.example.sp20250610.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${file.upload.path}")
    private String uploadPath; // 获取配置文件中的上传路径

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 静态资源映射：将本地路径映射为URL可访问路径
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadPath + "/")
                .setCachePeriod(3600); // 可选：设置缓存时间
    }
}