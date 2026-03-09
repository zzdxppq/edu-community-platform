/**
 * Test for Story 4.2: 二级栏目管理 — SubColumnService Layer
 *
 * Test Design: docs/qa/assessments/4.2-test-design-20260309.md
 */
package com.educp.content.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.educp.common.exception.BusinessException;
import com.educp.common.exception.ResourceNotFoundException;
import com.educp.content.dto.request.CreateSubColumnRequest;
import com.educp.content.dto.request.SortSubColumnRequest;
import com.educp.content.dto.request.UpdateSubColumnRequest;
import com.educp.content.dto.response.SubColumnResponse;
import com.educp.content.entity.Column;
import com.educp.content.entity.SubColumn;
import com.educp.content.mapper.ColumnMapper;
import com.educp.content.mapper.SubColumnMapper;
import com.educp.content.service.impl.SubColumnServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SubColumnServiceImplTest {

    @Mock
    private SubColumnMapper subColumnMapper;

    @Mock
    private ColumnMapper columnMapper;

    @InjectMocks
    private SubColumnServiceImpl subColumnService;

    private Column policyColumn;
    private Column schoolsColumn;
    private Column disabledColumn;
    private SubColumn systemSubColumn;
    private SubColumn customSubColumn;

    @BeforeEach
    void setUp() {
        policyColumn = new Column();
        policyColumn.setId(3L);
        policyColumn.setName("政策文件");
        policyColumn.setCode("policy");
        policyColumn.setIsSystem(true);
        policyColumn.setSubMenuEnabled(true);

        schoolsColumn = new Column();
        schoolsColumn.setId(4L);
        schoolsColumn.setName("示范校共体");
        schoolsColumn.setCode("schools");
        schoolsColumn.setIsSystem(true);
        schoolsColumn.setSubMenuEnabled(false);

        disabledColumn = new Column();
        disabledColumn.setId(10L);
        disabledColumn.setName("自定义");
        disabledColumn.setCode("custom");
        disabledColumn.setIsSystem(false);
        disabledColumn.setSubMenuEnabled(false);

        systemSubColumn = new SubColumn();
        systemSubColumn.setId(1L);
        systemSubColumn.setColumnId(3L);
        systemSubColumn.setName("国家政策");
        systemSubColumn.setCode("national-policy");
        systemSubColumn.setCategoryCode("national");
        systemSubColumn.setIsSystem(true);
        systemSubColumn.setSortOrder(1);
        systemSubColumn.setCreatedAt(LocalDateTime.now());
        systemSubColumn.setUpdatedAt(LocalDateTime.now());

        customSubColumn = new SubColumn();
        customSubColumn.setId(10L);
        customSubColumn.setColumnId(3L);
        customSubColumn.setName("自定义菜单");
        customSubColumn.setCode("custom-menu");
        customSubColumn.setCategoryCode("custom");
        customSubColumn.setIsSystem(false);
        customSubColumn.setSortOrder(5);
        customSubColumn.setCreatedAt(LocalDateTime.now());
        customSubColumn.setUpdatedAt(LocalDateTime.now());
    }

    // ============================================================
    // AC2: 二级菜单 CRUD 与排序
    // ============================================================

    @Nested
    @DisplayName("创建二级菜单")
    class CreateSubColumn {

        @Test
        @DisplayName("4.2-UNIT-004: 创建二级菜单成功（有效请求）")
        void shouldCreateSubColumnSuccessfully() {
            when(columnMapper.selectById(3L)).thenReturn(policyColumn);
            when(subColumnMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(0L);
            when(subColumnMapper.insert(any(SubColumn.class))).thenAnswer(invocation -> {
                SubColumn sc = invocation.getArgument(0);
                sc.setId(100L);
                return 1;
            });

            CreateSubColumnRequest request = new CreateSubColumnRequest();
            request.setName("测试菜单");
            request.setCode("test-menu");
            request.setCategoryCode("test");
            request.setSortOrder(3);

            Long id = subColumnService.create(3L, request);

            assertThat(id).isEqualTo(100L);
            verify(subColumnMapper).insert(argThat(sc ->
                    "测试菜单".equals(sc.getName())
                            && "test-menu".equals(sc.getCode())
                            && sc.getColumnId().equals(3L)
                            && Boolean.FALSE.equals(sc.getIsSystem())
            ));
        }

        @Test
        @DisplayName("4.2-UNIT-005: 同父栏目下 name 唯一性校验（重复→422）")
        void shouldRejectDuplicateNameInSameParent() {
            when(columnMapper.selectById(3L)).thenReturn(policyColumn);
            when(subColumnMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(1L);

            CreateSubColumnRequest request = new CreateSubColumnRequest();
            request.setName("国家政策");
            request.setCode("new-code");

            assertThatThrownBy(() -> subColumnService.create(3L, request))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("该栏目下已存在同名二级菜单");

            verify(subColumnMapper, never()).insert(any());
        }

        @Test
        @DisplayName("4.2-UNIT-006: 同父栏目下 code 唯一性校验（重复→422）")
        void shouldRejectDuplicateCodeInSameParent() {
            when(columnMapper.selectById(3L)).thenReturn(policyColumn);
            when(subColumnMapper.selectCount(any(LambdaQueryWrapper.class)))
                    .thenReturn(0L)  // name check passes
                    .thenReturn(1L); // code check fails

            CreateSubColumnRequest request = new CreateSubColumnRequest();
            request.setName("新名称");
            request.setCode("national-policy");

            assertThatThrownBy(() -> subColumnService.create(3L, request))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("该栏目下已存在同代码二级菜单");

            verify(subColumnMapper, never()).insert(any());
        }

        @Test
        @DisplayName("4.2-UNIT-010: 为 schools 栏目创建二级菜单被阻止（422）")
        void shouldRejectCreationForSchoolsColumn() {
            schoolsColumn.setSubMenuEnabled(true);
            when(columnMapper.selectById(4L)).thenReturn(schoolsColumn);

            CreateSubColumnRequest request = new CreateSubColumnRequest();
            request.setName("测试");
            request.setCode("test");

            assertThatThrownBy(() -> subColumnService.create(4L, request))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("示范校共体不支持二级菜单管理");

            verify(subColumnMapper, never()).insert(any());
        }

        @Test
        @DisplayName("4.2-UNIT-011: 父栏目未启用二级菜单时创建被阻止（422）")
        void shouldRejectCreationWhenParentNotEnabled() {
            when(columnMapper.selectById(10L)).thenReturn(disabledColumn);

            CreateSubColumnRequest request = new CreateSubColumnRequest();
            request.setName("测试");
            request.setCode("test");

            assertThatThrownBy(() -> subColumnService.create(10L, request))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("该栏目未启用二级菜单");

            verify(subColumnMapper, never()).insert(any());
        }
    }

    @Nested
    @DisplayName("更新二级菜单")
    class UpdateSubColumn {

        @Test
        @DisplayName("4.2-UNIT-008: 修改系统预置 code 被阻止（422）")
        void shouldRejectSystemPresetCodeModification() {
            when(subColumnMapper.selectById(1L)).thenReturn(systemSubColumn);

            UpdateSubColumnRequest request = new UpdateSubColumnRequest();
            request.setCode("new-code");

            assertThatThrownBy(() -> subColumnService.update(3L, 1L, request))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("系统预置二级菜单代码不可修改");

            verify(subColumnMapper, never()).updateById(any());
        }

        @Test
        @DisplayName("4.2-UNIT-009: 修改系统预置 name 允许成功")
        void shouldAllowSystemPresetNameModification() {
            when(subColumnMapper.selectById(1L)).thenReturn(systemSubColumn);
            when(subColumnMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(0L);
            when(subColumnMapper.updateById(any(SubColumn.class))).thenReturn(1);

            UpdateSubColumnRequest request = new UpdateSubColumnRequest();
            request.setName("新国家政策");

            subColumnService.update(3L, 1L, request);

            verify(subColumnMapper).updateById(argThat(sc ->
                    "新国家政策".equals(sc.getName())
                            && "national-policy".equals(sc.getCode())
            ));
        }

        @Test
        @DisplayName("修改系统预置 categoryCode 被阻止（422）")
        void shouldRejectSystemPresetCategoryCodeModification() {
            when(subColumnMapper.selectById(1L)).thenReturn(systemSubColumn);

            UpdateSubColumnRequest request = new UpdateSubColumnRequest();
            request.setCategoryCode("new-category");

            assertThatThrownBy(() -> subColumnService.update(3L, 1L, request))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("系统预置二级菜单分类代码不可修改");
        }
    }

    @Nested
    @DisplayName("删除二级菜单")
    class DeleteSubColumn {

        @Test
        @DisplayName("4.2-UNIT-007: 删除系统预置二级菜单被阻止（is_system=1→422）")
        void shouldRejectSystemPresetDeletion() {
            when(subColumnMapper.selectById(1L)).thenReturn(systemSubColumn);

            assertThatThrownBy(() -> subColumnService.delete(3L, 1L))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("系统预置二级菜单不可删除");

            verify(subColumnMapper, never()).deleteById(any(Long.class));
        }

        @Test
        @DisplayName("4.2-UNIT-013: 软删除设置 deleted_at")
        void shouldSoftDeleteBySettingDeletedAt() {
            when(subColumnMapper.selectById(10L)).thenReturn(customSubColumn);
            when(subColumnMapper.deleteById(10L)).thenReturn(1);

            subColumnService.delete(3L, 10L);

            verify(subColumnMapper).deleteById(10L);
        }
    }

    @Nested
    @DisplayName("批量排序")
    class SortSubColumns {

        @Test
        @DisplayName("4.2-UNIT-012: 批量排序更新成功")
        void shouldBatchUpdateSortOrder() {
            SortSubColumnRequest request = new SortSubColumnRequest();
            SortSubColumnRequest.SortItem item1 = new SortSubColumnRequest.SortItem();
            item1.setId(1L);
            item1.setSortOrder(2);
            SortSubColumnRequest.SortItem item2 = new SortSubColumnRequest.SortItem();
            item2.setId(2L);
            item2.setSortOrder(1);
            request.setItems(Arrays.asList(item1, item2));

            when(subColumnMapper.updateById(any(SubColumn.class))).thenReturn(1);

            subColumnService.updateSort(3L, request);

            verify(subColumnMapper, times(2)).updateById(any(SubColumn.class));
            verify(subColumnMapper).updateById(argThat(sc ->
                    sc.getId().equals(1L) && sc.getSortOrder().equals(2)));
            verify(subColumnMapper).updateById(argThat(sc ->
                    sc.getId().equals(2L) && sc.getSortOrder().equals(1)));
        }
    }

    // ============================================================
    // AC4: hasChildren / existsByColumnId
    // ============================================================

    @Nested
    @DisplayName("hasChildren 实际实现")
    class HasChildren {

        @Test
        @DisplayName("4.2-UNIT-017: hasChildren 有 sub_columns 记录→true")
        void shouldReturnTrueWhenSubColumnsExist() {
            when(subColumnMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(2L);

            assertThat(subColumnService.existsByColumnId(3L)).isTrue();
        }

        @Test
        @DisplayName("4.2-UNIT-018: hasChildren 无 sub_columns 记录→false")
        void shouldReturnFalseWhenNoSubColumns() {
            when(subColumnMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(0L);

            assertThat(subColumnService.existsByColumnId(99L)).isFalse();
        }
    }

    // ============================================================
    // AC4: listActiveByColumnId (用于公开 API)
    // ============================================================

    @Nested
    @DisplayName("公开 API subColumns 加载")
    class PublicApiSubColumns {

        @Test
        @DisplayName("4.2-UNIT-014/016: listActiveByColumnId 返回按 sort_order 升序排列的二级菜单")
        void shouldReturnSubColumnsSortedBySortOrder() {
            SubColumn sc1 = new SubColumn();
            sc1.setId(2L);
            sc1.setColumnId(3L);
            sc1.setName("省级政策");
            sc1.setCode("provincial-policy");
            sc1.setCategoryCode("provincial");
            sc1.setIsSystem(true);
            sc1.setSortOrder(2);
            sc1.setCreatedAt(LocalDateTime.now());
            sc1.setUpdatedAt(LocalDateTime.now());

            when(subColumnMapper.selectList(any(LambdaQueryWrapper.class)))
                    .thenReturn(Arrays.asList(systemSubColumn, sc1));

            List<SubColumnResponse> result = subColumnService.listActiveByColumnId(3L);

            assertThat(result).hasSize(2);
            assertThat(result.get(0).getName()).isEqualTo("国家政策");
            assertThat(result.get(1).getName()).isEqualTo("省级政策");
        }
    }

    // ============================================================
    // Error Handling
    // ============================================================

    @Nested
    @DisplayName("Error Handling")
    class ErrorHandling {

        @Test
        @DisplayName("父栏目不存在→404")
        void create_parentNotFound_throws404() {
            when(columnMapper.selectById(999L)).thenReturn(null);

            CreateSubColumnRequest request = new CreateSubColumnRequest();
            request.setName("测试");
            request.setCode("test");

            assertThatThrownBy(() -> subColumnService.create(999L, request))
                    .isInstanceOf(ResourceNotFoundException.class);
        }

        @Test
        @DisplayName("二级菜单不存在→404")
        void update_subColumnNotFound_throws404() {
            when(subColumnMapper.selectById(999L)).thenReturn(null);

            UpdateSubColumnRequest request = new UpdateSubColumnRequest();
            request.setName("test");

            assertThatThrownBy(() -> subColumnService.update(3L, 999L, request))
                    .isInstanceOf(ResourceNotFoundException.class);
        }

        @Test
        @DisplayName("二级菜单不属于指定父栏目→404")
        void update_subColumnWrongParent_throws404() {
            SubColumn wrongParent = new SubColumn();
            wrongParent.setId(10L);
            wrongParent.setColumnId(5L);
            when(subColumnMapper.selectById(10L)).thenReturn(wrongParent);

            UpdateSubColumnRequest request = new UpdateSubColumnRequest();
            request.setName("test");

            assertThatThrownBy(() -> subColumnService.update(3L, 10L, request))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }

    // ============================================================
    // Blind Spot Scenarios
    // ============================================================

    @Nested
    @DisplayName("[BLIND-SPOT] DATA 数据一致性")
    class BlindSpotData {

        @Test
        @DisplayName("[BLIND-SPOT] 4.2-BLIND-DATA-001: 软删除后同名/同代码复用")
        void shouldAllowReuseOfSoftDeletedNameAndCode() {
            when(columnMapper.selectById(3L)).thenReturn(policyColumn);
            when(subColumnMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(0L);
            when(subColumnMapper.insert(any(SubColumn.class))).thenAnswer(invocation -> {
                SubColumn sc = invocation.getArgument(0);
                sc.setId(200L);
                return 1;
            });

            CreateSubColumnRequest request = new CreateSubColumnRequest();
            request.setName("已删除的名称");
            request.setCode("deleted-code");

            Long id = subColumnService.create(3L, request);

            assertThat(id).isEqualTo(200L);
        }
    }

    @Nested
    @DisplayName("[BLIND-SPOT] FLOW 流程细节")
    class BlindSpotFlow {

        @Test
        @DisplayName("[BLIND-SPOT] 4.2-BLIND-FLOW-001: 管理过程中禁用父栏目二级菜单开关")
        void shouldHandleParentDisabledDuringManagement() {
            // Scenario: parent is disabled between list and create
            disabledColumn.setSubMenuEnabled(false);
            when(columnMapper.selectById(10L)).thenReturn(disabledColumn);

            CreateSubColumnRequest request = new CreateSubColumnRequest();
            request.setName("测试");
            request.setCode("test");

            assertThatThrownBy(() -> subColumnService.create(10L, request))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("该栏目未启用二级菜单");
        }
    }
}
