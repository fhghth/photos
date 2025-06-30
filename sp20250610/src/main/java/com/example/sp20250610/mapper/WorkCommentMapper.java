package com.example.sp20250610.mapper;

import com.example.sp20250610.entity.WorkComments;
import com.example.sp20250610.entity.Works;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.math.BigInteger;
import java.util.List;

@Mapper
public interface WorkCommentMapper {

    // 根据作品ID查询评论
    List<WorkComments> findByWorkId(BigInteger workId);

    // 插入新评论
    int insert(WorkComments comment);

    // 增加评论计数
    void incrementCommentCount(BigInteger workId);
}
