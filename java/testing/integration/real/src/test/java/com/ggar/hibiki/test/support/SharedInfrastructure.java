package com.ggar.hibiki.test.support;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Neo4jContainer;
import org.testcontainers.utility.DockerImageName;

public class SharedInfrastructure {

    public static final Neo4jContainer<?> neo4j = new Neo4jContainer<>("neo4j:5").withoutAuthentication();

    public static final GenericContainer<?> seaweedfs =
            new GenericContainer<>(DockerImageName.parse("chrislusf/seaweedfs:latest"))
                    .withCommand("server", "-s3")
                    .withExposedPorts(8333);

    static {
        neo4j.start();
        seaweedfs.start();
    }

    public static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.neo4j.uri", neo4j::getBoltUrl);
        registry.add(
                "storage.s3.endpoint",
                () -> String.format("http://%s:%d", seaweedfs.getHost(), seaweedfs.getMappedPort(8333)));
        registry.add("storage.s3.access-key", () -> "any");
        registry.add("storage.s3.secret-key", () -> "any");
        registry.add("storage.s3.region", () -> "us-east-1");
        registry.add("musicbrainz.api.user-agent", () -> "Hibiki/0.0.1 ( https://github.com/ggar/hibiki )");
        registry.add("acoustid.api.client-key", () -> "test-key");
    }
}
