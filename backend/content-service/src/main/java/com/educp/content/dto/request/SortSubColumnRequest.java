package com.educp.content.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "二级菜单排序请求")
public class SortSubColumnRequest {

    @NotEmpty(message = "排序列表不能为空")
    @Valid
    @Schema(description = "排序项列表")
    private List<SortItem> items;

    @Data
    public static class SortItem {

        @NotNull(message = "ID不能为空")
        @Schema(description = "二级菜单ID")
        private Long id;

        @NotNull(message = "排序值不能为空")
        @Min(value = 0, message = "排序值必须为非负整数")
        @Schema(description = "排序值")
        private Integer sortOrder;
    }
}
