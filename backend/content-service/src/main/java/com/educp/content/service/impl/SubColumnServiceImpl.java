package com.educp.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.educp.common.exception.BusinessException;
import com.educp.common.exception.ResourceNotFoundException;
import com.educp.common.security.UserContext;
import com.educp.content.converter.SubColumnConverter;
import com.educp.content.dto.request.CreateSubColumnRequest;
import com.educp.content.dto.request.SortSubColumnRequest;
import com.educp.content.dto.request.UpdateSubColumnRequest;
import com.educp.content.dto.response.SubColumnResponse;
import com.educp.content.entity.Column;
import com.educp.content.entity.SubColumn;
import com.educp.content.mapper.ColumnMapper;
import com.educp.content.mapper.SubColumnMapper;
import com.educp.content.service.SubColumnService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubColumnServiceImpl implements SubColumnService {

    private final SubColumnMapper subColumnMapper;
    private final ColumnMapper columnMapper;

    @Override
    public List<SubColumnResponse> listByColumnId(Long columnId) {
        getParentColumn(columnId);
        return listActiveByColumnId(columnId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long create(Long columnId, CreateSubColumnRequest request) {
        Column parent = getParentColumn(columnId);
        checkParentSubMenuEnabled(parent);
        checkNotSchoolsColumn(parent);
        checkNameUnique(columnId, request.getName(), null);
        checkCodeUnique(columnId, request.getCode(), null);

        SubColumn subColumn = SubColumnConverter.toEntity(request, columnId);
        UserContext ctx = UserContext.current();
        if (ctx != null) {
            subColumn.setCreatedBy(ctx.getUserId());
        }

        subColumnMapper.insert(subColumn);
        log.info("二级菜单创建成功: id={}, columnId={}, name={}, code={}",
                subColumn.getId(), columnId, subColumn.getName(), subColumn.getCode());
        return subColumn.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long columnId, Long id, UpdateSubColumnRequest request) {
        SubColumn subColumn = getSubColumn(columnId, id);

        // 系统预置二级菜单 code 和 categoryCode 不可修改
        if (Boolean.TRUE.equals(subColumn.getIsSystem())) {
            if (request.getCode() != null && !subColumn.getCode().equals(request.getCode())) {
                throw new BusinessException("系统预置二级菜单代码不可修改");
            }
            if (request.getCategoryCode() != null && !request.getCategoryCode().equals(subColumn.getCategoryCode())) {
                throw new BusinessException("系统预置二级菜单分类代码不可修改");
            }
        }

        // 唯一性校验
        if (request.getName() != null && !request.getName().equals(subColumn.getName())) {
            checkNameUnique(columnId, request.getName(), id);
        }
        if (request.getCode() != null && !request.getCode().equals(subColumn.getCode())) {
            checkCodeUnique(columnId, request.getCode(), id);
        }

        // 更新字段（仅非 null 字段）
        if (request.getName() != null) {
            subColumn.setName(request.getName());
        }
        if (request.getCode() != null) {
            subColumn.setCode(request.getCode());
        }
        if (request.getCategoryCode() != null) {
            subColumn.setCategoryCode(request.getCategoryCode());
        }
        if (request.getSortOrder() != null) {
            subColumn.setSortOrder(request.getSortOrder());
        }

        subColumnMapper.updateById(subColumn);
        log.info("二级菜单更新成功: id={}, columnId={}", id, columnId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long columnId, Long id) {
        SubColumn subColumn = getSubColumn(columnId, id);

        if (Boolean.TRUE.equals(subColumn.getIsSystem())) {
            throw new BusinessException("系统预置二级菜单不可删除");
        }

        subColumnMapper.deleteById(id);
        log.info("二级菜单删除成功: id={}, columnId={}, name={}", id, columnId, subColumn.getName());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSort(Long columnId, SortSubColumnRequest request) {
        for (SortSubColumnRequest.SortItem item : request.getItems()) {
            SubColumn subColumn = new SubColumn();
            subColumn.setId(item.getId());
            subColumn.setSortOrder(item.getSortOrder());
            subColumnMapper.updateById(subColumn);
        }
        log.info("二级菜单排序更新成功: columnId={}, {} 条", columnId, request.getItems().size());
    }

    @Override
    public List<SubColumnResponse> listActiveByColumnId(Long columnId) {
        LambdaQueryWrapper<SubColumn> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SubColumn::getColumnId, columnId)
               .orderByAsc(SubColumn::getSortOrder);
        List<SubColumn> subColumns = subColumnMapper.selectList(wrapper);
        return SubColumnConverter.toResponseList(subColumns);
    }

    @Override
    public boolean existsByColumnId(Long columnId) {
        LambdaQueryWrapper<SubColumn> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SubColumn::getColumnId, columnId);
        return subColumnMapper.selectCount(wrapper) > 0;
    }

    // ==================== 私有方法 ====================

    private Column getParentColumn(Long columnId) {
        Column column = columnMapper.selectById(columnId);
        if (column == null) {
            throw new ResourceNotFoundException("父栏目", columnId);
        }
        return column;
    }

    private SubColumn getSubColumn(Long columnId, Long id) {
        SubColumn subColumn = subColumnMapper.selectById(id);
        if (subColumn == null || !subColumn.getColumnId().equals(columnId)) {
            throw new ResourceNotFoundException("二级菜单", id);
        }
        return subColumn;
    }

    private void checkParentSubMenuEnabled(Column parent) {
        if (!Boolean.TRUE.equals(parent.getSubMenuEnabled())) {
            throw new BusinessException("该栏目未启用二级菜单");
        }
    }

    private void checkNotSchoolsColumn(Column parent) {
        if ("schools".equals(parent.getCode())) {
            throw new BusinessException("示范校共体不支持二级菜单管理");
        }
    }

    private void checkNameUnique(Long columnId, String name, Long excludeId) {
        LambdaQueryWrapper<SubColumn> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SubColumn::getColumnId, columnId)
               .eq(SubColumn::getName, name);
        if (excludeId != null) {
            wrapper.ne(SubColumn::getId, excludeId);
        }
        if (subColumnMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("该栏目下已存在同名二级菜单");
        }
    }

    private void checkCodeUnique(Long columnId, String code, Long excludeId) {
        LambdaQueryWrapper<SubColumn> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SubColumn::getColumnId, columnId)
               .eq(SubColumn::getCode, code);
        if (excludeId != null) {
            wrapper.ne(SubColumn::getId, excludeId);
        }
        if (subColumnMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("该栏目下已存在同代码二级菜单");
        }
    }
}
