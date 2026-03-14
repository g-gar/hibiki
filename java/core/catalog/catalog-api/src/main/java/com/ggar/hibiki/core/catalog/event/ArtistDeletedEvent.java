package com.ggar.hibiki.core.catalog.event;

import com.ggar.hibiki.core.shared.event.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArtistDeletedEvent implements DomainEvent {
    private UUID artistId;
    private String name;
}
