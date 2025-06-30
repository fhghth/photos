package com.example.sp20250610.service;

import com.example.sp20250610.entity.Users;
import com.example.sp20250610.exception.CustomerException;
import com.example.sp20250610.mapper.UsersMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class UsersService {
    
    @Resource
    UsersMapper usersMapper;
    
    public Users login(Users users) {
        Users dbUsers =  usersMapper.selectByUsername (users.getUsername());
        if (dbUsers == null) {
            throw new CustomerException("账号不存在");
        }
        if(!dbUsers.getPassword().equals(users.getPassword())) {
            throw new CustomerException("账号或密码错误");
        }
        return dbUsers;
    }

    public void register(Users users) {
        usersMapper.insert(users);
    }

    public void edit(Users users) {
        // 如果有头像文件但处理失败，不更新头像
        if (users.getAvatar() == null) {
            // 保持原有头像不变
            Users existingUser = usersMapper.selectById(users.getId());
            users.setAvatar(existingUser.getAvatar());
        }
        usersMapper.update(users);
    }

    // 添加根据ID查询的方法
    public Users selectById(Integer id) {
        return usersMapper.selectById(id);
    }
}
