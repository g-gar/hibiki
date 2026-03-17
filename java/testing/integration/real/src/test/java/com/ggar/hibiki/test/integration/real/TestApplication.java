package com.ggar.hibiki.test.integration.real;

import com.ggar.hibiki.core.shared.config.SharedFrameworkConfiguration;
import com.ggar.hibiki.features.ingestion.infrastructure.config.S3Config;
import com.ggar.hibiki.packages.jwt.config.JwtConfiguration;
import com.ggar.hibiki.test.integration.real.config.Neo4jTestSharedConfiguration;
import com.ggar.hibiki.test.integration.real.config.SharedTestFrameworkConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication(scanBasePackages = "com.ggar.hibiki")
@Import({
    SharedFrameworkConfiguration.class,
    Neo4jTestSharedConfiguration.class,
    SharedTestFrameworkConfiguration.class,
    JwtConfiguration.class,
    S3Config.class
})
public class TestApplication {}
