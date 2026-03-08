package com.ggar.hibiki.features.library.port;

import com.ggar.hibiki.features.library.model.AccessIntent;
import com.ggar.hibiki.features.library.model.User;
import java.util.List;

/**
 * Interface for contributing access control logic to library queries in an implementation-agnostic way.
 * Implementations provide "Intents" that the persistence layer translates into specific queries.
 */
public interface LibraryAccessContributor {

    /**
     * Gets the list of access intents for a specific user.
     *
     * @param user The user requesting the library.
     * @return A list of AccessIntent objects.
     */
    List<AccessIntent> getAccessIntents(User user);
}
