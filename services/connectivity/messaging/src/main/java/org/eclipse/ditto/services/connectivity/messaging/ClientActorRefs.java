/*
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
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
package org.eclipse.ditto.services.connectivity.messaging;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.concurrent.NotThreadSafe;

import org.eclipse.ditto.services.utils.pubsub.PubSubFactory;

import akka.actor.ActorPath;
import akka.actor.ActorRef;

/**
 * Collection of all client actor refs of a connection actor.
 */
@NotThreadSafe
public final class ClientActorRefs {

    private final Map<ActorPath, ActorRef> refsByPath = new HashMap<>();
    private List<ActorRef> sortedRefs = List.of();

    private ClientActorRefs() {}

    /**
     * Create a new empty store of client actor refs.
     *
     * @return an empty store.
     */
    public static ClientActorRefs empty() {
        return new ClientActorRefs();
    }

    /**
     * Add a new client actor.
     *
     * @param newClientActor the new client actor.
     */
    public void add(final ActorRef newClientActor) {
        refsByPath.put(newClientActor.path(), newClientActor);
        sortedRefs = sort(refsByPath);
    }

    public void remove(final ActorRef deadClientActor) {
        refsByPath.remove(deadClientActor.path());
        sortedRefs = sort(refsByPath);
    }

    /**
     * Remove all known client actors.
     */
    public void clear() {
        refsByPath.clear();
        sortedRefs = List.of();
    }

    /**
     * Get stored client actor refs other than the one given.
     *
     * @param clientActor the given client actor.
     * @return stored client actor refs different from the given actor.
     */
    public List<ActorRef> getOtherActors(final ActorRef clientActor) {
        return sortedRefs.stream()
                .filter(actorRef -> !actorRef.equals(clientActor))
                .collect(Collectors.toList());
    }

    /**
     * Get the client actor responsible for a hash key, usually the entity ID of a signal.
     * Only works if:
     * - All client actors have started and have not crashed,
     * - All client actors know about all other client actors,
     * - All client actors run on the same Connectivity instance, or no more than 1 client actor runs on an instance.
     *
     * @param hashKey the hash key to determine.
     * @return the client actor responsible for it.
     */
    public Optional<ActorRef> lookup(final CharSequence hashKey) {
        if (sortedRefs.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(sortedRefs.get(PubSubFactory.hashForPubSub(hashKey) % sortedRefs.size()));
        }
    }

    private static List<ActorRef> sort(final Map<ActorPath, ActorRef> refsByPath) {
        return refsByPath.values().stream().sorted(ActorRef::compareTo).collect(Collectors.toList());
    }
}
