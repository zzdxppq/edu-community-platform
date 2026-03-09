package com.educp.core.controller;

import com.educp.common.result.PageResult;
import com.educp.core.dto.response.AdminUserDetailResponse;
import com.educp.core.dto.response.AdminUserListResponse;
import com.educp.core.service.AdminUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.bean.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminUserController.class)
class AdminUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AdminUserService adminUserService;

    @Test
    void create_shouldReturn200_whenValidRequest() throws Exception {
        when(adminUserService.create(any())).thenReturn(1L);

        mockMvc.perform(post("/v1/admin-users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"13800138000\",\"role\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value(1));
    }

    @Test
    void create_shouldReturn400_whenInvalidUsername() throws Exception {
        mockMvc.perform(post("/v1/admin-users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"1380013\",\"role\":1}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_shouldReturn400_whenMissingRole() throws Exception {
        mockMvc.perform(post("/v1/admin-users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"13800138000\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getById_shouldReturn200_whenExists() throws Exception {
        AdminUserDetailResponse detail = new AdminUserDetailResponse();
        detail.setId(1L);
        detail.setUsername("13800138000");
        detail.setRole(1);
        detail.setIsFirstLogin(1);

        when(adminUserService.getById(1L)).thenReturn(detail);

        mockMvc.perform(get("/v1/admin-users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.username").value("13800138000"))
                .andExpect(jsonPath("$.data.isFirstLogin").value(1));
    }

    @Test
    void page_shouldReturn200() throws Exception {
        AdminUserListResponse listItem = new AdminUserListResponse();
        listItem.setId(1L);
        listItem.setUsername("13800138000");

        PageResult<AdminUserListResponse> pageResult = PageResult.of(
                List.of(listItem), 1L, 1, 10);

        when(adminUserService.page(any())).thenReturn(pageResult);

        mockMvc.perform(get("/v1/admin-users")
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.total").value(1))
                .andExpect(jsonPath("$.data.list[0].id").value(1));
    }

    @Test
    void update_shouldReturn200() throws Exception {
        doNothing().when(adminUserService).update(eq(1L), any());

        mockMvc.perform(put("/v1/admin-users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"phone\":\"13900139000\",\"status\":0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    void delete_shouldReturn200() throws Exception {
        doNothing().when(adminUserService).delete(1L);

        mockMvc.perform(delete("/v1/admin-users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    void getById_shouldReturnResponse_withoutPasswordAndSaltFields() throws Exception {
        AdminUserDetailResponse detail = new AdminUserDetailResponse();
        detail.setId(1L);
        detail.setUsername("13800138000");

        when(adminUserService.getById(1L)).thenReturn(detail);

        mockMvc.perform(get("/v1/admin-users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.password").doesNotExist())
                .andExpect(jsonPath("$.data.salt").doesNotExist());
    }
}
