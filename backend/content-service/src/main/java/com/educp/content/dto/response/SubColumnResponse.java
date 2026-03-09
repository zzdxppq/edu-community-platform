package com.educp.content.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "二级菜单响应")
public class SubColumnResponse {

    @Schema(description = "二级菜单ID")
    private Long id;

    @Schema(description = "父栏目ID")
    private Long columnId;

    @Schema(description = "二级菜单名称")
    private String name;

    @Schema(description = "二级菜单代码")
    private String code;

    @Schema(description = "关联内容分类代码")
    private String categoryCode;

    @Schema(description = "是否系统预置")
    private Boolean isSystem;

    @Schema(description = "排序值")
    private Integer sortOrder;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @Schema(description = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}
