package com.educp.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.educp.core.entity.AdminUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AdminUserMapper extends BaseMapper<AdminUser> {

    @Select("SELECT COUNT(*) > 0 FROM admin_users WHERE username = #{username} AND deleted_at IS NULL")
    boolean existsByUsername(@Param("username") String username);

    @Select("SELECT COUNT(*) > 0 FROM schools WHERE id = #{schoolId} AND deleted_at IS NULL")
    boolean existsSchoolById(@Param("schoolId") Long schoolId);

    @Select("SELECT * FROM admin_users WHERE username = #{username} AND deleted_at IS NULL")
    AdminUser findByUsername(@Param("username") String username);

    @Select("SELECT * FROM admin_users WHERE id = #{id} AND deleted_at IS NULL")
    AdminUser findByIdWithPassword(@Param("id") Long id);
}
