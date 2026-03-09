package com.educp.content.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.educp.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName("columns")
@EqualsAndHashCode(callSuper = true)
public class Column extends BaseEntity {

    private String name;

    private String code;

    private String icon;

    private String routePath;

    @TableField("is_system")
    private Boolean isSystem;

    @TableField("is_visible")
    private Boolean isVisible;

    @TableField("sub_menu_enabled")
    private Boolean subMenuEnabled;

    private Integer sortOrder;

    private Long createdBy;
}
