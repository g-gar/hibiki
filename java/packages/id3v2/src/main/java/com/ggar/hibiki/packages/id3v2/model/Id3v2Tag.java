package com.ggar.hibiki.packages.id3v2.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Id3v2Tag {
    private String version = "3.0";
    private byte flags;
    private int size;
    private List<Id3v2Frame> frames = new ArrayList<>();

    public void addFrame(Id3v2Frame frame) {
        frames.add(frame);
    }

    public List<Id3v2Frame> getFramesById(Id3v2FrameId frameId) {
        return getFramesById(frameId.name());
    }

    public List<Id3v2Frame> getFramesById(String id) {
        return frames.stream()
                .filter(frame -> frame.getId().equals(id))
                .toList();
    }
}
