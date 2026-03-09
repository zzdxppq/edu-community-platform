package com.educp.content.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(description = "栏目响应")
public class ColumnResponse {

    @Schema(description = "栏目ID")
    private Long id;

    @Schema(description = "栏目名称")
    private String name;

    @Schema(description = "栏目代码")
    private String code;

    @Schema(description = "栏目图标")
    private String icon;

    @Schema(description = "前端路由路径")
    private String routePath;

    @Schema(description = "是否系统预置")
    private Boolean isSystem;

    @Schema(description = "是否可见")
    private Boolean isVisible;

    @Schema(description = "是否启用二级菜单")
    private Boolean subMenuEnabled;

    @Schema(description = "排序值")
    private Integer sortOrder;

    @Schema(description = "二级菜单列表")
    private List<SubColumnResponse> subColumns;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @Schema(description = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}
