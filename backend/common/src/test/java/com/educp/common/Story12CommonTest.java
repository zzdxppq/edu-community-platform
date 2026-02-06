package com.educp.common;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test for Story 1.2: 后端微服务基础框架搭建 - Common Module & Cross-Module
 *
 * Test Design: docs/qa/assessments/1.2-test-design-20260206.md
 */
class Story12CommonTest {

    private static final Path BACKEND_ROOT = resolveBackendRoot();

    private static Path resolveBackendRoot() {
        Path path = Path.of("").toAbsolutePath();
        if (path.endsWith("common")) {
            return path.getParent();
        }
        return path.resolve("backend");
    }

    // ============================================================
    // AC-1: 跨服务注册验证
    // ============================================================

    @Nested
    @DisplayName("AC-1: 全服务注册完整性")
    class AC1_CrossServiceRegistration {

        @Test
        @Disabled("Integration test: requires all 4 services running with Nacos")
        @DisplayName("1.2-INT-005: Nacos 服务列表显示4个服务，实例数 >= 1")
        void int005_allFourServicesRegisteredInNacos() {
            // Requires Nacos infrastructure - manual verification
        }

        @Test
        @DisplayName("1.2-UNIT-003: 四个服务名称在 bootstrap.yml 中唯一")
        void unit003_serviceNamesAreUnique() throws IOException {
            List<String> modules = List.of("gateway", "core-service", "content-service", "file-service");
            Set<String> serviceNames = new HashSet<>();

            for (String module : modules) {
                String bootstrap = Files.readString(
                        BACKEND_ROOT.resolve(module + "/src/main/resources/bootstrap.yml"));
                // Extract spring.application.name value
                for (String line : bootstrap.split("\n")) {
                    if (line.trim().startsWith("name:")) {
                        String name = line.trim().substring("name:".length()).trim();
                        assertTrue(serviceNames.add(name),
                                "Duplicate service name found: " + name);
                    }
                }
            }

            assertEquals(4, serviceNames.size(),
                    "Expected 4 unique service names, found: " + serviceNames);
        }
    }

    // ============================================================
    // AC-4: Maven 父工程管理统一依赖版本
    // ============================================================

    @Nested
    @DisplayName("AC-4: 依赖版本统一管理")
    class AC4_DependencyManagement {

        @Test
        @DisplayName("1.2-UNIT-006: 父 POM dependencyManagement 包含所有要求的依赖版本")
        void unit006_parentPomHasAllRequiredVersions() throws Exception {
            String parentPom = Files.readString(BACKEND_ROOT.resolve("pom.xml"));

            // Check for all required managed dependency versions
            assertTrue(parentPom.contains("spring-cloud-dependencies"),
                    "Parent POM should manage Spring Cloud version");
            assertTrue(parentPom.contains("spring-cloud-alibaba-dependencies"),
                    "Parent POM should manage Spring Cloud Alibaba version");
            assertTrue(parentPom.contains("mybatis-plus"),
                    "Parent POM should manage MyBatis-Plus version");
            assertTrue(parentPom.contains("hutool"),
                    "Parent POM should manage Hutool version");
            assertTrue(parentPom.contains("mapstruct"),
                    "Parent POM should manage MapStruct version");
            assertTrue(parentPom.contains("springdoc"),
                    "Parent POM should manage SpringDoc version");
        }

        @Test
        @DisplayName("1.2-UNIT-007: 子模块 POM 未硬编码依赖版本号")
        void unit007_subModulesNoHardcodedVersions() throws Exception {
            List<String> modules = List.of("gateway", "core-service", "content-service", "file-service");

            for (String module : modules) {
                Document doc = DocumentBuilderFactory.newInstance()
                        .newDocumentBuilder()
                        .parse(BACKEND_ROOT.resolve(module + "/pom.xml").toFile());

                NodeList deps = doc.getElementsByTagName("dependency");
                for (int i = 0; i < deps.getLength(); i++) {
                    Element dep = (Element) deps.item(i);
                    NodeList versions = dep.getElementsByTagName("version");
                    if (versions.getLength() > 0) {
                        String version = versions.item(0).getTextContent();
                        // Allow ${} property references
                        if (!version.startsWith("${")) {
                            fail(module + "/pom.xml has hardcoded version: " + version +
                                    " in dependency " + dep.getElementsByTagName("artifactId")
                                    .item(0).getTextContent());
                        }
                    }
                }
            }
        }

        @Test
        @DisplayName("1.2-UNIT-008: common 模块被三个业务服务正确引用")
        void unit008_commonReferencedByAllServices() throws Exception {
            List<String> services = List.of("core-service", "content-service", "file-service");

            for (String service : services) {
                String pom = Files.readString(BACKEND_ROOT.resolve(service + "/pom.xml"));
                assertTrue(pom.contains("<artifactId>common</artifactId>"),
                        service + "/pom.xml should reference common module");
                assertTrue(pom.contains("<groupId>com.educp</groupId>"),
                        service + "/pom.xml common dependency should use com.educp groupId");
            }
        }
    }

    // ============================================================
    // AC-5: JDK 21 编译运行正常
    // ============================================================

    @Nested
    @DisplayName("AC-5: JDK 21 兼容性")
    class AC5_JdkCompatibility {

        @Test
        @Disabled("Integration test: requires Maven installed - run 'mvn clean compile -f backend/pom.xml'")
        @DisplayName("1.2-INT-013: mvn clean compile 全量编译成功")
        void int013_mavenCompileSucceeds() {
            // Requires Maven - manual verification
        }

        @Test
        @DisplayName("1.2-UNIT-009: 父 POM java.version=21 配置正确")
        void unit009_parentPomJavaVersion21() throws IOException {
            String parentPom = Files.readString(BACKEND_ROOT.resolve("pom.xml"));
            assertTrue(parentPom.contains("<java.version>21</java.version>"),
                    "Parent POM should set java.version to 21");
        }
    }

    // ============================================================
    // Blind Spot: 无效配置启动
    // ============================================================

    @Nested
    @DisplayName("Blind Spot: 配置错误处理")
    class BlindSpot_Config {

        @Test
        @Disabled("Integration test: requires modifying bootstrap.yml and restarting service")
        @DisplayName("[BLIND-SPOT] 1.2-BLIND-ERROR-003: 无效 bootstrap.yml 配置启动行为")
        void blindError003_invalidBootstrapConfig() {
            // Requires infrastructure - manual verification
        }
    }
}
