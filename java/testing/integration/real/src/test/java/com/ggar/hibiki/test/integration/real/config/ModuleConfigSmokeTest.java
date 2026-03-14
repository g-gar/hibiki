package com.ggar.hibiki.test.integration.real.config;

import static org.assertj.core.api.Assertions.assertThat;

import com.ggar.hibiki.core.catalog.CatalogModuleConfig;
import com.ggar.hibiki.core.identity.IdentityModuleConfig;
import com.ggar.hibiki.features.ingestion.IngestionModuleConfig;
import com.ggar.hibiki.features.library.LibraryModuleConfig;
import com.ggar.hibiki.test.integration.real.TestApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Neo4jContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest(classes = TestApplication.class)
@Testcontainers
class ModuleConfigSmokeTest {

    @Container
    static Neo4jContainer<?> neo4j = new Neo4jContainer<>("neo4j:5").withoutAuthentication();

    @Container
    static GenericContainer<?> seaweedfs = new GenericContainer<>(DockerImageName.parse("chrislusf/seaweedfs:latest"))
            .withCommand("server", "-s3")
            .withExposedPorts(8333)
            .waitingFor(Wait.forLogMessage(".*Start Seaweed S3 API Server.*", 1));

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.neo4j.uri", neo4j::getBoltUrl);

        String s3Endpoint = String.format("http://%s:%d", seaweedfs.getHost(), seaweedfs.getMappedPort(8333));
        registry.add("storage.s3.endpoint", () -> s3Endpoint);
        registry.add("storage.s3.access-key", () -> "any");
        registry.add("storage.s3.secret-key", () -> "any");
        registry.add("storage.s3.region", () -> "us-east-1");
        registry.add("storage.s3.bucket-name", () -> "hibiki-media");

        registry.add("musicbrainz.api.user-agent", () -> "HibikiSmokeTest/1.0.0 (https://github.com/hibiki)");
        registry.add("acoustid.api.client-key", () -> "dummy-key");
    }

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void contextLoads() {
        assertThat(applicationContext).isNotNull();
    }

    @Test
    void moduleConfigsAreLoaded() {
        assertThat(applicationContext.containsBean("catalogModuleConfig")).isTrue();
        assertThat(applicationContext.containsBean("libraryModuleConfig")).isTrue();
        assertThat(applicationContext.containsBean("identityModuleConfig")).isTrue();
        assertThat(applicationContext.containsBean("ingestionModuleConfig")).isTrue();

        assertThat(applicationContext.getBean(CatalogModuleConfig.class)).isNotNull();
        assertThat(applicationContext.getBean(LibraryModuleConfig.class)).isNotNull();
        assertThat(applicationContext.getBean(IdentityModuleConfig.class)).isNotNull();
        assertThat(applicationContext.getBean(IngestionModuleConfig.class)).isNotNull();
    }
}
