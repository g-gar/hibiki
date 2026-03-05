package com.ggar.hibiki.packages.id3v2;

import com.ggar.hibiki.packages.id3v2.model.Id3v2Tag;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import org.springframework.core.io.buffer.DataBuffer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * The main interface for interacting with ID3v2.3.0 tags.
 */
public interface Id3v2 {

    /**
     * Reads an ID3v2 tag from a file path.
     *
     * @param filePath the path to the file
     * @return a {@link Mono} emitting the deserialized ID3v2 tag
     */
    Mono<Id3v2Tag> readTag(Path filePath);

    /**
     * Reads an ID3v2 tag from an input stream.
     *
     * @param inputStream the input stream
     * @return a {@link Mono} emitting the deserialized ID3v2 tag
     */
    Mono<Id3v2Tag> readTag(InputStream inputStream);

    /**
     * Reads an ID3v2 tag from a reactive stream of data buffers.
     *
     * @param data the flux of data buffers
     * @return a {@link Mono} emitting the deserialized ID3v2 tag
     */
    Mono<Id3v2Tag> readTag(Flux<DataBuffer> data);

    /**
     * Writes an ID3v2 tag to a file path.
     *
     * @param filePath the path to the file
     * @param tag      the ID3v2 tag to write
     * @return a {@link Mono} indicating completion
     */
    Mono<Void> writeTag(Path filePath, Id3v2Tag tag);

    /**
     * Writes an ID3v2 tag to an output stream.
     *
     * @param outputStream the output stream
     * @param tag          the ID3v2 tag to write
     * @return a {@link Mono} indicating completion
     */
    Mono<Void> writeTag(OutputStream outputStream, Id3v2Tag tag);
}
