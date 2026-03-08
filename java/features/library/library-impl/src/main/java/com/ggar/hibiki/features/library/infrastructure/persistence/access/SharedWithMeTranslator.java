package com.ggar.hibiki.features.library.infrastructure.persistence.access;

import com.ggar.hibiki.features.library.model.AccessIntent;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * Translates the SHARED_WITH_ME intent into Cypher.
 */
@Component
public class SharedWithMeTranslator implements AccessIntentTranslator {

    @Override
    public Optional<String> translate(AccessIntent intent, Map<String, Object> parameters) {
        // This fragment is an EXPANSION (added via UNION)
        return Optional.of(
                """
               WITH u
               MATCH (u)<-[:SHARED_WITH]-(li:LibraryItem)
               WHERE ($types IS NULL OR size($types) = 0 OR any(t IN $types WHERE li:_type = t))
               RETURN li, li.addedAt as sortKey, labels(li) as labels
               """);
    }

    @Override
    public boolean supports(String intentId) {
        return AccessIntent.SHARED_WITH_ME.equals(intentId);
    }
}
