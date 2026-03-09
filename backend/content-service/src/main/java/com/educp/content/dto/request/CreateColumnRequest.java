package com.educp.content.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "创建栏目请求")
public class CreateColumnRequest {

    @NotBlank(message = "栏目名称不能为空")
    @Size(max = 50, message = "栏目名称不能超过50字符")
    @Schema(description = "栏目名称")
    private String name;

    @NotBlank(message = "栏目代码不能为空")
    @Size(max = 50, message = "栏目代码不能超过50字符")
    @Pattern(regexp = "^[a-z][a-z0-9_-]*$", message = "栏目代码只能包含小写字母、数字、下划线和连字符，且以小写字母开头")
    @Schema(description = "栏目代码")
    private String code;

    @Schema(description = "栏目图标")
    private String icon;

    @Size(max = 200, message = "路由路径不能超过200字符")
    @Schema(description = "前端路由路径")
    private String routePath;

    @Min(value = 0, message = "排序值必须为非负整数")
    @Schema(description = "排序值")
    private Integer sortOrder;
}
