package com.example.sp20250610.controller;

import com.example.sp20250610.common.Result;
import com.example.sp20250610.entity.Users;
import com.example.sp20250610.entity.Works;
import com.example.sp20250610.service.AdminService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;


@RestController
@RequestMapping("/admin")
public class AdminController {


    @Resource
    AdminService adminService;

     //用户信息
    @GetMapping("/select")
    public Result select(@RequestParam String role) {
        List<Users> adminList = adminService.selectByRole(role);
        return Result.success(adminList);
    }

    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             @RequestParam(required = false) String role) {

        //role,pageNum,pageSize调用顺序需要跟AdminService保持一致
        PageInfo<Users> pageInfo =adminService.selectPage(role,pageNum,pageSize);
        return Result.success(pageInfo);
    }

    //内容信息（作品、评论等）
    @GetMapping("/photos")
    public Result photos() {
        List<Works>worksList = adminService.selectAllWorks();
        return Result.success(worksList);
    }

    @GetMapping("/photosPending")
    public Result photosPending(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             @RequestParam(required = false) String role) {

        PageInfo<Works> pageInfo =adminService.photosPending(role,pageNum,pageSize);
        return Result.success(pageInfo);
    }


    // 审核通过接口
    @PutMapping("/approve/{id}")
    public Result approveWork(@PathVariable BigInteger id) {
        adminService.approveWork(id);
        return Result.success("作品已审核通过");
    }

    // 审核退回接口
    @PutMapping("/reject/{id}")
    public Result rejectWork(@PathVariable BigInteger id) {
        adminService.rejectWork(id);
        return Result.success("作品已退回");
    }

}
