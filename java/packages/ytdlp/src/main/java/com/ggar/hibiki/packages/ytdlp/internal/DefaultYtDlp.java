package com.ggar.hibiki.packages.ytdlp.internal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ggar.hibiki.packages.ytdlp.YtDlp;
import com.ggar.hibiki.packages.ytdlp.builder.YtDlpDownload;
import com.ggar.hibiki.packages.ytdlp.builder.YtDlpRequest;
import com.ggar.hibiki.packages.ytdlp.config.YtDlpProperties;
import com.ggar.hibiki.packages.ytdlp.logging.Logger;

public class DefaultYtDlp implements YtDlp {

    private final ProcessExecutor processExecutor;
    private final YtDlpProperties properties;
    private final ObjectMapper objectMapper;
    private final Logger logger;

    public DefaultYtDlp(
            ProcessExecutor processExecutor, YtDlpProperties properties, ObjectMapper objectMapper, Logger logger) {
        this.processExecutor = processExecutor;
        this.properties = properties;
        this.objectMapper = objectMapper;
        this.logger = logger;
    }

    @Override
    public YtDlpRequest request(String urlOrSearch) {
        return new DefaultYtDlpRequest(urlOrSearch, processExecutor, properties, objectMapper, logger);
    }

    @Override
    public YtDlpDownload download(String url) {
        return new DefaultYtDlpDownload(url, processExecutor, properties, logger);
    }
}
