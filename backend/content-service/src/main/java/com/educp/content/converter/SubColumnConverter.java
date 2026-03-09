package com.educp.content.converter;

import com.educp.content.dto.request.CreateSubColumnRequest;
import com.educp.content.dto.response.SubColumnResponse;
import com.educp.content.entity.SubColumn;

import java.util.List;

public final class SubColumnConverter {

    private SubColumnConverter() {
    }

    public static SubColumn toEntity(CreateSubColumnRequest request, Long columnId) {
        SubColumn subColumn = new SubColumn();
        subColumn.setColumnId(columnId);
        subColumn.setName(request.getName());
        subColumn.setCode(request.getCode());
        subColumn.setCategoryCode(request.getCategoryCode());
        subColumn.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0);
        subColumn.setIsSystem(false);
        return subColumn;
    }

    public static SubColumnResponse toResponse(SubColumn entity) {
        SubColumnResponse response = new SubColumnResponse();
        response.setId(entity.getId());
        response.setColumnId(entity.getColumnId());
        response.setName(entity.getName());
        response.setCode(entity.getCode());
        response.setCategoryCode(entity.getCategoryCode());
        response.setIsSystem(entity.getIsSystem());
        response.setSortOrder(entity.getSortOrder());
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());
        return response;
    }

    public static List<SubColumnResponse> toResponseList(List<SubColumn> entities) {
        return entities.stream().map(SubColumnConverter::toResponse).toList();
    }
}
