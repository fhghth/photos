package com.example.sp20250610.mapper;

import com.example.sp20250610.entity.Users;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UsersMapper {
    List<Users> selectAll();

    Users selectByUsername(@Param("username") String username);

    void insert(Users users);

    void update(Users users);

    Users selectById(@Param("id") Integer id);

    //接口方法在这里定义，在xml中实现
}
