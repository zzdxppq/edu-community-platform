package com.educp.content.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.educp.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName("sub_columns")
@EqualsAndHashCode(callSuper = true)
public class SubColumn extends BaseEntity {

    private Long columnId;

    private String name;

    private String code;

    private String categoryCode;

    @TableField("is_system")
    private Boolean isSystem;

    private Integer sortOrder;

    private Long createdBy;
}
