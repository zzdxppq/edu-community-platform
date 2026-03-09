package com.educp.content.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "更新二级菜单请求")
public class UpdateSubColumnRequest {

    @Size(max = 50, message = "二级菜单名称不能超过50字符")
    @Schema(description = "二级菜单名称")
    private String name;

    @Size(max = 50, message = "二级菜单代码不能超过50字符")
    @Pattern(regexp = "^[a-z][a-z0-9_-]*$", message = "代码只能包含小写字母、数字、下划线和连字符，且以小写字母开头")
    @Schema(description = "二级菜单代码")
    private String code;

    @Size(max = 50, message = "分类代码不能超过50字符")
    @Schema(description = "关联内容分类代码")
    private String categoryCode;

    @Min(value = 0, message = "排序值必须为非负整数")
    @Schema(description = "排序值")
    private Integer sortOrder;
}
