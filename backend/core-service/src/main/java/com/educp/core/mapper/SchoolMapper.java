package com.educp.core.mapper;

import com.educp.core.dto.response.SchoolOptionResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SchoolMapper {

    @Select("SELECT id, name FROM schools WHERE status = 1 AND deleted_at IS NULL ORDER BY sort_order")
    List<SchoolOptionResponse> listOptions();

    @Select("SELECT name FROM schools WHERE id = #{id} AND status = 1 AND deleted_at IS NULL")
    String findNameById(@Param("id") Long id);
}
