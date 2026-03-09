package com.ggar.hibiki.features.entitlements.service;

import com.ggar.hibiki.features.entitlements.port.SubscriptionLibraryAccessContributor;
import com.ggar.hibiki.features.library.model.AccessIntent;
import com.ggar.hibiki.features.library.model.User;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 * Implementation of SubscriptionLibraryAccessContributor.
 */
@Service
public class SubscriptionLibraryAccessContributorImpl implements SubscriptionLibraryAccessContributor {

    @Override
    public List<AccessIntent> getAccessIntents(User user) {
        // In a real scenario, we would fetch the user's tier from a database or context
        // For this example, we'll assume the user has a "PRO" tier if their ID ends with "0"
        boolean isPro = user.getId().toString().endsWith("0");

        if (isPro) {
            // Pro users might not need extra filters, or they might get access to exclusive content
            return Collections.emptyList();
        }

        // Free users get a filter intent
        return List.of(AccessIntent.builder()
                .id(AccessIntent.ENTITLEMENT_FILTER)
                .type(AccessIntent.Type.RESTRICTION)
                .parameters(Map.of("requiredTier", "FREE"))
                .build());
    }
}
