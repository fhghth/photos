package com.example.sp20250610.controller;

import com.example.sp20250610.common.Result;
import com.example.sp20250610.entity.Users;
import com.example.sp20250610.service.UsersService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
public class UsersController {

    @Resource
    UsersService usersService;


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




}
