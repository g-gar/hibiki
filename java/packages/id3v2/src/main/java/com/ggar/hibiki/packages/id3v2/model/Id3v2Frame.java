package com.ggar.hibiki.packages.id3v2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Id3v2Frame {
    private String id;
    private int size;
    private byte[] flags;
    private byte[] data;

    public Id3v2FrameId getFrameId() {
        return Id3v2FrameId.fromString(id);
    }
}
