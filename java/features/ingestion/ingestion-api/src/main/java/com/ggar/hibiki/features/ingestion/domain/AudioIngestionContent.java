package com.ggar.hibiki.features.ingestion.domain;

import java.io.InputStream;

public class AudioIngestionContent {

    private final String userId;
    private final InputStream contentStream;

    public AudioIngestionContent(String userId, InputStream contentStream) {
        this.userId = userId;
        this.contentStream = contentStream;
    }

    public String getUserId() {
        return userId;
    }

    public InputStream getContentStream() {
        return contentStream;
    }
}
