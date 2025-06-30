package com.example.sp20250610.mapper;

import java.math.BigInteger;
import java.sql.Timestamp;

public interface WorkStatsMapper {
    // 检查用户是否已点赞
    boolean hasUserLiked(BigInteger workId, BigInteger userId);

    // 添加点赞记录
    void addLike(BigInteger workId, BigInteger userId, Timestamp likeTime);

    // 删除点赞记录
    void deleteLike(BigInteger workId, BigInteger userId);
}