package com.educp.core.controller;

import com.educp.common.result.Result;
import com.educp.core.dto.response.SchoolOptionResponse;
import com.educp.core.service.SchoolService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "学校管理")
@RestController
@RequestMapping("/v1/schools")
@RequiredArgsConstructor
public class SchoolController {

    private final SchoolService schoolService;

    @Operation(summary = "获取学校选项列表")
    @GetMapping("/options")
    public Result<List<SchoolOptionResponse>> listOptions() {
        List<SchoolOptionResponse> options = schoolService.listOptions();
        return Result.success(options);
    }
}
