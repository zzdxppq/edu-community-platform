package com.educp.core;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test for Story 1.2: 后端微服务基础框架搭建 - Core Service Module
 *
 * Test Design: docs/qa/assessments/1.2-test-design-20260206.md
 */
class Story12CoreServiceTest {

    private static final Path MODULE_ROOT = resolveModuleRoot();

    private static Path resolveModuleRoot() {
        Path path = Path.of("").toAbsolutePath();
        if (path.endsWith("core-service")) {
            return path;
        }
        return path.resolve("backend/core-service");
    }

    // ============================================================
    // AC-1: core-service 启动与注册
    // ============================================================

    @Nested
    @DisplayName("AC-1: core-service 启动与注册")
    class AC1_ServiceRegistration {

        @Test
        @Disabled("Integration test: requires Nacos standalone running on port 8848")
        @DisplayName("1.2-INT-002: core-service 独立启动并注册到 Nacos")
        void int002_coreServiceStartsAndRegistersToNacos() {
            // Requires Nacos infrastructure - manual verification
        }

        @Test
        @DisplayName("1.2-UNIT-001: 验证 core-service 端口配置为 8081")
        void unit001_coreServicePortIs8081() throws IOException {
            String bootstrap = Files.readString(MODULE_ROOT.resolve("src/main/resources/bootstrap.yml"));
            assertTrue(bootstrap.contains("port: 8081"),
                    "bootstrap.yml should configure server.port as 8081");
        }

        @Test
        @DisplayName("1.2-UNIT-002: 验证 core-service bootstrap.yml 包含 Nacos 配置")
        void unit002_coreServiceBootstrapHasNacosConfig() throws IOException {
            String bootstrap = Files.readString(MODULE_ROOT.resolve("src/main/resources/bootstrap.yml"));
            assertTrue(bootstrap.contains("nacos"),
                    "bootstrap.yml should contain nacos configuration");
            assertTrue(bootstrap.contains("server-addr"),
                    "bootstrap.yml should contain nacos server-addr");
        }
    }

    // ============================================================
    // AC-3: core-service 健康检查
    // ============================================================

    @Nested
    @DisplayName("AC-3: core-service 健康检查")
    class AC3_HealthCheck {

        @Test
        @Disabled("Integration test: requires core-service running")
        @DisplayName("1.2-INT-010: core-service /actuator/health 返回 200 {status:UP}")
        void int010_coreServiceHealthEndpoint() {
            // Requires infrastructure - manual verification
        }

        @Test
        @DisplayName("1.2-INT-012: core-service 暴露 /actuator/health 和 /actuator/info")
        void int012_coreServiceExposesHealthAndInfo() throws IOException {
            String appYml = Files.readString(MODULE_ROOT.resolve("src/main/resources/application.yml"));
            assertTrue(appYml.contains("health") && appYml.contains("info"),
                    "core-service actuator should expose health and info endpoints");
        }
    }
}
