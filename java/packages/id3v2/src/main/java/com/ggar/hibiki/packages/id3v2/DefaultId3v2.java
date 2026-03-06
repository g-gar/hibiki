package com.ggar.hibiki.packages.id3v2;

import com.ggar.hibiki.packages.id3v2.internal.Id3v2Reader;
import com.ggar.hibiki.packages.id3v2.internal.Id3v2Writer;
import com.ggar.hibiki.packages.id3v2.model.Id3v2Tag;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import org.springframework.core.io.buffer.DataBuffer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Default implementation of the {@link Id3v2} API.
 * Uses reactive file operations and stream support.
 */
public class DefaultId3v2 implements Id3v2 {

    private final Id3v2Reader reader;
    private final Id3v2Writer writer;

    public DefaultId3v2() {
        this.reader = new Id3v2Reader();
        this.writer = new Id3v2Writer();
    }

    @Override
    public Mono<Id3v2Tag> readTag(Path filePath) {
        return reader.readTag(filePath);
    }

    @Override
    public Mono<Id3v2Tag> readTag(InputStream inputStream) {
        return reader.readTag(inputStream);
    }

    @Override
    public Mono<Id3v2Tag> readTag(Flux<DataBuffer> data) {
        return reader.readTag(data);
    }

    @Override
    public Mono<Void> writeTag(Path filePath, Id3v2Tag tag) {
        return writer.writeTag(filePath, tag);
    }

    @Override
    public Mono<Void> writeTag(OutputStream outputStream, Id3v2Tag tag) {
        return writer.writeTag(outputStream, tag);
    }
}
