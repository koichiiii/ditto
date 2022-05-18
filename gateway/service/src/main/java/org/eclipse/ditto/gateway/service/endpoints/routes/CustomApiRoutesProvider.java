/*
 * Copyright (c) 2017 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.ditto.gateway.service.endpoints.routes;

import static org.eclipse.ditto.base.model.common.ConditionChecker.checkNotNull;

import org.eclipse.ditto.base.model.headers.DittoHeaders;
import org.eclipse.ditto.base.model.json.JsonSchemaVersion;
import org.eclipse.ditto.base.service.DittoExtensionPoint;

import akka.actor.ActorSystem;
import akka.http.javadsl.server.Route;

/**
 * Provider for custom routes.
 * You can distinguish between routes for unauthorized access and authorized access.
 */
public interface CustomApiRoutesProvider extends DittoExtensionPoint {

    String CONFIG_PATH = "ditto.gateway.http.custom-api-routes-provider";

    /**
     * Provides a custom route for unauthorized access.
     *
     * @param routeBaseProperties the basic properties of the root route.
     * @param version the API version.
     * @param correlationId the correlation ID.
     * @return custom route for unauthorized access.
     */
    Route unauthorized(RouteBaseProperties routeBaseProperties, JsonSchemaVersion version, CharSequence correlationId);

    /**
     * Provides a custom route for authorized access.
     *
     * @param routeBaseProperties the basic properties of the root route.
     * @param headers headers of the request.
     * @return custom route for authorized access.
     */
    Route authorized(RouteBaseProperties routeBaseProperties, DittoHeaders headers);

    /**
     * Loads the implementation of {@code CustomApiRoutesProvider} which is configured for the {@code ActorSystem}.
     *
     * @param actorSystem the actorSystem in which the {@code CustomApiRoutesProvider} should be loaded.
     * @return the {@code CustomApiRoutesProvider} implementation.
     * @throws NullPointerException if {@code actorSystem} is {@code null}.
     * @since 3.0.0
     */
    static CustomApiRoutesProvider get(final ActorSystem actorSystem) {
        checkNotNull(actorSystem, "actorSystem");
        final var implementation = actorSystem.settings().config().getString(CONFIG_PATH);

        return new ExtensionId<>(implementation, CustomApiRoutesProvider.class).get(actorSystem);
    }

}
