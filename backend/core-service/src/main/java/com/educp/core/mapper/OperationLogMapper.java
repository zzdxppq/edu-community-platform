package com.educp.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.educp.core.entity.OperationLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OperationLogMapper extends BaseMapper<OperationLog> {
}
