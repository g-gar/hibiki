package com.ggar.hibiki.features.library.infrastructure.persistence.access;

import com.ggar.hibiki.features.library.model.AccessIntent;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * Translates the ENTITLEMENT_FILTER intent into Cypher.
 */
@Component
public class EntitlementFilterTranslator implements AccessIntentTranslator {

    @Override
    public Optional<String> translate(AccessIntent intent, Map<String, Object> parameters) {
        // This fragment is a RESTRICTION (added via WHERE)
        String requiredTier = (String) intent.getParameters().get("requiredTier");
        parameters.put("requiredTier", requiredTier);

        // Simple example: ensure item tier is within user's entitlement
        // (Assuming li has a tier property)
        return Optional.of("li.tier <= $requiredTier");
    }

    @Override
    public boolean supports(String intentId) {
        return AccessIntent.ENTITLEMENT_FILTER.equals(intentId);
    }
}
