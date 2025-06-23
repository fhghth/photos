package com.example.sp20250610.mapper;

import com.example.sp20250610.entity.WorkImage;
import org.apache.ibatis.annotations.*;
import java.math.BigInteger;
import java.util.List;

@Mapper
public interface WorkImageMapper {

    /**
     * 插入作品图片信息
     * 对应XML中的id="insert"语句
     *
     * @param workImage 作品图片实体对象
     * @return 插入操作影响的行数
     */
    int insert(WorkImage workImage);

    /**
     * 根据作品ID查询相关图片
     *
     * @param workId 作品ID
     * @return 作品图片列表
     */
    @Select("SELECT * FROM work_images WHERE work_id = #{workId}")
    List<WorkImage> findByWorkId(BigInteger workId);

    /**
     * 根据图片ID删除图片记录
     *
     * @param id 图片ID
     */
    @Delete("DELETE FROM work_images WHERE id = #{id}")
    void deleteById(BigInteger id);

    List<WorkImage> findImagesByWorkIds(@Param("ids") List<BigInteger> ids);
}