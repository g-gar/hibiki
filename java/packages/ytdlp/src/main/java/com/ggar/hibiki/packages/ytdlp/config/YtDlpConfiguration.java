package com.ggar.hibiki.packages.ytdlp.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.ggar.hibiki.packages.ytdlp.YtDlp;
import com.ggar.hibiki.packages.ytdlp.internal.DefaultYtDlp;
import com.ggar.hibiki.packages.ytdlp.internal.ProcessExecutor;
import com.ggar.hibiki.packages.ytdlp.logging.Logger;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(YtDlpProperties.class)
public class YtDlpConfiguration {

    @Bean
    public ProcessExecutor ytDlpProcessExecutor(Logger logger) {
        return new ProcessExecutor(logger);
    }

    @Bean
    public ObjectMapper ytDlpObjectMapper() {
        return new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
    }

    @Bean
    public YtDlp ytDlp(ProcessExecutor executor, YtDlpProperties properties, ObjectMapper objectMapper, Logger logger) {
        return new DefaultYtDlp(executor, properties, objectMapper, logger);
    }
}
