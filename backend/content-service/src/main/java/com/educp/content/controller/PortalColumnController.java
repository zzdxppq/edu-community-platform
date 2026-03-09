package com.educp.content.controller;

import com.educp.common.result.Result;
import com.educp.content.dto.response.ColumnResponse;
import com.educp.content.service.ColumnService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "公开栏目接口")
@RestController
@RequestMapping("/v1/portal/columns")
@RequiredArgsConstructor
public class PortalColumnController {

    private final ColumnService columnService;

    @Operation(summary = "获取可见栏目列表（无需认证）")
    @GetMapping
    public Result<List<ColumnResponse>> listVisible() {
        return Result.success(columnService.listVisible());
    }
}
