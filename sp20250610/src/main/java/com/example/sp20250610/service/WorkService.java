package com.example.sp20250610.service;

import com.example.sp20250610.entity.Works;
import com.example.sp20250610.mapper.WorksMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;

@Service
public class WorkService {
    @Autowired
    private WorksMapper worksMapper;

    @Transactional
    public Works save(Works work) {
        worksMapper.insert(work);
        return work;
    }
    //获取通过审核的作品信息
    public List<Works> selectByWorkRole(String role) {
        return worksMapper.selectByWorkRole(role);

    }

    public List<Works> findAllApprovedWorks(String query) {
        return worksMapper.findAllApprovedWorks(query);
    }

    public Works findById(BigInteger id) {
        return worksMapper.findById(id);
    }
}