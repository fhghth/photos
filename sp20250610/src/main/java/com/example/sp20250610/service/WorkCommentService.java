package com.example.sp20250610.service;


import com.example.sp20250610.entity.WorkComments;
import com.example.sp20250610.entity.Works;
import com.example.sp20250610.mapper.WorkCommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;

@Service
public class WorkCommentService {

    @Autowired
    private WorkCommentMapper workCommentMapper;

    @Autowired
    private WorkService workService; // 添加workService依赖


    // 根据作品ID获取评论列表
    public List<WorkComments> getCommentsByWorkId(BigInteger workId) {
        return workCommentMapper.findByWorkId(workId);
    }

    // 添加评论
    public WorkComments addComment(WorkComments comment) {
        comment.setCommentTime(new Timestamp(System.currentTimeMillis()));
        comment.setIsDeleted(false);
        workCommentMapper.insert(comment);

        // 更新作品的评论计数
        workCommentMapper.incrementCommentCount(comment.getWorkId());

        return comment;
    }
}