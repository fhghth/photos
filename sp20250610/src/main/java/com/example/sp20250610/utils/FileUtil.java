package com.example.sp20250610.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
public class FileUtil {

    @Value("${avatar.upload.path}")
    private String avatarUploadPath;

    @Value("${file.upload.path}")
    private String fileUploadPath;

    public String storeAvatar(MultipartFile file, String username) throws IOException {
        // 确保存储目录存在
        Path uploadPath = Paths.get(avatarUploadPath);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 生成唯一文件名: 用户名 + UUID + 原始扩展名
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        String fileName = username + "_" + UUID.randomUUID() + extension;
        Path filePath = uploadPath.resolve(fileName);

        // 保存文件
        file.transferTo(filePath.toFile());

        // 返回相对路径（不带绝对路径）
        return "/avatars/" + fileName;
    }

    public String getAvatarUploadPath() {
        return avatarUploadPath;
    }

    public String getFileUploadPath() {
        return fileUploadPath;
    }
}