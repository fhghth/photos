package com.example.sp20250610.service;

import com.example.sp20250610.entity.Users;
import com.example.sp20250610.entity.Works;
import com.example.sp20250610.exception.CustomerException;
import com.example.sp20250610.mapper.AdminMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;

@Service
public class AdminService {

    @Resource
    AdminMapper adminMapper;

    public String admin(String username) {
        if ("admin".equals(username)) {
            return "登录成功！";
        } else {
            throw new CustomerException("账号错误");
        }
    }
    //用户信息
    public List<Users> selectByRole(String role) {
        return adminMapper.selectByRole(role);
    }

    public PageInfo<Users> selectPage(String role,Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Users> list = adminMapper.selectByRole(role);
        return PageInfo.of(list);
    }
    //作品信息全查找（地图）
    public List<Works> selectAllWorks() {
        return adminMapper.selectAllWorks();

    }
    public PageInfo<Works> photosPending(String role, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Works> list = adminMapper.selectByWorkRole(role);
        return PageInfo.of(list);
    }
    //审核通过
    @Transactional
    public void approveWork(BigInteger id) {
        // 1. 从数据库获取作品
        Works work = adminMapper.selectById(id);
        if (work == null) {
            throw new RuntimeException("未找到作品");
        }

        // 2. 更新审核状态
        work.setRole("true"); // 设置为审核通过
        adminMapper.updateReviewStatus(work);
    }
    //审核退回（未通过）
    @Transactional
    public void rejectWork(BigInteger id) {
        // 1. 从数据库获取作品
        Works work = adminMapper.selectById(id);
        if (work == null) {
            throw new RuntimeException("未找到作品");
        }

        // 2. 更新审核状态
        work.setRole("back"); // 设置为退回
        adminMapper.updateReviewStatus(work);
    }

}
