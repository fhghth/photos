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
     * 根据ID查询作品信息
     * 这个查询方法对应的XML未提供，这里添加方法声明
     *
     * @param id 作品ID
     * @return 作品对象
     */
    @Select("SELECT * FROM works WHERE id = #{id}")
    Works findById(BigInteger id);

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

}