package com.example.sp20250610.controller;

import com.example.sp20250610.common.Result;
import com.example.sp20250610.mapper.WorkStatsMapper;
import com.example.sp20250610.service.WorkStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.Map;

@RestController
@RequestMapping("/work-stats")
public class WorkStatsController {

    @Autowired
    private WorkStatsService workStatsService;

    @Autowired
    private WorkStatsMapper workStatsMapper;

    @PostMapping("/like")
    public Result like(@RequestBody Map<String, Object> params) {
        Object workIdObj = params.get("workId");
        Object userIdObj = params.get("userId");
        if (workIdObj == null || userIdObj == null) {
            return Result.error("缺少参数workId或userId","缺少");
        }
        BigInteger workId = new BigInteger(workIdObj.toString());
        BigInteger userId = new BigInteger(userIdObj.toString());
        int likeCount = workStatsService.likeWork(workId, userId);
        return Result.success(Map.of("likeCount", likeCount));
    }

    @PostMapping("/view")
    public Result view(@RequestBody Map<String, Object> params) {
        Object workIdObj = params.get("workId");
        if (workIdObj == null) {
            return Result.error("缺少参数workId","失败");
        }
        BigInteger workId = new BigInteger(workIdObj.toString());
        int viewCount = workStatsService.incrementViewCount(workId);
        return Result.success(Map.of("viewCount", viewCount));
    }

    @GetMapping("/check")
    public Result checkLikeStatus(@RequestParam BigInteger workId,
                                  @RequestParam BigInteger userId) {
        boolean liked = workStatsMapper.hasUserLiked(workId, userId);
        return Result.success(Map.of("liked", liked));
    }
}