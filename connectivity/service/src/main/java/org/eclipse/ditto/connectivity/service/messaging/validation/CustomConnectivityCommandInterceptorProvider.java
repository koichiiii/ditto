/*
 * Copyright (c) 2022 Contributors to the Eclipse Foundation
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
package org.eclipse.ditto.connectivity.service.messaging.validation;

import static org.eclipse.ditto.base.model.common.ConditionChecker.checkNotNull;

import org.eclipse.ditto.base.service.DittoExtensionPoint;
import org.eclipse.ditto.connectivity.model.signals.commands.ConnectivityCommandInterceptor;

import akka.actor.ActorSystem;

public interface CustomConnectivityCommandInterceptorProvider extends DittoExtensionPoint {

    String CONFIG_PATH = "ditto.connectivity.connection.custom-command-interceptor-provider";

    ConnectivityCommandInterceptor getCommandInterceptor();

    /**
     * Loads the implementation of {@code CustomConnectivityCommandInterceptorProvider} which is configured for the
     * {@code ActorSystem}.
     *
     * @param actorSystem the actorSystem in which the {@code CustomConnectivityCommandInterceptorProvider} should be loaded.
     * @return the {@code CustomConnectivityCommandInterceptorProvider} implementation.
     * @throws NullPointerException if {@code actorSystem} is {@code null}.
     */
    static CustomConnectivityCommandInterceptorProvider get(final ActorSystem actorSystem) {
        checkNotNull(actorSystem, "actorSystem");
        final var implementation = actorSystem.settings().config().getString(CONFIG_PATH);
        return new ExtensionId<>(implementation, CustomConnectivityCommandInterceptorProvider.class).get(actorSystem);
    }

}
