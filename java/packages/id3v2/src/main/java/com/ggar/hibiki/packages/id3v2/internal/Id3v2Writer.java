package com.ggar.hibiki.packages.id3v2.internal;

import com.ggar.hibiki.packages.id3v2.model.Id3v2Frame;
import com.ggar.hibiki.packages.id3v2.model.Id3v2Tag;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
public class Id3v2Writer {

    private static final byte[] ID3_IDENTIFIER = {'I', 'D', '3'};
    private static final int HEADER_SIZE = 10;
    private static final int FRAME_HEADER_SIZE = 10;

    public Id3v2Writer() {}

    public Mono<Void> writeTag(Path filePath, Id3v2Tag tag) {
        log.debug("Writing ID3 Tag to File path: {}", filePath);
        return Mono.using(
                () -> AsynchronousFileChannel.open(filePath, StandardOpenOption.WRITE, StandardOpenOption.CREATE),
                channel -> DataBufferUtils.write(createBufferFlux(tag), channel).then(),
                channel -> {
                    try {
                        channel.close();
                    } catch (Exception e) {
                        log.warn("Failed to close channel", e);
                    }
                });
    }

    public Mono<Void> writeTag(OutputStream outputStream, Id3v2Tag tag) {
        log.debug("Writing ID3 Tag to OutputStream");
        return DataBufferUtils.write(createBufferFlux(tag), outputStream).then();
    }

    private Flux<DataBuffer> createBufferFlux(Id3v2Tag tag) {
        return Flux.defer(() -> {
            DefaultDataBufferFactory factory = new DefaultDataBufferFactory();
            DataBuffer buffer = factory.allocateBuffer();

            // 1. Calculate tag size
            int tagSize = 0;
            List<Id3v2Frame> frames = tag.getFrames();
            for (Id3v2Frame frame : frames) {
                tagSize += FRAME_HEADER_SIZE + (frame.getData() != null ? frame.getData().length : 0);
            }
            tag.setSize(tagSize);

            // 2. Write Header (10 bytes)
            buffer.write(ID3_IDENTIFIER);

            // Version
            String[] v = tag.getVersion().split("\\.");
            int major = v.length > 0 ? Integer.parseInt(v[0]) : 3;
            int minor = v.length > 1 ? Integer.parseInt(v[1]) : 0;

            // Only ID3v2.3.* is supported (major version 3)
            buffer.write((byte) 3);
            buffer.write((byte) minor);

            // Flags
            buffer.write(tag.getFlags());

            // Size (Syncsafe integer)
            buffer.write(encodeSyncsafeInteger(tagSize));

            // 3. Write Frames
            for (Id3v2Frame frame : frames) {
                // Frame ID
                String id = frame.getId();
                if (id == null) {
                    id = "    ";
                }
                while (id.length() < 4) id += " "; // Pad if necessary
                buffer.write(id.substring(0, 4).getBytes());

                // Frame Size
                int size = frame.getData() != null ? frame.getData().length : 0;
                byte[] sizeBytes = ByteBuffer.allocate(4).putInt(size).array();
                buffer.write(sizeBytes);

                // Frame Flags
                byte[] flags =
                        frame.getFlags() != null && frame.getFlags().length == 2 ? frame.getFlags() : new byte[2];
                buffer.write(flags);

                // Frame Data
                if (frame.getData() != null) {
                    buffer.write(frame.getData());
                }
            }

            log.info(
                    "Successfully scheduled write of ID3v2.3.0 tag with {} frames and {} bytes",
                    frames.size(),
                    tagSize);
            return Mono.just(buffer);
        });
    }

    private byte[] encodeSyncsafeInteger(int value) {
        byte[] encoded = new byte[4];
        encoded[0] = (byte) ((value >> 21) & 0x7F);
        encoded[1] = (byte) ((value >> 14) & 0x7F);
        encoded[2] = (byte) ((value >> 7) & 0x7F);
        encoded[3] = (byte) (value & 0x7F);
        return encoded;
    }
}
