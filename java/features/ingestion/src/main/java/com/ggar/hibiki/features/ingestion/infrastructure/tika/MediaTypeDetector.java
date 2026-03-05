package com.ggar.hibiki.features.ingestion.infrastructure.tika;

import java.io.InputStream;
import org.apache.tika.Tika;

/**
 * Wrapper for Apache Tika's media type detection capabilities.
 */
public class MediaTypeDetector {

    private final Tika tika;

    public MediaTypeDetector() {
        this.tika = new Tika();
    }

    public String detect(InputStream inputStream, String filename) {
        try {
            return tika.detect(inputStream, filename);
        } catch (Exception e) {
            return "application/octet-stream";
        }
    }

    public String detect(byte[] data, String filename) {
        try {
            return tika.detect(data, filename);
        } catch (Exception e) {
            return "application/octet-stream";
        }
    }
}
