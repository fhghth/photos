package com.example.sp20250610.mapper;

import com.example.sp20250610.entity.Users;
import com.example.sp20250610.entity.Works;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.math.BigInteger;
import java.util.List;

public interface AdminMapper {
    List<Users> selectByRole(@Param("role")String role);

    List<Works>selectByWorkRole(@Param("role")String role);

    List<Works> selectAllWorks();


    Works selectById(BigInteger id);

    // 更新审核状态，此处使用@Update注解，简化了sql语句，不需要在xml中再次配置
    @Update("UPDATE works SET role = #{role} WHERE id = #{id}")
    int updateReviewStatus(Works work);
}
