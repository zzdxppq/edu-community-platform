package com.educp.content.service;

import com.educp.content.dto.request.CreateSubColumnRequest;
import com.educp.content.dto.request.SortSubColumnRequest;
import com.educp.content.dto.request.UpdateSubColumnRequest;
import com.educp.content.dto.response.SubColumnResponse;

import java.util.List;

public interface SubColumnService {

    /**
     * 获取指定栏目下的全部二级菜单（管理端，按 sort_order 升序）
     *
     * @throws com.educp.common.exception.ResourceNotFoundException 父栏目不存在
     */
    List<SubColumnResponse> listByColumnId(Long columnId);

    /**
     * 创建二级菜单
     *
     * @return 新创建的二级菜单ID
     * @throws com.educp.common.exception.BusinessException 名称/代码重复、父栏目未启用二级菜单、示范校共体栏目
     * @throws com.educp.common.exception.ResourceNotFoundException 父栏目不存在
     */
    Long create(Long columnId, CreateSubColumnRequest request);

    /**
     * 更新二级菜单
     *
     * @throws com.educp.common.exception.BusinessException 系统预置code不可修改、名称/代码重复
     * @throws com.educp.common.exception.ResourceNotFoundException 二级菜单不存在
     */
    void update(Long columnId, Long id, UpdateSubColumnRequest request);

    /**
     * 删除二级菜单（软删除）
     *
     * @throws com.educp.common.exception.BusinessException 系统预置不可删除
     * @throws com.educp.common.exception.ResourceNotFoundException 二级菜单不存在
     */
    void delete(Long columnId, Long id);

    /**
     * 批量更新排序
     */
    void updateSort(Long columnId, SortSubColumnRequest request);

    /**
     * 获取指定栏目下未删除的二级菜单（内部使用，按 sort_order 升序）
     */
    List<SubColumnResponse> listActiveByColumnId(Long columnId);

    /**
     * 检查栏目是否有二级菜单记录
     */
    boolean existsByColumnId(Long columnId);
}
