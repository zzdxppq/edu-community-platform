package com.educp.core.service.impl;

import com.educp.core.entity.OperationLog;
import com.educp.core.mapper.OperationLogMapper;
import com.educp.core.service.OperationLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OperationLogServiceImpl implements OperationLogService {

    private final OperationLogMapper operationLogMapper;

    @Override
    public void log(OperationLog operationLog) {
        operationLogMapper.insert(operationLog);
        log.info("操作日志已记录: module={}, action={}, userId={}",
                operationLog.getModule(), operationLog.getAction(), operationLog.getUserId());
    }
}
