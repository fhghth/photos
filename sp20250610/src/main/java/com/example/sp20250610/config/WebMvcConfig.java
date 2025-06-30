package com.example.sp20250610.config;

import com.example.sp20250610.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final FileUtil fileUtil;
    
    @Value("${file.upload.path}")
    private String uploadPath;

    @Autowired
    public WebMvcConfig(FileUtil fileUtil) {
        this.fileUtil = fileUtil;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 映射头像访问路径
        registry.addResourceHandler("/avatars/**")
                .addResourceLocations("file:" + fileUtil.getAvatarUploadPath() + File.separator);
        // 静态资源映射：将本地路径映射为URL可访问路径
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadPath + "/")
                .setCachePeriod(3600); // 可选：设置缓存时间
    }
}