package com.example.sp20250610.service;

import com.example.sp20250610.entity.Users;
import com.example.sp20250610.entity.Works;
import com.example.sp20250610.mapper.UsersMapper;
import com.example.sp20250610.mapper.WorksMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;

@Service
public class WorkService {
    @Autowired
    private WorksMapper worksMapper;
    @Autowired
    private UsersMapper usersMapper;

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
        return worksMapper.selectById(id);
    }

    public List<Works> selectByUsername(String username) {return worksMapper.selectByUsername(username);}


    //分页
    public PageInfo<Works> selectPage(String username, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Works> list = worksMapper.selectByUsername(username);
        return PageInfo.of(list);
    }

    //重新提交作品
    @Transactional
    public void reuploadWork(BigInteger id) {
        // 1. 从数据库获取作品
        Works work = worksMapper.selectById(id);
        if (work == null) {
            throw new RuntimeException("未找到作品");
        }

        // 2. 更新作品状态
        work.setRole("false"); //
        worksMapper.updateReviewStatus(work);
    }

    @Transactional
    public void deleteWork(BigInteger id) {
        worksMapper.deleteById(id);
    }
}