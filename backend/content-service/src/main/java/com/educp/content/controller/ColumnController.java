package com.educp.content.controller;

import com.educp.common.result.Result;
import com.educp.common.security.RequireRole;
import com.educp.content.dto.request.CreateColumnRequest;
import com.educp.content.dto.request.SortColumnRequest;
import com.educp.content.dto.request.UpdateColumnRequest;
import com.educp.content.dto.response.ColumnResponse;
import com.educp.content.service.ColumnService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "栏目管理")
@RestController
@RequestMapping("/v1/columns")
@RequiredArgsConstructor
@RequireRole(1)
public class ColumnController {

    private final ColumnService columnService;

    @Operation(summary = "获取全部栏目列表")
    @GetMapping
    public Result<List<ColumnResponse>> listAll() {
        return Result.success(columnService.listAll());
    }

    @Operation(summary = "创建栏目")
    @PostMapping
    public Result<Long> create(@Valid @RequestBody CreateColumnRequest request) {
        Long id = columnService.create(request);
        return Result.success(id);
    }

    @Operation(summary = "更新栏目")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody UpdateColumnRequest request) {
        columnService.update(id, request);
        return Result.success();
    }

    @Operation(summary = "删除栏目")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        columnService.delete(id);
        return Result.success();
    }

    @Operation(summary = "批量更新排序")
    @PutMapping("/sort")
    public Result<Void> updateSort(@Valid @RequestBody SortColumnRequest request) {
        columnService.updateSort(request);
        return Result.success();
    }
}
