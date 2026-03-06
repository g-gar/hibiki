package com.ggar.hibiki.packages.ytdlp.internal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ggar.hibiki.packages.ytdlp.YtDlp;
import com.ggar.hibiki.packages.ytdlp.builder.YtDlpDownload;
import com.ggar.hibiki.packages.ytdlp.builder.YtDlpRequest;
import com.ggar.hibiki.packages.ytdlp.config.YtDlpProperties;

public class DefaultYtDlp implements YtDlp {

    private final ProcessExecutor processExecutor;
    private final YtDlpProperties properties;
    private final ObjectMapper objectMapper;

    public DefaultYtDlp(ProcessExecutor processExecutor, YtDlpProperties properties, ObjectMapper objectMapper) {
        this.processExecutor = processExecutor;
        this.properties = properties;
        this.objectMapper = objectMapper;
    }

    @Override
    public YtDlpRequest request(String urlOrSearch) {
        return new DefaultYtDlpRequest(urlOrSearch, processExecutor, properties, objectMapper);
    }

    @Override
    public YtDlpDownload download(String url) {
        return new DefaultYtDlpDownload(url, processExecutor, properties);
    }
}
