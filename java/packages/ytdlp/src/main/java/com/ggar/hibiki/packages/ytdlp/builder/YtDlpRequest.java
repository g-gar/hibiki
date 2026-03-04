package com.ggar.hibiki.packages.ytdlp.builder;

import com.ggar.hibiki.packages.ytdlp.model.VideoMetadata;
import com.ggar.hibiki.packages.ytdlp.model.VideoSearchResult;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * A fluent builder block for configuring and executing yt-dlp metadata and
 * search requests.
 */
public interface YtDlpRequest {

    /**
     * Instructs yt-dlp to treat the target as a flat playlist.
     * This rapidly extracts the entries of a playlist or channel without fully
     * resolving individual player pages, saving significant bandwidth and time.
     *
     * @return this builder
     */
    YtDlpRequest flatPlaylist();

    /**
     * Caps the maximum number of results yt-dlp will process or return.
     * Useful for search queries or large playlists to prevent out-of-memory errors
     * or extremely long processing times.
     *
     * @param limit The maximum number of results.
     * @return this builder
     */
    YtDlpRequest maxResults(int limit);

    /**
     * Executes the configured yt-dlp request using `--dump-json`.
     * This returns a reactive stream of generic VideoSearchResult items, suitable
     * for searches and playlist extraction where deep formats/chapters are not
     * immediately needed.
     *
     * @return A Flux emitting each parsed result line from yt-dlp stdout.
     */
    Flux<VideoSearchResult> execute();

    /**
     * Executes the configured yt-dlp request specifically for a single video to
     * extract
     * deep metadata. This maps the JSON payload into the rich VideoMetadata model,
     * including chapter markers and stream formats.
     *
     * @return A Mono emitting the deeply parsed metadata.
     */
    Mono<VideoMetadata> executeForDeepMetadata();

}
