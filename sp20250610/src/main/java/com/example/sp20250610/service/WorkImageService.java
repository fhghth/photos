package com.example.sp20250610.service;

import com.example.sp20250610.entity.WorkImage;
import com.example.sp20250610.entity.Works;
import com.example.sp20250610.mapper.WorkImageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class WorkImageService {
    @Autowired
    private WorkImageMapper workImageMapper;

    @Transactional
    public WorkImage save(WorkImage workImage) {
        workImageMapper.insert(workImage);
        return workImage;
    }

    public List<WorkImage> findByWorkId(BigInteger workId) {
        return workImageMapper.findByWorkId(workId);
    }

    public Map<BigInteger, List<WorkImage>> getImagesForWorks(List<Works> works) {
        if (works == null || works.isEmpty()) {
            return new HashMap<>();
        }
        List<BigInteger> workIds = works.stream()
                .map(Works::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        List<WorkImage> images = workImageMapper.findImagesByWorkIds(workIds);
        // 过滤掉 work_id 为 null 的图片
        return images.stream()
                .filter(img -> img.getWorkId() != null)
                .collect(Collectors.groupingBy(WorkImage::getWorkId));
    }
} 