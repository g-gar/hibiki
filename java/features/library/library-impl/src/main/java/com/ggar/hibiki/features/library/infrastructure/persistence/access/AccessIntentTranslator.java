package com.ggar.hibiki.features.library.infrastructure.persistence.access;

import com.ggar.hibiki.features.library.model.AccessIntent;
import java.util.Map;
import java.util.Optional;

/**
 * Interface for translating implementation-agnostic AccessIntents into Cypher fragments.
 */
public interface AccessIntentTranslator {

    /**
     * Translates an intent into a Cypher fragment.
     *
     * @param intent The intent to translate.
     * @param parameters Map to add parameters to for binding.
     * @return An optional Cypher fragment.
     */
    Optional<String> translate(AccessIntent intent, Map<String, Object> parameters);

    /**
     * Checks if this translator supports a specific intent ID.
     */
    boolean supports(String intentId);
}
