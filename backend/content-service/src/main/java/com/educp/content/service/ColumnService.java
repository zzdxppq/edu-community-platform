package com.educp.content.service;

import com.educp.content.dto.request.CreateColumnRequest;
import com.educp.content.dto.request.SortColumnRequest;
import com.educp.content.dto.request.UpdateColumnRequest;
import com.educp.content.dto.response.ColumnResponse;

import java.util.List;

public interface ColumnService {

    /**
     * 获取全部栏目（管理端，按 sort_order 升序）
     */
    List<ColumnResponse> listAll();

    /**
     * 获取可见栏目（公开 API，仅 is_visible=1，按 sort_order 升序）
     */
    List<ColumnResponse> listVisible();

    /**
     * 创建栏目
     *
     * @return 新创建的栏目ID
     * @throws com.educp.common.exception.BusinessException 名称或代码重复
     */
    Long create(CreateColumnRequest request);

    /**
     * 更新栏目
     *
     * @throws com.educp.common.exception.ResourceNotFoundException 栏目不存在
     * @throws com.educp.common.exception.BusinessException 系统预置栏目不可修改 code / 名称重复
     */
    void update(Long id, UpdateColumnRequest request);

    /**
     * 删除栏目（软删除）
     *
     * @throws com.educp.common.exception.ResourceNotFoundException 栏目不存在
     * @throws com.educp.common.exception.BusinessException 系统预置栏目不可删除 / 有关联内容
     */
    void delete(Long id);

    /**
     * 批量更新排序
     *
     * @throws com.educp.common.exception.BusinessException 排序列表为空
     */
    void updateSort(SortColumnRequest request);

    /**
     * 检查栏目是否有子栏目（预留接口，Story 4.2 实现后替换）
     */
    boolean hasChildren(Long columnId);
}
