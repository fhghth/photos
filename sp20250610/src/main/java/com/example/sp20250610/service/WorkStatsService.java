package com.example.sp20250610.service;

import com.example.sp20250610.mapper.WorkStatsMapper;
import com.example.sp20250610.mapper.WorksMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
public class WorkStatsService {
    @Autowired
    private WorksMapper worksMapper;

    @Autowired
    private WorkStatsMapper workStatsMapper;

    /**
     * 增加作品浏览量
     */
    public int incrementViewCount(BigInteger workId) {
        // 先执行更新
        worksMapper.incrementViewCount(workId);

        // 然后查询更新后的实际值
        return worksMapper.getViewCount(workId);
    }

    /**
     * 点赞作品
     */
    @Transactional
    public int likeWork(BigInteger workId, BigInteger userId) {
        // 检查用户是否已点赞
        if (workStatsMapper.hasUserLiked(workId, userId)) {
            // 已点赞则取消点赞
            workStatsMapper.deleteLike(workId, userId);
            worksMapper.decrementLikeCount(workId);
            return worksMapper.getLikeCount(workId);
        } else {
            // 未点赞则添加点赞记录
            workStatsMapper.addLike(workId, userId, Timestamp.valueOf(LocalDateTime.now()));
            worksMapper.incrementLikeCount(workId);
            return worksMapper.getLikeCount(workId);
        }
    }
}