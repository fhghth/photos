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
}
