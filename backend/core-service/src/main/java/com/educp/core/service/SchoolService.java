package com.educp.core.service;

import com.educp.core.dto.response.SchoolOptionResponse;

import java.util.List;

public interface SchoolService {

    List<SchoolOptionResponse> listOptions();
}
