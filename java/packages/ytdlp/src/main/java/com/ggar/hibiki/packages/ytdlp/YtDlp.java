package com.ggar.hibiki.packages.ytdlp;

import com.ggar.hibiki.packages.ytdlp.builder.YtDlpDownload;
import com.ggar.hibiki.packages.ytdlp.builder.YtDlpRequest;

/**
 * The main entry point for interacting with yt-dlp.
 * This facade provides fluent builders to configure commands,
 * reducing redundant OS process spawns by combining operations
 * (like search + metadata extraction, or download + info json writing).
 */
public interface YtDlp {

    /**
     * Prepares a request to fetch metadata or search results from YouTube.
     *
     * @param urlOrSearch The video URL, playlist URL, channel URL, or a search
     *                    query (e.g., "ytsearch5:slipknot")
     * @return A builder to configure and execute the metadata request.
     */
    YtDlpRequest request(String urlOrSearch);

    /**
     * Prepares a download operation for a specific media URL.
     *
     * @param url The video URL or ID to download.
     * @return A builder to configure and execute the download operation.
     */
    YtDlpDownload download(String url);
}
