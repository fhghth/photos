package com.example.sp20250610.controller;

import com.example.sp20250610.common.Result;
import com.example.sp20250610.entity.Users;
import com.example.sp20250610.service.UsersService;
import com.example.sp20250610.utils.FileUtil;
import jakarta.annotation.Resource;
import org.apache.ibatis.annotations.Update;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.http.ResponseEntity;

@RestController
public class UsersController {

    @Resource
    UsersService usersService;

    @Resource
    private FileUtil fileUtil;

    @PostMapping("/login")
    public Result login(@RequestBody Users users){
        Users dbUsers = usersService.login(users);
        return Result.success(dbUsers);
    }

    @PostMapping("/register")
    public Result register(@RequestBody Users users){ //@RequestBody 接收前端传来的json参数
        usersService.register(users);                  //alt+enter 在UsersService中创建register方法
        return Result.success(users);
    }

    @PostMapping(value = "/edit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result edit(
            @RequestParam("id") Integer id,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "avatarFile", required = false) MultipartFile avatarFile) {

        // 创建用户对象并设置属性
        Users user = new Users();
        user.setId(id);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setPhone(phone);

        // 如果有新头像文件，则处理
        if (avatarFile != null && !avatarFile.isEmpty()) {
            try {
                String avatarPath = fileUtil.storeAvatar(avatarFile, username);
                user.setAvatar(avatarPath); // 只存储相对路径
            } catch (IOException e) {
                e.printStackTrace();
                return Result.error("200","上传失败");
            }
        }

        usersService.edit(user);
        return Result.success(user);
    }

    // 添加头像访问接口
    @GetMapping("/avatar/{filename:.+}")
    public ResponseEntity<org.springframework.core.io.Resource> serveAvatar(@PathVariable String filename) {
        try {
            Path file = Paths.get(fileUtil.getAvatarUploadPath()).resolve(filename);
            org.springframework.core.io.Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION,
                                "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
