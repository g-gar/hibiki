package com.ggar.hibiki.core.catalog.usecase.handler.event;

import org.springframework.stereotype.Component;

@Component
public class IngestionCompletedEventHandler {

    // TODO: Implement event handling logic here.
    // We will subscribe to an event from features:ingestion or features:analysis
    // which gives us the ID3 tags, and we will dispatch our Commands to create
    // Artists, Albums, and Songs.

    /*
     * Example:
     *
     * @EventListener
     * public void onIngestionCompleted(IngestionCompletedEvent event) {
     * // Read ID3v2 Tags from Event Payload
     * // Dispatch CreateArtistCommand, CreateAlbumCommand, CreateSongCommand
     * }
     */
}
