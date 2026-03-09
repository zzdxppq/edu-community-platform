package com.educp.core.dto;

import com.educp.core.dto.request.CreateAdminUserRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CreateAdminUserRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void shouldPass_whenValidPhoneAndRole() {
        CreateAdminUserRequest request = new CreateAdminUserRequest();
        request.setUsername("13800138000");
        request.setRole(1);

        Set<ConstraintViolation<CreateAdminUserRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldFail_whenUsernameNotMatchPhonePattern() {
        CreateAdminUserRequest request = new CreateAdminUserRequest();
        request.setUsername("1380013");
        request.setRole(1);

        Set<ConstraintViolation<CreateAdminUserRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("11位手机号")));
    }

    @Test
    void shouldFail_whenUsernameStartsWith2() {
        CreateAdminUserRequest request = new CreateAdminUserRequest();
        request.setUsername("23800138000");
        request.setRole(1);

        Set<ConstraintViolation<CreateAdminUserRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldFail_whenUsernameIsBlank() {
        CreateAdminUserRequest request = new CreateAdminUserRequest();
        request.setUsername("");
        request.setRole(1);

        Set<ConstraintViolation<CreateAdminUserRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldFail_whenRoleIsNull() {
        CreateAdminUserRequest request = new CreateAdminUserRequest();
        request.setUsername("13800138000");
        request.setRole(null);

        Set<ConstraintViolation<CreateAdminUserRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("角色不能为空")));
    }

    @Test
    void shouldPass_whenValidPhoneFormats() {
        String[] validPhones = {"13800138000", "15912345678", "18699998888", "19900001111"};
        for (String phone : validPhones) {
            CreateAdminUserRequest request = new CreateAdminUserRequest();
            request.setUsername(phone);
            request.setRole(1);
            Set<ConstraintViolation<CreateAdminUserRequest>> violations = validator.validate(request);
            assertTrue(violations.isEmpty(), "Should pass for " + phone);
        }
    }

    @Test
    void shouldFail_whenInvalidPhoneFormats() {
        String[] invalidPhones = {"12345678901", "1380013800", "138001380001", "abc12345678"};
        for (String phone : invalidPhones) {
            CreateAdminUserRequest request = new CreateAdminUserRequest();
            request.setUsername(phone);
            request.setRole(1);
            Set<ConstraintViolation<CreateAdminUserRequest>> violations = validator.validate(request);
            assertFalse(violations.isEmpty(), "Should fail for " + phone);
        }
    }
}
