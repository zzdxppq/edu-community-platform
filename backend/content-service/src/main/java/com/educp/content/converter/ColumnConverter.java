package com.educp.content.converter;

import com.educp.content.dto.request.CreateColumnRequest;
import com.educp.content.dto.response.ColumnResponse;
import com.educp.content.entity.Column;

import java.util.List;

public final class ColumnConverter {

    private ColumnConverter() {
    }

    public static Column toEntity(CreateColumnRequest request) {
        Column column = new Column();
        column.setName(request.getName());
        column.setCode(request.getCode());
        column.setIcon(request.getIcon());
        column.setRoutePath(request.getRoutePath());
        column.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0);
        column.setIsSystem(false);
        column.setIsVisible(true);
        return column;
    }

    public static ColumnResponse toResponse(Column entity) {
        ColumnResponse response = new ColumnResponse();
        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setCode(entity.getCode());
        response.setIcon(entity.getIcon());
        response.setRoutePath(entity.getRoutePath());
        response.setIsSystem(entity.getIsSystem());
        response.setIsVisible(entity.getIsVisible());
        response.setSubMenuEnabled(entity.getSubMenuEnabled());
        response.setSortOrder(entity.getSortOrder());
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());
        return response;
    }

    public static List<ColumnResponse> toResponseList(List<Column> entities) {
        return entities.stream().map(ColumnConverter::toResponse).toList();
    }
}
