package com.ggar.hibiki.core.orchestrator.usecase;

import com.ggar.hibiki.core.shared.mediator.Mediator;

/**
 * Base abstract class for Orchestrator Use Cases.
 * Provides access to the Mediator for dispatching commands and queries across
 * domains.
 */
public abstract class BaseOrchestratorUseCase {

    protected final Mediator mediator;

    protected BaseOrchestratorUseCase(Mediator mediator) {
        this.mediator = mediator;
    }
}
