package com.educp.content.controller;

import com.educp.common.result.Result;
import com.educp.common.security.RequireRole;
import com.educp.content.dto.request.CreateSubColumnRequest;
import com.educp.content.dto.request.SortSubColumnRequest;
import com.educp.content.dto.request.UpdateSubColumnRequest;
import com.educp.content.dto.response.SubColumnResponse;
import com.educp.content.service.SubColumnService;
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

@Tag(name = "二级菜单管理")
@RestController
@RequestMapping("/v1/columns/{columnId}/sub-columns")
@RequiredArgsConstructor
@RequireRole(1)
public class SubColumnController {

    private final SubColumnService subColumnService;

    @Operation(summary = "获取二级菜单列表")
    @GetMapping
    public Result<List<SubColumnResponse>> list(@PathVariable Long columnId) {
        return Result.success(subColumnService.listByColumnId(columnId));
    }

    @Operation(summary = "创建二级菜单")
    @PostMapping
    public Result<Long> create(@PathVariable Long columnId,
                               @Valid @RequestBody CreateSubColumnRequest request) {
        Long id = subColumnService.create(columnId, request);
        return Result.success(id);
    }

    @Operation(summary = "更新二级菜单")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long columnId,
                               @PathVariable Long id,
                               @Valid @RequestBody UpdateSubColumnRequest request) {
        subColumnService.update(columnId, id, request);
        return Result.success();
    }

    @Operation(summary = "删除二级菜单")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long columnId, @PathVariable Long id) {
        subColumnService.delete(columnId, id);
        return Result.success();
    }

    @Operation(summary = "批量更新排序")
    @PutMapping("/sort")
    public Result<Void> updateSort(@PathVariable Long columnId,
                                   @Valid @RequestBody SortSubColumnRequest request) {
        subColumnService.updateSort(columnId, request);
        return Result.success();
    }
}
