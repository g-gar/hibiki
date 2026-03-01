package com.ggar.hibiki.packages.id3v2.model;

import lombok.Getter;

/**
 * ID3v2.3.0 declared frames.
 * Based on https://id3.org/id3v2.3.0
 */
@Getter
public enum Id3v2FrameId {

    // 4.1. Unique file identifier
    UFID("Unique file identifier"),

    // 4.2. Text information frames
    TALB("Album/Movie/Show title"),
    TBPM("BPM (beats per minute)"),
    TCOM("Composer"),
    TCON("Content type"),
    TCOP("Copyright message"),
    TDAT("Date"),
    TDLY("Playlist delay"),
    TENC("Encoded by"),
    TEXT("Lyricist/Text writer"),
    TFLT("File type"),
    TIME("Time"),
    TIT1("Content group description"),
    TIT2("Title/songname/content description"),
    TIT3("Subtitle/Description refinement"),
    TKEY("Initial key"),
    TLAN("Language(s)"),
    TLEN("Length"),
    TMED("Media type"),
    TOAL("Original album/movie/show title"),
    TOFN("Original filename"),
    TOLY("Original lyricist(s)/text writer(s)"),
    TOPE("Original artist(s)/performer(s)"),
    TORY("Original release year"),
    TOWN("File owner/licensee"),
    TPE1("Lead performer(s)/Soloist(s)"),
    TPE2("Band/orchestra/accompaniment"),
    TPE3("Conductor/performer refinement"),
    TPE4("Interpreted, remixed, or otherwise modified by"),
    TPOS("Part of a set"),
    TPUB("Publisher"),
    TRCK("Track number/Position in set"),
    TRDA("Recording dates"),
    TRSN("Internet radio station name"),
    TRSO("Internet radio station owner"),
    TSIZ("Size"),
    TSRC("ISRC (international standard recording code)"),
    TSSE("Software/Hardware and settings used for encoding"),
    TYER("Year"),

    // 4.2.2. User defined text information frame
    TXXX("User defined text information frame"),

    // 4.3. URL link frames
    WCOM("Commercial information"),
    WCOP("Copyright/Legal information"),
    WOAF("Official audio file webpage"),
    WOAR("Official artist/performer webpage"),
    WOAS("Official audio source webpage"),
    WORS("Official internet radio station homepage"),
    WPAY("Payment"),
    WPUB("Publishers official webpage"),

    // 4.3.2. User defined URL link frame
    WXXX("User defined URL link frame"),

    // 4.11. Comments
    COMM("Comments"),

    // 4.15. Attached picture
    APIC("Attached picture"),

    // Add remaining or generic support
    UNKNOWN("Unknown Frame");

    private final String description;

    Id3v2FrameId(String description) {
        this.description = description;
    }

    public static Id3v2FrameId fromString(String frameId) {
        try {
            return valueOf(frameId);
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }
}
