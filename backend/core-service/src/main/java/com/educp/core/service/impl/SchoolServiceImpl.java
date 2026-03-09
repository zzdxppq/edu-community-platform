package com.educp.core.service.impl;

import com.educp.core.dto.response.SchoolOptionResponse;
import com.educp.core.mapper.SchoolMapper;
import com.educp.core.service.SchoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SchoolServiceImpl implements SchoolService {

    private final SchoolMapper schoolMapper;

    @Override
    public List<SchoolOptionResponse> listOptions() {
        return schoolMapper.listOptions();
    }
}
