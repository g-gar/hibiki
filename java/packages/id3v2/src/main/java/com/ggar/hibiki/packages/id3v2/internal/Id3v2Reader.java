package com.ggar.hibiki.packages.id3v2.internal;

import com.ggar.hibiki.packages.id3v2.model.Id3v2Frame;
import com.ggar.hibiki.packages.id3v2.model.Id3v2Tag;
import com.ggar.hibiki.packages.id3v2.logging.Logger;
import com.ggar.hibiki.packages.id3v2.logging.NoOpLogger;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

public class Id3v2Reader {

    private static final byte[] ID3_IDENTIFIER = { 'I', 'D', '3' };
    private static final int HEADER_SIZE = 10;
    private static final int FRAME_HEADER_SIZE = 10;

    private final Logger logger;

    public Id3v2Reader() {
        this(NoOpLogger.getInstance());
    }

    public Id3v2Reader(Logger logger) {
        this.logger = logger;
    }

    public Mono<Id3v2Tag> readTag(Path filePath) {
        logger.debug("Reading ID3 Tag from File path: {}", filePath);
        return Mono.using(
                () -> AsynchronousFileChannel.open(filePath, StandardOpenOption.READ),
                channel -> parseTagFromBuffer(DataBufferUtils
                        .readAsynchronousFileChannel(() -> channel, 0, new DefaultDataBufferFactory(), 4096)),
                channel -> {
                    try {
                        channel.close();
                    } catch (IOException e) {
                        // Ignore close errors
                    }
                });
    }

    public Mono<Id3v2Tag> readTag(InputStream inputStream) {
        logger.debug("Reading ID3 Tag from InputStream");
        return parseTagFromBuffer(
                DataBufferUtils.readInputStream(() -> inputStream, new DefaultDataBufferFactory(), 4096));
    }

    public Mono<Id3v2Tag> readTag(Flux<DataBuffer> data) {
        logger.debug("Reading ID3 Tag from reactive DataBuffer stream");
        return parseTagFromBuffer(data);
    }

    private Mono<Id3v2Tag> parseTagFromBuffer(Flux<DataBuffer> buffers) {
        return DataBufferUtils.join(buffers)
                .flatMap(buffer -> {
                    try {
                        if (buffer.readableByteCount() < HEADER_SIZE) {
                            return Mono.empty(); // Not enough data for header
                        }

                        byte[] header = new byte[HEADER_SIZE];
                        buffer.read(header);

                        if (!Arrays.equals(Arrays.copyOfRange(header, 0, 3), ID3_IDENTIFIER)) {
                            return Mono.empty(); // Not an ID3 tag
                        }

                        int majorVersion = header[3];
                        int minorVersion = header[4];
                        byte flags = header[5];
                        int size = decodeSyncsafeInteger(header, 6);

                        if (majorVersion != 3) {
                            return Mono.error(
                                    new UnsupportedOperationException("Only ID3v2.3.0 is supported, found version 2."
                                            + majorVersion + "." + minorVersion));
                        }

                        Id3v2Tag tag = new Id3v2Tag();
                        tag.setVersion("3." + minorVersion);
                        tag.setFlags(flags);
                        tag.setSize(size);

                        int bytesRead = 0;
                        while (bytesRead < size && buffer.readableByteCount() >= FRAME_HEADER_SIZE) {
                            byte[] frameHeader = new byte[FRAME_HEADER_SIZE];
                            buffer.read(frameHeader);

                            String frameId = new String(frameHeader, 0, 4);
                            if (frameId.charAt(0) == 0) {
                                // Padding reached
                                break;
                            }

                            int frameSize = ByteBuffer.wrap(frameHeader, 4, 4).getInt();
                            byte[] frameFlags = Arrays.copyOfRange(frameHeader, 8, 10);

                            bytesRead += FRAME_HEADER_SIZE;

                            if (frameSize > 0 && buffer.readableByteCount() >= frameSize) {
                                byte[] frameData = new byte[frameSize];
                                buffer.read(frameData);
                                bytesRead += frameSize;

                                logger.debug("Read frame {} with {} bytes", frameId, frameSize);
                                Id3v2Frame frame = new Id3v2Frame(frameId, frameSize, frameFlags, frameData);
                                tag.addFrame(frame);
                            } else {
                                // Invalid frame size or not enough data
                                logger.debug("Reached invalid frame size or end of stream at {} bytes read", bytesRead);
                                break;
                            }
                        }

                        logger.info("Successfully read ID3v2.3.0 tag with {} frames", tag.getFrames().size());
                        return Mono.just(tag);
                    } finally {
                        DataBufferUtils.release(buffer);
                    }
                });
    }

    private int decodeSyncsafeInteger(byte[] data, int offset) {
        return ((data[offset] & 0x7F) << 21) |
                ((data[offset + 1] & 0x7F) << 14) |
                ((data[offset + 2] & 0x7F) << 7) |
                (data[offset + 3] & 0x7F);
    }
}
