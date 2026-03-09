package com.educp.content.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "更新栏目请求")
public class UpdateColumnRequest {

    @Size(max = 50, message = "栏目名称不能超过50字符")
    @Schema(description = "栏目名称")
    private String name;

    @Size(max = 50, message = "栏目代码不能超过50字符")
    @Pattern(regexp = "^[a-z][a-z0-9_-]*$", message = "栏目代码格式不正确")
    @Schema(description = "栏目代码")
    private String code;

    @Schema(description = "栏目图标")
    private String icon;

    @Size(max = 200, message = "路由路径不能超过200字符")
    @Schema(description = "前端路由路径")
    private String routePath;

    @Schema(description = "是否可见")
    private Boolean isVisible;

    @Schema(description = "是否启用二级菜单")
    private Boolean subMenuEnabled;

    @Min(value = 0, message = "排序值必须为非负整数")
    @Schema(description = "排序值")
    private Integer sortOrder;
}
