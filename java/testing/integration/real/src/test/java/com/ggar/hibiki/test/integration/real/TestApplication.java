package com.ggar.hibiki.test.integration.real;

import com.ggar.hibiki.core.shared.config.SharedFrameworkConfiguration;
import com.ggar.hibiki.features.ingestion.infrastructure.config.S3Config;
import com.ggar.hibiki.packages.jwt.config.JwtConfiguration;
import com.ggar.hibiki.test.integration.real.config.Neo4jTestSharedConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.data.neo4j.autoconfigure.DataNeo4jAutoConfiguration;
import org.springframework.boot.data.neo4j.autoconfigure.DataNeo4jReactiveAutoConfiguration;
import org.springframework.context.annotation.Import;

@SpringBootApplication(
        scanBasePackages = "com.ggar.hibiki",
        exclude = {DataNeo4jAutoConfiguration.class, DataNeo4jReactiveAutoConfiguration.class})
@Import({SharedFrameworkConfiguration.class, Neo4jTestSharedConfiguration.class, JwtConfiguration.class, S3Config.class
})
public class TestApplication {}
