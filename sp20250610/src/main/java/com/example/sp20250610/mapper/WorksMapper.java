package com.example.sp20250610.mapper;

import com.example.sp20250610.entity.Works;
import org.apache.ibatis.annotations.*;
import java.math.BigInteger;
import java.util.List;

@Mapper
public interface WorksMapper {

    /**
     * 插入作品信息
     * 对应XML中的id="insert"语句
     *
     * @param works 作品实体对象
     * @return 插入操作影响的行数
     */
    int insert(Works works);

    /**
     * 更新作品封面路径
     *
     * @param id 作品ID
     * @param coverImagePath 封面图片路径
     */
    @Update("UPDATE works SET cover = #{coverImagePath} WHERE id = #{id}")
    void updateCoverImage(@Param("id") BigInteger id,
                          @Param("coverImagePath") String coverImagePath);

    List<Works> selectByWorkRole(@Param("role")String role);


    List<Works> findAllApprovedWorks(@Param("query") String query);

    //查询浏览量
    int getViewCount(BigInteger workId); // 添加新方法

    // 增加浏览量
    int incrementViewCount(BigInteger workId);

    // 增加点赞数
    int incrementLikeCount(BigInteger workId);

    // 减少点赞数
    int decrementLikeCount(BigInteger workId);

    // 获取当前点赞数
    int getLikeCount(BigInteger workId);

    // 获取用户所有作品
    List<Works> selectByUsername(@Param("username") String username);


    //
    Works selectById(BigInteger id);

    // 更新审核状态，此处使用@Update注解，简化了sql语句，不需要在xml中再次配置
    @Update("UPDATE works SET role = #{role} WHERE id = #{id}")
    int updateReviewStatus(Works work);


    void deleteById(BigInteger id);
}