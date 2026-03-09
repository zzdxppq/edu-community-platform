package com.educp.common.result;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResultTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void success_shouldReturnCode200() {
        Result<Void> result = Result.success();
        assertEquals(200, result.getCode());
        assertEquals("success", result.getMessage());
        assertNull(result.getData());
    }

    @Test
    void successWithData_shouldReturnDataAndCode200() {
        Result<String> result = Result.success("hello");
        assertEquals(200, result.getCode());
        assertEquals("success", result.getMessage());
        assertEquals("hello", result.getData());
    }

    @Test
    void successWithMessageAndData_shouldReturnCustomMessage() {
        Result<Integer> result = Result.success("操作成功", 42);
        assertEquals(200, result.getCode());
        assertEquals("操作成功", result.getMessage());
        assertEquals(42, result.getData());
    }

    @Test
    void fail_shouldReturnErrorCodeAndMessage() {
        Result<Void> result = Result.fail("500", "内部错误");
        assertEquals(500, result.getCode());
        assertEquals("内部错误", result.getMessage());
        assertNull(result.getData());
    }

    @Test
    void failWithHttpStatus_shouldReturnCorrectStatus() {
        Result<Void> result = Result.fail(422, "BUSINESS_ERROR", "业务错误");
        assertEquals(422, result.getCode());
        assertEquals("业务错误", result.getMessage());
        assertNull(result.getData());
    }

    @Test
    void success_shouldSerializeToJson() throws Exception {
        Result<String> result = Result.success("data");
        String json = objectMapper.writeValueAsString(result);
        assertTrue(json.contains("\"code\":200"));
        assertTrue(json.contains("\"message\":\"success\""));
        assertTrue(json.contains("\"data\":\"data\""));
    }

    @Test
    void successWithNullData_shouldSerializeDataAsNull() throws Exception {
        Result<Void> result = Result.success();
        String json = objectMapper.writeValueAsString(result);
        assertTrue(json.contains("\"code\":200"));
        assertTrue(json.contains("\"data\":null"));
    }
}
