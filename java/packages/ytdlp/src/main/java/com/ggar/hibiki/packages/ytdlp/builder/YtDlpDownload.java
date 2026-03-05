package com.ggar.hibiki.packages.ytdlp.builder;

import com.ggar.hibiki.packages.ytdlp.model.DownloadEvent;
import reactor.core.publisher.Flux;

/**
 * A fluent builder block for configuring and executing yt-dlp media downloads.
 */
public interface YtDlpDownload {

    /**
     * Instructs yt-dlp to extract the audio layer from the downloaded container
     * (usually relying on ffmpeg installed on the host OS).
     *
     * @return this builder
     */
    YtDlpDownload extractAudio();

    /**
     * Specifies the target audio format if extractAudio is used.
     *
     * @param format Examples: "mp3", "m4a", "flac", "best".
     * @return this builder
     */
    YtDlpDownload audioFormat(String format);

    /**
     * Specifies the audio quality if extractAudio is used and the format supports
     * it.
     *
     * @param quality A number from 0 (best) to 9 (worst) for VBR, or a specific
     *                bitrate like 320.
     * @return this builder
     */
    YtDlpDownload audioQuality(int quality);

    /**
     * Instructs yt-dlp to write a `.info.json` file containing the deep metadata
     * alongside the downloaded media files. This eliminates the need for a separate
     * metadata fetch if chapters or metadata are required strictly after a valid
     * download.
     *
     * @return this builder
     */
    YtDlpDownload writeInfoJson();

    /**
     * Overrides the default yt-dlp output template naming convention.
     *
     * @param template e.g. "%(title)s.%(ext)s"
     * @return this builder
     */
    YtDlpDownload outputTemplate(String template);

    /**
     * Finalizes the configuration and starts executing the yt-dlp download process.
     * It actively monitors the stdout stream to parse and emit granular lifecycle
     * progress events so the caller can checkpoint work or update UIs.
     *
     * @param targetDirectory The absolute path to the directory where media and
     *                        files should be written.
     * @return A Flux stream emitting the download lifecycle status.
     */
    Flux<DownloadEvent> executeTo(String targetDirectory);
}
