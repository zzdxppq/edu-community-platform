package com.educp.content;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test for Story 1.2: 后端微服务基础框架搭建 - Content Service Module
 *
 * Test Design: docs/qa/assessments/1.2-test-design-20260206.md
 */
class Story12ContentServiceTest {

    private static final Path MODULE_ROOT = resolveModuleRoot();

    private static Path resolveModuleRoot() {
        Path path = Path.of("").toAbsolutePath();
        if (path.endsWith("content-service")) {
            return path;
        }
        return path.resolve("backend/content-service");
    }

    @Nested
    @DisplayName("AC-1: content-service 启动与注册")
    class AC1_ServiceRegistration {

        @Test
        @Disabled("Integration test: requires Nacos standalone running on port 8848")
        @DisplayName("1.2-INT-003: content-service 独立启动并注册到 Nacos")
        void int003_contentServiceStartsAndRegistersToNacos() {
            // Requires Nacos infrastructure - manual verification
        }

        @Test
        @DisplayName("1.2-UNIT-001: 验证 content-service 端口配置为 8082")
        void unit001_contentServicePortIs8082() throws IOException {
            String bootstrap = Files.readString(MODULE_ROOT.resolve("src/main/resources/bootstrap.yml"));
            assertTrue(bootstrap.contains("port: 8082"),
                    "bootstrap.yml should configure server.port as 8082");
        }
    }

    @Nested
    @DisplayName("AC-3: content-service 健康检查")
    class AC3_HealthCheck {

        @Test
        @Disabled("Integration test: requires content-service running")
        @DisplayName("1.2-INT-010: content-service /actuator/health 返回 200 {status:UP}")
        void int010_contentServiceHealthEndpoint() {
            // Requires infrastructure - manual verification
        }

        @Test
        @DisplayName("1.2-INT-012: content-service 暴露 /actuator/health 和 /actuator/info")
        void int012_contentServiceExposesHealthAndInfo() throws IOException {
            String appYml = Files.readString(MODULE_ROOT.resolve("src/main/resources/application.yml"));
            assertTrue(appYml.contains("health") && appYml.contains("info"),
                    "content-service actuator should expose health and info endpoints");
        }
    }
}
