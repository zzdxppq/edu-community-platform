/**
 * Test for Story 4.2: 二级栏目管理 — Controller Layer
 *
 * Test Design: docs/qa/assessments/4.2-test-design-20260309.md
 */
package com.educp.content.controller;

import com.educp.common.exception.BusinessException;
import com.educp.common.exception.ResourceNotFoundException;
import com.educp.common.handler.GlobalExceptionHandler;
import com.educp.common.security.RoleCheckAspect;
import com.educp.common.security.SecurityConstants;
import com.educp.common.security.UserContextFilter;
import com.educp.content.dto.request.CreateSubColumnRequest;
import com.educp.content.dto.request.UpdateSubColumnRequest;
import com.educp.content.dto.request.SortSubColumnRequest;
import com.educp.content.dto.response.SubColumnResponse;
import com.educp.content.service.SubColumnService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.bean.MockBean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SubColumnController.class)
@Import({GlobalExceptionHandler.class, RoleCheckAspect.class, UserContextFilter.class})
@EnableAspectJAutoProxy
class SubColumnControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SubColumnService subColumnService;

    private static final String BASE_URL = "/v1/columns/3/sub-columns";

    private SubColumnResponse buildSampleResponse(Long id, String name, String code, boolean isSystem) {
        SubColumnResponse r = new SubColumnResponse();
        r.setId(id);
        r.setColumnId(3L);
        r.setName(name);
        r.setCode(code);
        r.setIsSystem(isSystem);
        r.setSortOrder(1);
        r.setCreatedAt(LocalDateTime.now());
        r.setUpdatedAt(LocalDateTime.now());
        return r;
    }

    // ============================================================
    // AC2: 二级菜单 CRUD API 端点
    // ============================================================

    @Nested
    @DisplayName("POST /v1/columns/{columnId}/sub-columns")
    class CreateEndpoint {

        @Test
        @DisplayName("4.2-INT-003a: POST API 参数校验（name 空→400）")
        void shouldReturn400WhenNameIsBlank() throws Exception {
            String body = """
                    {"name": "", "code": "valid-code"}
                    """;

            mockMvc.perform(post(BASE_URL)
                            .header(SecurityConstants.HEADER_USER_ID, "1")
                            .header(SecurityConstants.HEADER_USER_ROLE, "1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("4.2-INT-003b: POST API 参数校验（code 格式错误→400）")
        void shouldReturn400WhenCodeFormatInvalid() throws Exception {
            String body = """
                    {"name": "有效名称", "code": "INVALID"}
                    """;

            mockMvc.perform(post(BASE_URL)
                            .header(SecurityConstants.HEADER_USER_ID, "1")
                            .header(SecurityConstants.HEADER_USER_ROLE, "1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("POST 创建成功返回 200 + id")
        void shouldCreateSuccessfully() throws Exception {
            when(subColumnService.create(eq(3L), any(CreateSubColumnRequest.class)))
                    .thenReturn(100L);

            String body = """
                    {"name": "测试菜单", "code": "test-menu", "sortOrder": 3}
                    """;

            mockMvc.perform(post(BASE_URL)
                            .header(SecurityConstants.HEADER_USER_ID, "1")
                            .header(SecurityConstants.HEADER_USER_ROLE, "1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.data").value(100));
        }

        @Test
        @DisplayName("POST 无权限（role=2）返回 403")
        void shouldReturn403WhenNotSuperAdmin() throws Exception {
            String body = """
                    {"name": "测试", "code": "test"}
                    """;

            mockMvc.perform(post(BASE_URL)
                            .header(SecurityConstants.HEADER_USER_ID, "2")
                            .header(SecurityConstants.HEADER_USER_ROLE, "2")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("POST 父栏目未启用二级菜单返回 422")
        void shouldReturn422WhenParentNotEnabled() throws Exception {
            when(subColumnService.create(eq(3L), any(CreateSubColumnRequest.class)))
                    .thenThrow(new BusinessException("该栏目未启用二级菜单"));

            String body = """
                    {"name": "测试", "code": "test"}
                    """;

            mockMvc.perform(post(BASE_URL)
                            .header(SecurityConstants.HEADER_USER_ID, "1")
                            .header(SecurityConstants.HEADER_USER_ROLE, "1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(jsonPath("$.message").value("该栏目未启用二级菜单"));
        }
    }

    @Nested
    @DisplayName("GET /v1/columns/{columnId}/sub-columns")
    class ListEndpoint {

        @Test
        @DisplayName("4.2-INT-004: GET API 返回该栏目下二级菜单列表")
        void shouldReturnSubColumnList() throws Exception {
            SubColumnResponse sub1 = buildSampleResponse(1L, "国家政策", "national-policy", true);
            SubColumnResponse sub2 = buildSampleResponse(2L, "省级政策", "provincial-policy", true);
            when(subColumnService.listByColumnId(3L)).thenReturn(Arrays.asList(sub1, sub2));

            mockMvc.perform(get(BASE_URL)
                            .header(SecurityConstants.HEADER_USER_ID, "1")
                            .header(SecurityConstants.HEADER_USER_ROLE, "1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.data.length()").value(2))
                    .andExpect(jsonPath("$.data[0].name").value("国家政策"))
                    .andExpect(jsonPath("$.data[1].name").value("省级政策"));
        }

        @Test
        @DisplayName("GET 父栏目不存在返回 404")
        void shouldReturn404WhenParentNotFound() throws Exception {
            when(subColumnService.listByColumnId(3L))
                    .thenThrow(new ResourceNotFoundException("父栏目", 3L));

            mockMvc.perform(get(BASE_URL)
                            .header(SecurityConstants.HEADER_USER_ID, "1")
                            .header(SecurityConstants.HEADER_USER_ROLE, "1"))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("PUT /v1/columns/{columnId}/sub-columns/{id}")
    class UpdateEndpoint {

        @Test
        @DisplayName("4.2-INT-005: PUT API 更新二级菜单")
        void shouldUpdateSubColumn() throws Exception {
            doNothing().when(subColumnService).update(eq(3L), eq(1L), any(UpdateSubColumnRequest.class));

            String body = """
                    {"name": "新国家政策", "sortOrder": 2}
                    """;

            mockMvc.perform(put(BASE_URL + "/1")
                            .header(SecurityConstants.HEADER_USER_ID, "1")
                            .header(SecurityConstants.HEADER_USER_ROLE, "1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(200));
        }

        @Test
        @DisplayName("PUT 修改系统预置 code 返回 422")
        void shouldReturn422WhenModifyingSystemCode() throws Exception {
            doThrow(new BusinessException("系统预置二级菜单代码不可修改"))
                    .when(subColumnService).update(eq(3L), eq(1L), any(UpdateSubColumnRequest.class));

            String body = """
                    {"code": "new-code"}
                    """;

            mockMvc.perform(put(BASE_URL + "/1")
                            .header(SecurityConstants.HEADER_USER_ID, "1")
                            .header(SecurityConstants.HEADER_USER_ROLE, "1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(jsonPath("$.message").value("系统预置二级菜单代码不可修改"));
        }
    }

    @Nested
    @DisplayName("DELETE /v1/columns/{columnId}/sub-columns/{id}")
    class DeleteEndpoint {

        @Test
        @DisplayName("4.2-INT-006: DELETE API 软删除二级菜单")
        void shouldSoftDeleteSubColumn() throws Exception {
            doNothing().when(subColumnService).delete(3L, 10L);

            mockMvc.perform(delete(BASE_URL + "/10")
                            .header(SecurityConstants.HEADER_USER_ID, "1")
                            .header(SecurityConstants.HEADER_USER_ROLE, "1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(200));
        }

        @Test
        @DisplayName("DELETE 系统预置二级菜单返回 422")
        void shouldReturn422WhenDeletingSystemPreset() throws Exception {
            doThrow(new BusinessException("系统预置二级菜单不可删除"))
                    .when(subColumnService).delete(3L, 1L);

            mockMvc.perform(delete(BASE_URL + "/1")
                            .header(SecurityConstants.HEADER_USER_ID, "1")
                            .header(SecurityConstants.HEADER_USER_ROLE, "1"))
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(jsonPath("$.message").value("系统预置二级菜单不可删除"));
        }

        @Test
        @DisplayName("DELETE 不存在的二级菜单返回 404")
        void shouldReturn404WhenNotFound() throws Exception {
            doThrow(new ResourceNotFoundException("二级菜单", 999L))
                    .when(subColumnService).delete(3L, 999L);

            mockMvc.perform(delete(BASE_URL + "/999")
                            .header(SecurityConstants.HEADER_USER_ID, "1")
                            .header(SecurityConstants.HEADER_USER_ROLE, "1"))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("PUT /v1/columns/{columnId}/sub-columns/sort")
    class SortEndpoint {

        @Test
        @DisplayName("4.2-INT-007: PUT API 批量排序")
        void shouldBatchSortSubColumns() throws Exception {
            doNothing().when(subColumnService).updateSort(eq(3L), any(SortSubColumnRequest.class));

            String body = """
                    {"items":[{"id":1,"sortOrder":2},{"id":2,"sortOrder":1}]}
                    """;

            mockMvc.perform(put(BASE_URL + "/sort")
                            .header(SecurityConstants.HEADER_USER_ID, "1")
                            .header(SecurityConstants.HEADER_USER_ROLE, "1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(200));
        }

        @Test
        @DisplayName("PUT sort 排序值含负数返回 400")
        void shouldReturn400WhenNegativeSortOrder() throws Exception {
            String body = """
                    {"items":[{"id":1,"sortOrder":-1}]}
                    """;

            mockMvc.perform(put(BASE_URL + "/sort")
                            .header(SecurityConstants.HEADER_USER_ID, "1")
                            .header(SecurityConstants.HEADER_USER_ROLE, "1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("PUT sort 空 items 返回 400")
        void shouldReturn400WhenEmptyItems() throws Exception {
            String body = """
                    {"items":[]}
                    """;

            mockMvc.perform(put(BASE_URL + "/sort")
                            .header(SecurityConstants.HEADER_USER_ID, "1")
                            .header(SecurityConstants.HEADER_USER_ROLE, "1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isBadRequest());
        }
    }

    // ============================================================
    // AC1: 启用开关 API (ColumnController 扩展)
    // ============================================================

    @Nested
    @DisplayName("PUT /v1/columns/{id} (subMenuEnabled)")
    class ToggleSubMenuEnabled {

        @Test
        @Disabled("此测试属于 ColumnControllerTest — ColumnController 不在本 @WebMvcTest 范围内")
        @DisplayName("4.2-INT-001: PUT API 更新 subMenuEnabled 字段")
        void shouldUpdateSubMenuEnabled() {
            // See ColumnControllerTest.Story42_SubMenuEnabled
        }
    }

    // ============================================================
    // AC4: 公开 API
    // ============================================================

    @Nested
    @DisplayName("GET /v1/portal/columns (含 subColumns)")
    class PortalColumnsEndpoint {

        @Test
        @Disabled("此测试属于 ColumnControllerTest — PortalColumnController 不在本 @WebMvcTest 范围内")
        @DisplayName("4.2-INT-002: 公开 API 仅在 sub_menu_enabled=1 时返回 subColumns")
        void shouldReturnSubColumnsOnlyWhenEnabled() {
            // See ColumnControllerTest.Story42_PortalSubColumns
        }

        @Test
        @Disabled("此测试属于 ColumnControllerTest — PortalColumnController 不在本 @WebMvcTest 范围内")
        @DisplayName("4.2-INT-012: GET /v1/portal/columns 无需认证，返回含 subColumns")
        void shouldReturnColumnsWithSubColumnsWithoutAuth() {
            // See ColumnControllerTest.Story42_PortalSubColumns
        }
    }

    // ============================================================
    // [BLIND-SPOT] BOUNDARY — Controller 层边界
    // ============================================================

    @Nested
    @DisplayName("[BLIND-SPOT] Controller 边界验证")
    class BlindSpotBoundary {

        @Test
        @DisplayName("[BLIND-SPOT] 4.2-BLIND-BOUNDARY-008: code 以数字开头应拒绝")
        void shouldRejectCodeStartingWithDigit() throws Exception {
            String body = """
                    {"name": "测试", "code": "1invalid"}
                    """;

            mockMvc.perform(post(BASE_URL)
                            .header(SecurityConstants.HEADER_USER_ID, "1")
                            .header(SecurityConstants.HEADER_USER_ROLE, "1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("[BLIND-SPOT] name 恰好 50 字符应通过")
        void shouldAcceptName50Chars() throws Exception {
            when(subColumnService.create(eq(3L), any(CreateSubColumnRequest.class)))
                    .thenReturn(1L);
            String name50 = "a".repeat(50);
            String body = String.format("""
                    {"name":"%s","code":"valid-code"}
                    """, name50);

            mockMvc.perform(post(BASE_URL)
                            .header(SecurityConstants.HEADER_USER_ID, "1")
                            .header(SecurityConstants.HEADER_USER_ROLE, "1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("[BLIND-SPOT] name 51 字符应拒绝")
        void shouldRejectName51Chars() throws Exception {
            String name51 = "a".repeat(51);
            String body = String.format("""
                    {"name":"%s","code":"valid-code"}
                    """, name51);

            mockMvc.perform(post(BASE_URL)
                            .header(SecurityConstants.HEADER_USER_ID, "1")
                            .header(SecurityConstants.HEADER_USER_ROLE, "1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isBadRequest());
        }
    }

    // ============================================================
    // [BLIND-SPOT] FLOW — 重复提交
    // ============================================================

    @Nested
    @DisplayName("[BLIND-SPOT] 流程防护")
    class BlindSpotFlow {

        @Test
        @DisplayName("[BLIND-SPOT] 4.2-BLIND-FLOW-002: 创建按钮双击（重复提交）— 第二次被唯一性校验阻止")
        void shouldHandleDuplicateSubmission() throws Exception {
            when(subColumnService.create(eq(3L), any(CreateSubColumnRequest.class)))
                    .thenReturn(1L)
                    .thenThrow(new BusinessException("该栏目下已存在同名二级菜单"));

            String body = """
                    {"name": "测试菜单", "code": "test-menu"}
                    """;

            // First request succeeds
            mockMvc.perform(post(BASE_URL)
                            .header(SecurityConstants.HEADER_USER_ID, "1")
                            .header(SecurityConstants.HEADER_USER_ROLE, "1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isOk());

            // Second request rejected by uniqueness check
            mockMvc.perform(post(BASE_URL)
                            .header(SecurityConstants.HEADER_USER_ID, "1")
                            .header(SecurityConstants.HEADER_USER_ROLE, "1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isUnprocessableEntity());
        }

        @Test
        @Disabled("级联删除由数据库 ON DELETE CASCADE 保证，需集成测试验证")
        @DisplayName("[BLIND-SPOT] 4.2-BLIND-FLOW-003: 删除父栏目后其二级菜单的级联处理")
        void shouldCascadeDeleteSubColumnsWhenParentDeleted() {
            // Database foreign key ON DELETE CASCADE handles this.
            // Requires integration test with real database.
        }
    }
}
