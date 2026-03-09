package com.educp.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.educp.common.exception.BusinessException;
import com.educp.common.exception.ResourceNotFoundException;
import com.educp.common.security.UserContext;
import com.educp.content.converter.ColumnConverter;
import com.educp.content.dto.request.CreateColumnRequest;
import com.educp.content.dto.request.SortColumnRequest;
import com.educp.content.dto.request.UpdateColumnRequest;
import com.educp.content.dto.response.ColumnResponse;
import com.educp.content.entity.Column;
import com.educp.content.mapper.ColumnMapper;
import com.educp.content.service.ColumnService;
import com.educp.content.service.SubColumnService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ColumnServiceImpl implements ColumnService {

    private final ColumnMapper columnMapper;
    @Lazy
    private final SubColumnService subColumnService;

    @Override
    public List<ColumnResponse> listAll() {
        LambdaQueryWrapper<Column> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Column::getSortOrder);
        List<Column> columns = columnMapper.selectList(wrapper);
        return ColumnConverter.toResponseList(columns);
    }

    @Override
    public List<ColumnResponse> listVisible() {
        LambdaQueryWrapper<Column> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Column::getIsVisible, true)
               .orderByAsc(Column::getSortOrder);
        List<Column> columns = columnMapper.selectList(wrapper);
        List<ColumnResponse> responses = ColumnConverter.toResponseList(columns);

        // 为启用二级菜单的栏目加载 subColumns
        for (ColumnResponse response : responses) {
            if (Boolean.TRUE.equals(response.getSubMenuEnabled())) {
                response.setSubColumns(subColumnService.listActiveByColumnId(response.getId()));
            } else {
                response.setSubColumns(Collections.emptyList());
            }
        }
        return responses;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long create(CreateColumnRequest request) {
        checkNameUnique(request.getName(), null);
        checkCodeUnique(request.getCode(), null);

        Column column = ColumnConverter.toEntity(request);
        UserContext ctx = UserContext.current();
        if (ctx != null) {
            column.setCreatedBy(ctx.getUserId());
        }

        columnMapper.insert(column);
        log.info("栏目创建成功: id={}, name={}, code={}", column.getId(), column.getName(), column.getCode());
        return column.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, UpdateColumnRequest request) {
        Column column = getColumnById(id);

        // 系统预置栏目 code 不可修改
        if (Boolean.TRUE.equals(column.getIsSystem()) && request.getCode() != null
                && !column.getCode().equals(request.getCode())) {
            throw new BusinessException("系统预置栏目代码不可修改");
        }

        // 唯一性校验
        if (request.getName() != null && !request.getName().equals(column.getName())) {
            checkNameUnique(request.getName(), id);
        }
        if (request.getCode() != null && !request.getCode().equals(column.getCode())) {
            checkCodeUnique(request.getCode(), id);
        }

        // 更新字段（仅非 null 字段）
        if (request.getName() != null) {
            column.setName(request.getName());
        }
        if (request.getCode() != null) {
            column.setCode(request.getCode());
        }
        if (request.getIcon() != null) {
            column.setIcon(request.getIcon());
        }
        if (request.getRoutePath() != null) {
            column.setRoutePath(request.getRoutePath());
        }
        if (request.getIsVisible() != null) {
            column.setIsVisible(request.getIsVisible());
        }
        if (request.getSubMenuEnabled() != null) {
            column.setSubMenuEnabled(request.getSubMenuEnabled());
        }
        if (request.getSortOrder() != null) {
            column.setSortOrder(request.getSortOrder());
        }

        columnMapper.updateById(column);
        log.info("栏目更新成功: id={}", id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        Column column = getColumnById(id);

        if (Boolean.TRUE.equals(column.getIsSystem())) {
            throw new BusinessException("系统预置栏目不可删除，仅可隐藏");
        }

        if (hasChildren(id)) {
            throw new BusinessException("该栏目下有关联内容，不可删除");
        }

        columnMapper.deleteById(id);
        log.info("栏目删除成功: id={}, name={}", id, column.getName());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSort(SortColumnRequest request) {
        for (SortColumnRequest.SortItem item : request.getItems()) {
            Column column = new Column();
            column.setId(item.getId());
            column.setSortOrder(item.getSortOrder());
            columnMapper.updateById(column);
        }
        log.info("栏目排序更新成功: {} 条", request.getItems().size());
    }

    @Override
    public boolean hasChildren(Long columnId) {
        return subColumnService.existsByColumnId(columnId);
    }

    // ==================== 私有方法 ====================

    private Column getColumnById(Long id) {
        Column column = columnMapper.selectById(id);
        if (column == null) {
            throw new ResourceNotFoundException("栏目", id);
        }
        return column;
    }

    private void checkNameUnique(String name, Long excludeId) {
        LambdaQueryWrapper<Column> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Column::getName, name);
        if (excludeId != null) {
            wrapper.ne(Column::getId, excludeId);
        }
        if (columnMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("栏目名称已存在");
        }
    }

    private void checkCodeUnique(String code, Long excludeId) {
        LambdaQueryWrapper<Column> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Column::getCode, code);
        if (excludeId != null) {
            wrapper.ne(Column::getId, excludeId);
        }
        if (columnMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("栏目代码已存在");
        }
    }
}
