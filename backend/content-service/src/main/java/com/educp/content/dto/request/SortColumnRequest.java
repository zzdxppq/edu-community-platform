package com.educp.content.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "栏目排序请求")
public class SortColumnRequest {

    @NotEmpty(message = "排序列表不能为空")
    @Valid
    private List<SortItem> items;

    @Data
    public static class SortItem {

        @NotNull(message = "栏目ID不能为空")
        private Long id;

        @NotNull(message = "排序值不能为空")
        @Min(value = 0, message = "排序值必须为非负整数")
        private Integer sortOrder;
    }
}
