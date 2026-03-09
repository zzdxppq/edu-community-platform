package com.educp.core.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.educp.common.exception.BusinessException;
import com.educp.common.exception.ResourceNotFoundException;
import com.educp.common.result.PageResult;
import com.educp.core.converter.AdminUserConverter;
import com.educp.core.dto.request.AdminUserQueryRequest;
import com.educp.core.dto.request.CreateAdminUserRequest;
import com.educp.core.dto.request.UpdateAdminUserRequest;
import com.educp.core.dto.response.AdminUserDetailResponse;
import com.educp.core.dto.response.AdminUserListResponse;
import com.educp.core.entity.AdminUser;
import com.educp.core.mapper.AdminUserMapper;
import com.educp.core.service.impl.AdminUserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminUserServiceImplTest {

    @Mock
    private AdminUserMapper adminUserMapper;

    @Mock
    private AdminUserConverter adminUserConverter;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AdminUserServiceImpl adminUserService;

    private CreateAdminUserRequest createRequest;
    private AdminUser adminUser;
    private AdminUserDetailResponse detailResponse;

    @BeforeEach
    void setUp() {
        createRequest = new CreateAdminUserRequest();
        createRequest.setUsername("13800138000");
        createRequest.setRole(1);

        adminUser = new AdminUser();
        adminUser.setId(1L);
        adminUser.setUsername("13800138000");
        adminUser.setRole(1);
        adminUser.setStatus(1);
        adminUser.setIsFirstLogin(1);

        detailResponse = new AdminUserDetailResponse();
        detailResponse.setId(1L);
        detailResponse.setUsername("13800138000");
        detailResponse.setRole(1);
        detailResponse.setIsFirstLogin(1);
    }

    // === create() tests ===

    @Test
    void create_shouldSucceed_whenValidRequest() {
        when(adminUserMapper.existsByUsername("13800138000")).thenReturn(false);
        when(adminUserConverter.toEntity(createRequest)).thenReturn(new AdminUser());
        when(passwordEncoder.encode("138000")).thenReturn("$2a$10$encodedPassword");
        when(adminUserMapper.insert(any(AdminUser.class))).thenReturn(1);

        Long id = adminUserService.create(createRequest);

        verify(adminUserMapper).existsByUsername("13800138000");
        verify(passwordEncoder).encode("138000");
        verify(adminUserMapper).insert(any(AdminUser.class));
    }

    @Test
    void create_shouldThrowBusinessException_whenUsernameExists() {
        when(adminUserMapper.existsByUsername("13800138000")).thenReturn(true);

        BusinessException ex = assertThrows(BusinessException.class,
                () -> adminUserService.create(createRequest));
        assertEquals("该手机号已被注册", ex.getMessage());
    }

    @Test
    void create_shouldThrowBusinessException_whenSchoolAdminWithoutSchoolId() {
        createRequest.setRole(2);
        createRequest.setSchoolId(null);
        when(adminUserMapper.existsByUsername("13800138000")).thenReturn(false);

        BusinessException ex = assertThrows(BusinessException.class,
                () -> adminUserService.create(createRequest));
        assertEquals("学校管理员必须指定学校", ex.getMessage());
    }

    @Test
    void create_shouldThrowBusinessException_whenSchoolIdNotFound() {
        createRequest.setRole(2);
        createRequest.setSchoolId(999L);
        when(adminUserMapper.existsByUsername("13800138000")).thenReturn(false);
        when(adminUserMapper.existsSchoolById(999L)).thenReturn(false);

        BusinessException ex = assertThrows(BusinessException.class,
                () -> adminUserService.create(createRequest));
        assertEquals("指定的学校不存在", ex.getMessage());
    }

    @Test
    void create_shouldSetDefaultPassword_asLast6DigitsOfPhone() {
        when(adminUserMapper.existsByUsername("13800138000")).thenReturn(false);
        AdminUser entity = new AdminUser();
        when(adminUserConverter.toEntity(createRequest)).thenReturn(entity);
        when(passwordEncoder.encode("138000")).thenReturn("$2a$10$encoded");
        when(adminUserMapper.insert(any())).thenReturn(1);

        adminUserService.create(createRequest);

        verify(passwordEncoder).encode("138000");
        assertEquals("$2a$10$encoded", entity.getPassword());
    }

    @Test
    void create_shouldSetBcryptPassword() {
        when(adminUserMapper.existsByUsername("13800138000")).thenReturn(false);
        AdminUser entity = new AdminUser();
        when(adminUserConverter.toEntity(createRequest)).thenReturn(entity);
        when(passwordEncoder.encode("138000")).thenReturn("$2a$10$bcryptHash");
        when(adminUserMapper.insert(any())).thenReturn(1);

        adminUserService.create(createRequest);

        assertTrue(entity.getPassword().startsWith("$2a$"));
    }

    @Test
    void create_shouldSetIsFirstLoginToOne() {
        when(adminUserMapper.existsByUsername("13800138000")).thenReturn(false);
        AdminUser entity = new AdminUser();
        when(adminUserConverter.toEntity(createRequest)).thenReturn(entity);
        when(passwordEncoder.encode(anyString())).thenReturn("$2a$10$encoded");
        when(adminUserMapper.insert(any())).thenReturn(1);

        adminUserService.create(createRequest);

        assertEquals(1, entity.getIsFirstLogin());
    }

    @Test
    void create_shouldGenerateSalt32Chars() {
        when(adminUserMapper.existsByUsername("13800138000")).thenReturn(false);
        AdminUser entity = new AdminUser();
        when(adminUserConverter.toEntity(createRequest)).thenReturn(entity);
        when(passwordEncoder.encode(anyString())).thenReturn("$2a$10$encoded");
        when(adminUserMapper.insert(any())).thenReturn(1);

        adminUserService.create(createRequest);

        assertNotNull(entity.getSalt());
        assertEquals(32, entity.getSalt().length());
    }

    @Test
    void create_shouldSucceed_whenSchoolAdminWithValidSchoolId() {
        createRequest.setRole(2);
        createRequest.setSchoolId(1L);
        when(adminUserMapper.existsByUsername("13800138000")).thenReturn(false);
        when(adminUserMapper.existsSchoolById(1L)).thenReturn(true);
        AdminUser entity = new AdminUser();
        when(adminUserConverter.toEntity(createRequest)).thenReturn(entity);
        when(passwordEncoder.encode(anyString())).thenReturn("$2a$10$encoded");
        when(adminUserMapper.insert(any())).thenReturn(1);

        assertDoesNotThrow(() -> adminUserService.create(createRequest));
    }

    // === getById() tests ===

    @Test
    void getById_shouldReturnDetail_whenExists() {
        when(adminUserMapper.selectById(1L)).thenReturn(adminUser);
        when(adminUserConverter.toDetailResponse(adminUser)).thenReturn(detailResponse);

        AdminUserDetailResponse result = adminUserService.getById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("13800138000", result.getUsername());
    }

    @Test
    void getById_shouldThrowResourceNotFoundException_whenNotExists() {
        when(adminUserMapper.selectById(999L)).thenReturn(null);

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> adminUserService.getById(999L));
        assertEquals("管理员不存在: 999", ex.getMessage());
    }

    // === page() tests ===

    @Test
    @SuppressWarnings("unchecked")
    void page_shouldReturnPageResult() {
        AdminUserQueryRequest queryRequest = new AdminUserQueryRequest();
        queryRequest.setPage(1);
        queryRequest.setSize(10);

        Page<AdminUser> mockPage = new Page<>(1, 10);
        mockPage.setRecords(List.of(adminUser));
        mockPage.setTotal(1);

        AdminUserListResponse listResponse = new AdminUserListResponse();
        listResponse.setId(1L);

        when(adminUserMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class))).thenReturn(mockPage);
        when(adminUserConverter.toListResponse(anyList())).thenReturn(List.of(listResponse));

        PageResult<AdminUserListResponse> result = adminUserService.page(queryRequest);

        assertNotNull(result);
        assertEquals(1, result.getTotal());
        assertEquals(1, result.getList().size());
    }

    @Test
    @SuppressWarnings("unchecked")
    void page_shouldReturnEmpty_whenNoRecords() {
        AdminUserQueryRequest queryRequest = new AdminUserQueryRequest();

        Page<AdminUser> mockPage = new Page<>(1, 10);
        mockPage.setRecords(Collections.emptyList());
        mockPage.setTotal(0);

        when(adminUserMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class))).thenReturn(mockPage);
        when(adminUserConverter.toListResponse(anyList())).thenReturn(Collections.emptyList());

        PageResult<AdminUserListResponse> result = adminUserService.page(queryRequest);

        assertEquals(0, result.getTotal());
        assertTrue(result.getList().isEmpty());
    }

    // === update() tests ===

    @Test
    void update_shouldSucceed_whenExists() {
        UpdateAdminUserRequest updateRequest = new UpdateAdminUserRequest();
        updateRequest.setPhone("13900139000");
        updateRequest.setStatus(0);

        when(adminUserMapper.selectById(1L)).thenReturn(adminUser);
        when(adminUserMapper.updateById(any())).thenReturn(1);

        assertDoesNotThrow(() -> adminUserService.update(1L, updateRequest));

        assertEquals("13900139000", adminUser.getPhone());
        assertEquals(0, adminUser.getStatus());
    }

    @Test
    void update_shouldThrowResourceNotFoundException_whenNotExists() {
        when(adminUserMapper.selectById(999L)).thenReturn(null);

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> adminUserService.update(999L, new UpdateAdminUserRequest()));
        assertEquals("管理员不存在: 999", ex.getMessage());
    }

    // === delete() tests ===

    @Test
    void delete_shouldSucceed_whenExists() {
        when(adminUserMapper.selectById(1L)).thenReturn(adminUser);
        when(adminUserMapper.deleteById(1L)).thenReturn(1);

        assertDoesNotThrow(() -> adminUserService.delete(1L));
        verify(adminUserMapper).deleteById(1L);
    }

    @Test
    void delete_shouldThrowResourceNotFoundException_whenNotExists() {
        when(adminUserMapper.selectById(999L)).thenReturn(null);

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> adminUserService.delete(999L));
        assertEquals("管理员不存在: 999", ex.getMessage());
    }
}
